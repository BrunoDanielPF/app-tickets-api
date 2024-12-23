package br.com.tickets.orquestrator.tickets.services.feign.dto

import br.com.tickets.orquestrator.tickets.domain.entity.user.Role
import br.com.tickets.orquestrator.tickets.domain.entity.user.User
import com.google.gson.annotations.SerializedName

data class ResponseCustomer(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("notificationDisabled")
	val notificationDisabled: Boolean? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("postalCode")
	val postalCode: String? = null,

	@field:SerializedName("externalReference")
	val externalReference: String? = null,

	@field:SerializedName("foreignCustomer")
	val foreignCustomer: Boolean? = null,

	@field:SerializedName("dateCreated")
	val dateCreated: String? = null,

	@field:SerializedName("deleted")
	val deleted: Boolean? = null,

	@field:SerializedName("mobilePhone")
	val mobilePhone: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("cityName")
	val cityName: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("observations")
	val observations: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("addressNumber")
	val addressNumber: String? = null,

	@field:SerializedName("additionalEmails")
	val additionalEmails: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("cpfCnpj")
	val cpfCnpj: String? = null,

	@field:SerializedName("complement")
	val complement: String? = null,

	@field:SerializedName("personType")
	val personType: String? = null,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("object")
	val objectCustomer: String? = null
) {
	fun createUser(): User {
		return User(
            email = email,
            name = name!!,
            password = TODO(),
            emailValidated = TODO(),
            emailCode = TODO(),
            emailCodeExpiration = TODO(),
            isOrganizer = false,
            roles = mutableListOf(Role(1, "USER"))
        )
	}
}