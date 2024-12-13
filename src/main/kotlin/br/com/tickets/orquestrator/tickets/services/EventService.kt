package br.com.tickets.orquestrator.tickets.services

import br.com.tickets.orquestrator.tickets.controller.events.dto.EventRequest
import br.com.tickets.orquestrator.tickets.controller.events.dto.TicketEventRequest
import br.com.tickets.orquestrator.tickets.domain.entity.events.Event
import br.com.tickets.orquestrator.tickets.domain.entity.events.Ticket
import br.com.tickets.orquestrator.tickets.exceptions.UserNoHasOrganizerException
import br.com.tickets.orquestrator.tickets.repository.events.EventRepository
import br.com.tickets.orquestrator.tickets.repository.events.PurchasedTicketRepository
import br.com.tickets.orquestrator.tickets.repository.events.TicketRepository
import br.com.tickets.orquestrator.tickets.services.feign.AsaasClient
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class EventService(
    val eventRepository: EventRepository,
    val ticketRepository: TicketRepository,
    val userService: UserService,
    val purchasedTicketRepository: PurchasedTicketRepository,
    val assasClient: AsaasClient
) {

    fun getEventsFromOrganizer(id: Long): List<Event> {
        val eventList = eventRepository.findByOrganizerId(id).orElseThrow {
            throw RuntimeException("User Not found")
        }
        return eventList
    }

    fun createEvent(eventRequest: EventRequest, userOrganizerId: Long): Event {
        if (userService.isUserOrganizer(userOrganizerId)) {
            val user = userService.getUser(userOrganizerId)
            val eventCreated = eventRepository.save(
                Event(
                    name = eventRequest.name,
                    location = eventRequest.location,
                    date = LocalDateTime.now(),
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

//    fun buyTicket(ticketId: Long, buyerEmail: String) {
//
//        val ticketType = ticketRepository.findById(ticketId).orElseThrow {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de ingresso não encontrado.")
//        }
//
//        if (ticketType.sold >= ticketType.limit) {
//            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Ingressos esgotados para este tipo.")
//        }
//
//        try{
//            val buyer = userService.getUser(email = buyerEmail)
//        }catch (e: Exception) {
//
//        }
//
//
//        ticketType.sold += 1
//
//        buyer.purchasedTickets.add(ticketType)
//
//        ticketRepository.save(ticketType)
//
//        val purchasedTicket = PurchasedTicket(
//            ticketType = ticketType,
//            buyer = buyer
//        )
//        userService.saveUser(buyer)
//        purchasedTicketRepository.save(purchasedTicket)
//        assasClient.createCustomer(customerRequest = )
//        return
//    }

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