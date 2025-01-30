package com.codeIn.myCash.features.sales.data.branch.model.response


import com.codeIn.common.domain.model.AutoCompleteModelSearch
import com.google.gson.annotations.SerializedName

data class BranchesDTO(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: Any?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("data")
        val `data`: List<Data>?,
        @SerializedName("pagination")
        val pagination: Pagination?
    ) {
        data class Data(
            @SerializedName("id")
            val id: Int,
            val invoiceNumber: Int,
            @SerializedName("name")
            val name: String?,
            @SerializedName("address")
            val address: String?,
            @SerializedName("phone")
            val phone: String?,
            @SerializedName("country")
            val country: Any?,
            @SerializedName("city")
            val city: String?,
            @SerializedName("additional_info")
            val additionalInfo: String?,
            @SerializedName("isMain")
            val isMain: Int?,
            @SerializedName("status")
            val status: Int?,
            @SerializedName("employee")
            val employee: Employee?,
            @SerializedName("is_active")
            var isActive: Boolean?  = false,
        ) {
            object ModelMapper {
                fun from(form: Data) = AutoCompleteModelSearch(form.id!!, form.name)
            }

            data class Employee(
                @SerializedName("name")
                val name: Any?,
                @SerializedName("id")
                val id: Int?
            )
        }

        data class Pagination(
            @SerializedName("total")
            val total: Int?,
            @SerializedName("count")
            val count: Int?,
            @SerializedName("per_page")
            val perPage: Int?,
            @SerializedName("current_page")
            val currentPage: Int?,
            @SerializedName("total_pages")
            val totalPages: Int?,
            @SerializedName("is_pagination")
            val isPagination: Boolean?
        )
    }
}