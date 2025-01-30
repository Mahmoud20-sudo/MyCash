package com.codeIn.common.data

import com.google.gson.annotations.SerializedName

data class Pagination (

    @SerializedName("total"         ) var total        : Int?     = null,
    @SerializedName("count"         ) var count        : Int?     = null,
    @SerializedName("per_page"      ) var perPage      : Int?     = null,
    @SerializedName("current_page"  ) var currentPage  : Int?     = null,
    @SerializedName("total_pages"   ) var totalPages   : Int?     = null,
    @SerializedName("is_pagination" ) var isPagination : Boolean? = null

)
