package br.com.tickets.orquestrator.tickets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class OrchestratorTicketsApplication

fun main(args: Array<String>) {
	runApplication<OrchestratorTicketsApplication>(*args)
}