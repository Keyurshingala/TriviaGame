package com.gvapps.triviagame.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String
)