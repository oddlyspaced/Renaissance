package com.oddlyspaced.renaissance.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("anchor_text")
    @Expose
    val anchor_text: String,
    @SerializedName("url")
    @Expose
    val url: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("content")
    @Expose
    val content: String,
    @SerializedName("review")
    @Expose
    val review: Boolean,
    @SerializedName("trusted")
    @Expose
    val trusted: Boolean,
    @SerializedName("comment")
    @Expose
    val comment: Boolean,
    @SerializedName("comments")
    @Expose
    val comments: Int,
    @SerializedName("views")
    @Expose
    val views: Int,
    @SerializedName("user")
    @Expose
    val user: String,
    @SerializedName("userid")
    @Expose
    val userid: Int,
    @SerializedName("userimage")
    @Expose
    val userimage: String,
    @SerializedName("thumbnail")
    @Expose
    val thumbnail: String? = null,
    @SerializedName("original")
    @Expose
    val original: String,
    @SerializedName("video")
    @Expose
    val video: String,
    @SerializedName("created")
    @Expose
    val created: String,
    @SerializedName("tags")
    @Expose
    val tags: Any?,
    @SerializedName("shares")
    @Expose
    val shares: Int,
    var category: String = "",
)