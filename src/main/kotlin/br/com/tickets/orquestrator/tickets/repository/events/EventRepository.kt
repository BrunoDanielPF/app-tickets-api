package br.com.tickets.orquestrator.tickets.repository.events

import br.com.tickets.orquestrator.tickets.domain.entity.events.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EventRepository: JpaRepository<Event, Long> {

    fun findByOrganizerId(organizerId: Long): Optional<List<Event>>
    @Query("SELECT e FROM Event e ORDER BY e.createdOn DESC")
    fun findTop15EventsByCreatedOn(): List<Event>
}