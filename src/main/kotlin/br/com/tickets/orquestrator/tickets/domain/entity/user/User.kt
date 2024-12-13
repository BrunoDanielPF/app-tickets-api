package br.com.tickets.orquestrator.tickets.domain.entity.user

import br.com.tickets.orquestrator.tickets.domain.entity.Image
import br.com.tickets.orquestrator.tickets.domain.entity.events.Event
import br.com.tickets.orquestrator.tickets.domain.entity.events.Ticket
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Email(message = "invalid e-mail must count the following format e.g.: email@domain")
    @JsonProperty("e-mail")
    var email: String,

    @JsonProperty("nome")
    var name: String,

    @JsonProperty("senha")
    var password: String,

    var emailValidated: Boolean,

    var emailCode: Int? = null,

    var emailCodeExpiration: LocalDateTime? = null,

    var isOrganizer: Boolean = false,

    @OneToMany(mappedBy = "organizer", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    val events: MutableList<Event> = mutableListOf(), // Eventos criados pelo usuário SE for organizador

    @OneToMany(mappedBy = "buyer", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    val purchasedTickets: MutableList<Ticket> = mutableListOf(), // Ingressos comprados pelo usuário

    @OneToOne(cascade = [CascadeType.ALL])
    @JsonProperty("imagem")
    var image: Image? = null,

    @CreationTimestamp
    @JsonProperty("criado_em")
    val createdOn: Instant? = null,

    @UpdateTimestamp
    @JsonProperty("atualizado_em")
    val lastUpdatedOn: Instant? = null,

    @ManyToMany(targetEntity = Role::class)
    @JsonProperty("cargo")
    var roles: MutableList<Role> = mutableListOf()
) {
    override fun toString(): String {
        return "User(id=$id, email=$email, name=$name, isOrganizer=$isOrganizer)"
    }

    fun getTicketsByEvent(event: Event): List<Ticket> {
        return purchasedTickets.filter { it.event.id == event.id }
    }
}
