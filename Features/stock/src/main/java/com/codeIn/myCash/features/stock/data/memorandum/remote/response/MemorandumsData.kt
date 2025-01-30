package com.codeIn.myCash.features.stock.data.memorandum.remote.response

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class MemorandumsData(
    @SerializedName("data")
    val `data`: List<MemorandumModel>?,
    @SerializedName("pagination")
    val pagination: Pagination
)