package br.com.tickets.orquestrator.tickets.repository.events

import br.com.tickets.orquestrator.tickets.domain.entity.events.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository:JpaRepository<Ticket, Long> {

    fun findByBuyerId(userId: Long): List<Ticket>
}