package br.com.tickets.orquestrator.tickets.services.utils

fun generateEmailCode(): Int {
    return (1000..9999).random()
}