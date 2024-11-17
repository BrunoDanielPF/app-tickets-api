package br.com.tickets.orquestrator.tickets.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InvalidCodeException(message: String): ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, message) {
}