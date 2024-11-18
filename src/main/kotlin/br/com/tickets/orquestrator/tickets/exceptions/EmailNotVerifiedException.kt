package br.com.tickets.orquestrator.tickets.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class EmailNotVerifiedException(message: String): ResponseStatusException(HttpStatus.UNAUTHORIZED, message) {
}