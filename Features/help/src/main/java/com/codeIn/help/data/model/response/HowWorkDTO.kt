package com.codeIn.help.data.model.response


import com.google.gson.annotations.SerializedName

data class HowWorkDTO(
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
            @SerializedName("title")
            val title: String?,
            @SerializedName("title_en")
            val titleEn: Any?,
            @SerializedName("title_ar")
            val titleAr: String?,
            @SerializedName("link")
            val link: String?
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