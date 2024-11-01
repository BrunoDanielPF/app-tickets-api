package br.com.tickets.orquestrator.tickets.controller.authentication

import br.com.tickets.orquestrator.tickets.domain.entity.Role
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email

data class UserRequest(
    @field:Email(message = "invalid e-mail must count the following format e.g. : blablabla@domain")
    @JsonProperty("e-mail")
    val email: String,

    @JsonProperty("nome")
    val name: String,

    @JsonProperty("senha")
    var password: String,

    @JsonProperty("cargo")
    var roles: List<Role>?
)
