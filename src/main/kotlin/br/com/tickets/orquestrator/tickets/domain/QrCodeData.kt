package br.com.tickets.orquestrator.tickets.domain

data class QRCodeData(
    val userName: String,
    val eventName: String,
    val enabled: Boolean,
    val dateValid: String,
    val couponCode: String
)
