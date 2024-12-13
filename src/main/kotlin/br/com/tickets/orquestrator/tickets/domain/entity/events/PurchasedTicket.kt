package br.com.tickets.orquestrator.tickets.domain.entity.events

import br.com.tickets.orquestrator.tickets.domain.entity.user.User
import jakarta.persistence.*

@Entity
@Table(name = "purchased_tickets")
data class PurchasedTicket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    val ticketType: Ticket,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val buyer: User
)
