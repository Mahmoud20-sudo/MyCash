package com.codeIn.stock.inventory.data.model.response


import com.google.gson.annotations.SerializedName

data class InventoryDTO(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: Any?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("allQuantity")
        val allQuantity: String?,
        @SerializedName("quantity")
        val quantity: String?,
        @SerializedName("damageQuantity")
        val damageQuantity: String?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("product")
        val product: Product?
    )
}