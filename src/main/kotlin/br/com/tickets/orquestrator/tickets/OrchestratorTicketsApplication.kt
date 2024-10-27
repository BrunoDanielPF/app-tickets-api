package br.com.tickets.orquestrator.tickets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrchestratorTicketsApplication

fun main(args: Array<String>) {
	runApplication<OrchestratorTicketsApplication>(*args)
}
