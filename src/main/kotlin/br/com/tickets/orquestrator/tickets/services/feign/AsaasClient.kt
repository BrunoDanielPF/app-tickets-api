package br.com.tickets.orquestrator.tickets.services.feign

import br.com.tickets.orquestrator.tickets.services.feign.dto.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "asaas", url = "\${url.asaas}")
interface AsaasClient {

    @PostMapping("/v3/customers")
    fun createCustomer(customerRequest: CustomerRequest): ResponseCustomer

    @GetMapping("/v3/customers")
    fun getCustomer(@RequestParam(name = "email") email: String): EspecificationResponse<MutableList<ResponseCustomer>>

    @PostMapping("/v3/pix/qrCodes/static")
    fun createPix(@RequestBody pixInformationRequest: PixInformationRequest): PixInformationResponse
}