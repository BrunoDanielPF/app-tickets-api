package br.com.tickets.orquestrator.tickets.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

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
    var roles: List<Role> = mutableListOf()
)
