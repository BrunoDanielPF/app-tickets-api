package br.com.tickets.orquestrator.tickets.domain.entity.events

import br.com.tickets.orquestrator.tickets.domain.entity.Image
import br.com.tickets.orquestrator.tickets.domain.entity.user.User
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    val eventHasPublish: Boolean? = false,

    @Column(nullable = false)
    val totalTickets: Int,

    @Column(nullable = false)
    val location: String,

    @Column(nullable = false)
    val date: LocalDateTime,

    @Column(nullable = false, length = 500)
    val description: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JsonProperty("imagem")
    var image: Image? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    @JsonBackReference
    val organizer: User,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
    val ticketTypes: List<Ticket> = mutableListOf(),

    @CreationTimestamp
    @JsonProperty("criado_em")
    val createdOn: Instant? = null,

    @UpdateTimestamp
    @JsonProperty("atualizado_em")
    val lastUpdatedOn: Instant? = null
) {
    override fun toString(): String {
        return "Event(lastUpdatedOn=$lastUpdatedOn, createdOn=$createdOn, description='$description', date=$date, location='$location', eventHasPublish=$eventHasPublish, name='$name', id=$id)"
    }

    fun ticketsRemaining(): Int {
        return totalTickets - ticketTypes.count { it.buyer != null }
    }
}