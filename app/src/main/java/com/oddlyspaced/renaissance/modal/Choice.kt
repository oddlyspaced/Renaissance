package com.oddlyspaced.renaissance.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("choice")
    @Expose
    val choice: String,
    @SerializedName("value")
    @Expose
    val value: Int
)