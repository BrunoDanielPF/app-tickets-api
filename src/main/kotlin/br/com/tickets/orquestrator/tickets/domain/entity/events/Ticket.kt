package br.com.tickets.orquestrator.tickets.domain.entity.events

import br.com.tickets.orquestrator.tickets.domain.entity.user.User
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "tickets")
data class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val isVip: Boolean,

    @Column(nullable = false)
    val price: BigDecimal,

    @Column(nullable = false, name = "description_ticket")
    val description: String,

    @Column(nullable = false, name = "limit_tickets")
    val limit: Int,

    @Column(nullable = false)
    var sold: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference
    val event: Event,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var buyer: User? = null, // Null se o ingresso ainda não foi comprado

    @CreationTimestamp
    @JsonProperty("criado_em")
    val createdOn: Instant? = null,

    @UpdateTimestamp
    @JsonProperty("atualizado_em")
    val lastUpdatedOn: Instant? = null
) {
    override fun toString(): String {
        return "Ticket(id=$id, isVip=$isVip, price=$price, createdOn=$createdOn, lastUpdatedOn=$lastUpdatedOn)"
    }
}