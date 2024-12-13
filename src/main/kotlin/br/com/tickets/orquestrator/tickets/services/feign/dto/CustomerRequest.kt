package br.com.tickets.orquestrator.tickets.services.feign.dto

import com.google.gson.annotations.SerializedName

data class CustomerRequest(

	@field:SerializedName("mobilePhone")
	val mobilePhone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("cpf")
	val cpfCnpj: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
