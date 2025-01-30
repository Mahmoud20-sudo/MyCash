package com.codeIn.stock.inventory.data.model.response


import com.google.gson.annotations.SerializedName

data class SalesReportDTO(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: Any?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("data")
        val `data`: List<Data?>?,
        @SerializedName("pagination")
        val pagination: Pagination?
    ) {
        data class Data(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("totalSalesPrice")
            val totalSalesPrice: String?,
            @SerializedName("totalCashPrice")
            val totalCashPrice: String?,
            @SerializedName("totalVisaPrice")
            val totalVisaPrice: String?,
            @SerializedName("totalRemainingPrice")
            val totalRemainingPrice: String?,
            @SerializedName("totalReturnPrice")
            val totalReturnPrice: String?,
            @SerializedName("dateType")
            val dateType: Int?
        )

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