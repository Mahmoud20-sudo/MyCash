package com.plcoding.reports.data.inventory.model

import com.google.gson.annotations.SerializedName


data class Cat (

  @SerializedName("id"            ) var id            : Int?              = null,
  @SerializedName("selected"      ) var selected      : Boolean?          = null,
  @SerializedName("parent_name"   ) var parentName    : String?           = null,
  @SerializedName("name"          ) var name          : String?           = null,
  @SerializedName("parent_id"     ) var parentId      : Int?              = null,
  @SerializedName("subCategories" ) var subCategories : ArrayList<String> = arrayListOf()

)