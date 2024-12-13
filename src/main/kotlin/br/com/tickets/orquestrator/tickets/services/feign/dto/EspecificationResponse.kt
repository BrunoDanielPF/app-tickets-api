package br.com.tickets.orquestrator.tickets.services.feign.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
class EspecificationResponse<T>(
    @field:SerializedName("object")
    val objectCustomer: String?,
    val hasMore: Boolean,
    val totalCount: Int,
    val limit: Int,
    val offset: Int,
    val data: T
) {
}