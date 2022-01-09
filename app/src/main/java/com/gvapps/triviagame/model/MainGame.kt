package com.gvapps.triviagame.model

import com.google.gson.annotations.SerializedName

data class MainGame(
    @SerializedName("id") var id: Int,
    @SerializedName("answer") var answer: String,
    @SerializedName("question") var question: String,
    @SerializedName("category") var category: Category
 )