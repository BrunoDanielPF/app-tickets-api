package br.com.tickets.orquestrator.tickets.repository.events

import br.com.tickets.orquestrator.tickets.domain.entity.events.PurchasedTicket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchasedTicketRepository: JpaRepository<PurchasedTicket, Long>