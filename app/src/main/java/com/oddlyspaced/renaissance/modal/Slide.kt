package com.oddlyspaced.renaissance.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Slide(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("post")
    @Expose
    val post: Post,
    @SerializedName("category")
    @Expose
    val category: Category,
    @SerializedName("url")
    @Expose
    val url: String
)