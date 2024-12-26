package br.com.tickets.orquestrator.tickets.services

import br.com.tickets.orquestrator.tickets.controller.events.dto.EventRequest
import br.com.tickets.orquestrator.tickets.controller.events.dto.TicketEventRequest
import br.com.tickets.orquestrator.tickets.domain.entity.events.Event
import br.com.tickets.orquestrator.tickets.domain.entity.events.PurchasedTicket
import br.com.tickets.orquestrator.tickets.domain.entity.events.Ticket
import br.com.tickets.orquestrator.tickets.domain.entity.user.Role
import br.com.tickets.orquestrator.tickets.domain.entity.user.User
import br.com.tickets.orquestrator.tickets.exceptions.UserNoHasOrganizerException
import br.com.tickets.orquestrator.tickets.repository.events.EventRepository
import br.com.tickets.orquestrator.tickets.repository.events.PurchasedTicketRepository
import br.com.tickets.orquestrator.tickets.repository.events.TicketRepository
import br.com.tickets.orquestrator.tickets.services.feign.AsaasClient
import br.com.tickets.orquestrator.tickets.services.feign.dto.CustomerRequest
import br.com.tickets.orquestrator.tickets.services.feign.dto.PaymentEventRequest
import br.com.tickets.orquestrator.tickets.services.feign.dto.PixInformationRequest
import br.com.tickets.orquestrator.tickets.services.feign.dto.PixInformationResponse
import br.com.tickets.orquestrator.tickets.services.utils.generateEmailCode
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class EventService(
    val eventRepository: EventRepository,
    val ticketRepository: TicketRepository,
    val userService: UserService,
    val purchasedTicketRepository: PurchasedTicketRepository,
    val assasClient: AsaasClient,
    val environment: Environment
) {

    fun getEventsFromOrganizer(id: Long): List<Event> {
        val eventList = eventRepository.findByOrganizerId(id).orElseThrow {
            throw RuntimeException("User Not found")
        }
        return eventList
    }

    fun getRecentEvents() : List<Event> {
        return eventRepository.findTop15EventsByCreatedOn()
    }

    fun createEvent(eventRequest: EventRequest, userOrganizerId: Long): Event {
        if (userService.isUserOrganizer(userOrganizerId)) {
            val user = userService.getUser(userOrganizerId)
            val eventCreated = eventRepository.save(
                Event(
                    name = eventRequest.name,
                    location = eventRequest.location,
                    date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    description = eventRequest.description,
                    organizer = user,
                    totalTickets = eventRequest.totalTickets!!
                )
            )
            return eventCreated
        } else {
            throw UserNoHasOrganizerException("Usuário sem permissão para criar evento")
        }
    }

    fun createTicketToEvent(eventId: Long, ticketEventRequest: TicketEventRequest): Event? {
        val event = getEventById(eventId)
        ticketRepository.save(
            Ticket(
                isVip = ticketEventRequest.isVIP,
                price = ticketEventRequest.price,
                event = event!!,
                limit = ticketEventRequest.totalTickets,
                description = ticketEventRequest.description
            )
        )
        return getEventById(eventId)
    }

    fun buyTicket(ticketId: Long, paymentEventRequest: PaymentEventRequest): PixInformationResponse {
        // Passo 1: Validar a existência do ingresso
        val ticketType = ticketRepository.findById(ticketId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de ingresso não encontrado.")
        }

        // Passo 2: Verificar se os ingressos ainda estão disponíveis
        if (ticketType.sold >= ticketType.limit) {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Ingressos esgotados para este tipo.")
        }

        // Passo 3: Validar e criar o usuário, se necessário
        val emailBuyer = paymentEventRequest.customerRequest.email
        val buyer = getOrCreateBuyer(emailBuyer, paymentEventRequest.customerRequest)

        // Incrementar o número de ingressos vendidos
        ticketType.sold += 1
        ticketRepository.save(ticketType)

        // Associar o ingresso ao comprador
        buyer.purchasedTickets.add(ticketType)
        userService.saveUser(buyer)

        // Salvar o ingresso comprado
        val purchasedTicket = PurchasedTicket(ticketType = ticketType, buyer = buyer)
        purchasedTicketRepository.save(purchasedTicket)

        // Criar o cliente no Assas, caso ainda não tenha sido criado
        assasClient.createCustomer(customerRequest = paymentEventRequest.customerRequest)

        // Gerar o código Pix com data de expiração
        val expirationTime = LocalDateTime.now().plusMinutes(30)
        val formattedExpiration = expirationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        return assasClient.createPix(
            PixInformationRequest(
                addressKey = environment.getProperty("CHAVE_PIX_TECHLIVITY")!!,
                description = paymentEventRequest.description,
                value = paymentEventRequest.value,
                expirationDate = formattedExpiration
            )
        )
    }

    // Método auxiliar para verificar e criar o usuário, se necessário
    private fun getOrCreateBuyer(email: String, customerRequest: CustomerRequest): User {
        // Tentar buscar o usuário localmente
        val localUser = runCatching { userService.getUser(email = email) }.getOrNull()
        if (localUser != null) return localUser

        // Se o usuário não estiver na base local, verificar no cliente Assas
        val assasCustomer = assasClient.getCustomer(email)
        val assasUser = assasCustomer.data.firstOrNull()?.createUser()

        if (assasUser != null) {
            // Salvar usuário recuperado no cliente Assas na base local
            return userService.saveUser(assasUser)
        }

        // Se o usuário não existir em nenhuma das fontes, criar um novo usuário
        val newUser = User(
            email = email,
            name = customerRequest.name ?: "Nome não informado",
            password = null, // Implementar lógica para senha padrão, se necessário
            emailValidated = true,
            emailCode = generateEmailCode(),
            emailCodeExpiration = null,
            isOrganizer = false,
            roles = mutableListOf(Role(1, "USER"))
        )

        // Salvar novo usuário na base local e no cliente Assas
        userService.saveUser(newUser)
        assasClient.createCustomer(customerRequest)

        return newUser
    }


    fun createTicketType(eventId: Long, isVip: Boolean, price: BigDecimal, limit: Int): Ticket {
        val event =
            getEventById(eventId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado.")

        val ticketType = Ticket(
            isVip = isVip,
            price = price,
            limit = limit,
            event = event,
            description = ""
        )

        return ticketRepository.save(ticketType)
    }

    fun getPurchasedTicketsByUser(userId: Long): List<Ticket> {
        return ticketRepository.findByBuyerId(userId)
    }

    private fun getEventById(eventId: Long): Event? {
        return eventRepository.findById(eventId).orElseThrow {
            throw RuntimeException("Event not found")
        }
    }
}