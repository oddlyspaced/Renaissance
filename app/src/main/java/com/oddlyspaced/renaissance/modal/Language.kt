package com.oddlyspaced.renaissance.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Language(
    @SerializedName("id") @Expose
    var id: Int,
    @SerializedName("language")
    @Expose
    var language: String,
    @SerializedName("image")
    @Expose
    val image: String,
    val isSelected: Boolean = false
)