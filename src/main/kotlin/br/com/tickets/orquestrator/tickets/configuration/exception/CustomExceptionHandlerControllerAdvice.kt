package br.com.tickets.orquestrator.tickets.configuration.exception

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CustomExceptionHandlerControllerAdvice : ResponseEntityExceptionHandler() {
}