package br.com.tickets.orquestrator.tickets.controller.user

import br.com.tickets.orquestrator.tickets.domain.entity.events.Ticket
import br.com.tickets.orquestrator.tickets.domain.entity.user.User
import br.com.tickets.orquestrator.tickets.services.EventService
import br.com.tickets.orquestrator.tickets.services.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    val eventService: EventService
) {

    @PostMapping
    @Operation(summary = "Registra usuário como um organizador para ter acesso a eventos.")
    fun changeUserToOrganizer(
        @RequestParam(name = "email", required = false) email: String?,
        @RequestParam(name = "id", required = false) id: Long?,
        @RequestParam(name = "name", required = false) name: String?
    ): ResponseEntity<String> {
        return ResponseEntity.ok(userService.userToOrganizer(id, name, email))
    }


    @GetMapping
    @Operation(summary = "Recupera tickets comprados pelo usuario")
    fun getUserWithParam(
        @RequestParam(name = "email", required = false) email: String?,
        @RequestParam(name = "id", required = false) id: Long?,
        @RequestParam(name = "name", required = false) name: String?
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.getUser(id, name, email))
    }

    @GetMapping("/{userId}/tickets")
    fun getPurchasedTickets(@PathVariable userId: Long): ResponseEntity<List<Ticket>> {
        val tickets = eventService.getPurchasedTicketsByUser(userId)
        return ResponseEntity.ok(tickets)
    }
}