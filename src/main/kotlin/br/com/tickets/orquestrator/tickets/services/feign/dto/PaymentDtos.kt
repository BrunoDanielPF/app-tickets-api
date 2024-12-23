package br.com.tickets.orquestrator.tickets.services.feign.dto

data class PaymentEventRequest(
    val customerRequest: CustomerRequest,
    val value: Int,
    val description: String
)