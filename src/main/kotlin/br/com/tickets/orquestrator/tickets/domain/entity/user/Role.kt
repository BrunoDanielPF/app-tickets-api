package br.com.tickets.orquestrator.tickets.domain.entity.user

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(length = 60)
    var name: String
) : Serializable