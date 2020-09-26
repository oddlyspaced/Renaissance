package com.oddlyspaced.renaissance.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("id")
    @Expose
    private var id: Int,
    @SerializedName("question")
    @Expose
    val question: String,
    @SerializedName("multichoice")
    @Expose
    val multichoice: Boolean,
    @SerializedName("close")
    @Expose
    val close: Boolean,
    @SerializedName("choices")
    @Expose
    val choices: List<Choice>,
    @SerializedName("featured")
    @Expose
    val featured: Boolean
)