package br.com.tickets.orquestrator.tickets.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class EmailInUseException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message) {

}
