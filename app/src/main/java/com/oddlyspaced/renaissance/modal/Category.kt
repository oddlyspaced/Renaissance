package com.oddlyspaced.renaissance.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("image")
    @Expose
    val image: String,
    var isSelected: Boolean = false
)