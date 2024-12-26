package br.com.tickets.orquestrator.tickets.controller.events.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.math.BigDecimal
import java.time.LocalDateTime

data class EventRequest(
    @JsonProperty("nome")
    @NotNull
    val name: String,

    @JsonProperty("localização")
    @NotNull
    val location: String,

    @JsonProperty("data")
    @field:Pattern(
        regexp = "^\\d{2}-\\d{2}-\\d{4}$",
        message = "A data deve estar no formato dd-MM-yyyy (exemplo: 25-12-2025)"
    )
    val date: String,

    @JsonProperty("descrição")
    @NotNull
    val description: String,

    @JsonProperty("quantidade_de_tipos_de_ingressos")
    val totalTickets: Int? = 1
)

data class TicketEventRequest(
    @JsonProperty("eVIP")
    val isVIP: Boolean,

    @JsonProperty("preço")
    val price: BigDecimal,

    @JsonProperty("dia")
    val day: Int,

    @JsonProperty("mês")
    val month: Int,

    @JsonProperty("descrição")
    val description: String,

    @JsonProperty("quantidade_de_ingressos")
    @NotNull
    val totalTickets: Int
)

data class TicketRequest(
    @JsonProperty("valor")
    val price: BigDecimal,
    @JsonProperty("email_comprador")
    val buyerEmail: String,
    @JsonProperty("cpf_comprador")
    val buyerCPF: String,
    @JsonProperty("nome_comprador")
    val buyerName: String,
    @JsonProperty("quantidade")
    val quantity: Int
)