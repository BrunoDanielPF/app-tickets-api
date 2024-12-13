package br.com.tickets.orquestrator.tickets.controller.user.authentication

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class AuthenticationDTO(
    @field:NotNull(message = "email cannot be null")
    @JsonProperty("e-mail")
    val email: String,

    @field:NotNull(message = "password cannot be null")
    @JsonProperty("senha")
    val password: String
)
