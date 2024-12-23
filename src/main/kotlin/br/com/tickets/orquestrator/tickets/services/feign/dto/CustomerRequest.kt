package br.com.tickets.orquestrator.tickets.services.feign.dto

import com.google.gson.annotations.SerializedName
import jakarta.validation.constraints.NotNull

data class CustomerRequest(

	@field:SerializedName("mobilePhone")
	val mobilePhone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("cpf")
	val cpfCnpj: String? = null,

	@field:SerializedName("email")
	@field:NotNull(message = "Email nao pode ser nulo")
	val email: String
)
