package br.com.tickets.orquestrator.tickets.controller.events

import br.com.tickets.orquestrator.tickets.controller.events.dto.EventRequest
import br.com.tickets.orquestrator.tickets.controller.events.dto.TicketEventRequest
import br.com.tickets.orquestrator.tickets.controller.events.dto.TicketRequest
import br.com.tickets.orquestrator.tickets.domain.entity.events.Event
import br.com.tickets.orquestrator.tickets.services.EventService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class EventController(
    val eventService: EventService
) {

    @PostMapping("/{userOrganizerId}")
    @Operation(
        summary = "Cria evento com o organizador",
        security = [SecurityRequirement(name = "basicScheme")]
    )
    @PreAuthorize("hasRole('USER_ORGANIZER') || hasRole('USER_ADMIN')")
    fun createEvent(
        @PathVariable(name = "userOrganizerId") userOrganizerId: Long,
        @RequestBody eventRequest: EventRequest
    ): ResponseEntity<Event> {
        return ResponseEntity.ok(eventService.createEvent(eventRequest, userOrganizerId))
    }

//    @PostMapping("/{eventId}/{ticketId}")
//    @Operation(
//        summary = "Compra y ingresso para evento x",
//        security = [SecurityRequirement(name = "basicScheme")]
//    )
//    fun buyTicketFromEvent(
//        @PathVariable(name = "eventId") eventId: Long,
//        @PathVariable(name = "ticketId") ticketId: Long,
//        @RequestBody ticketRequest: TicketRequest
//    ): ResponseEntity<Unit> {
//        return ResponseEntity.ok(eventService.buyTicket(ticketId, ticketRequest.buyerEmail))
//    }

    @PostMapping("/ticket/{eventId}")
    @Operation(
        summary = "Cria ingressos para evento",
        security = [SecurityRequirement(name = "basicScheme")]
    )
    @PreAuthorize("hasRole('USER_ORGANIZER') || hasRole('USER_ADMIN')")
    fun createTicketsToEvent(
        @PathVariable(name = "eventId") eventId: Long,
        @RequestBody ticketEventRequest: TicketEventRequest
    ): ResponseEntity<Event> {
        return ResponseEntity.ok(eventService.createTicketToEvent(eventId, ticketEventRequest))
    }

    @GetMapping
    @Operation(
        summary = "recupera lista de eventos criado pelo organizador"
    )
    fun getAllEventsFromUser(
        @RequestParam(name = "id", required = true) id: Long,
    ): ResponseEntity<List<Event>> {
        return ResponseEntity.ok(eventService.getEventsFromOrganizer(id))
    }
}