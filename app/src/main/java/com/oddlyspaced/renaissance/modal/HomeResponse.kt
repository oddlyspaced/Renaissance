package com.oddlyspaced.renaissance.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("slides")
    @Expose
    var slides: List<Slide?>,
    @SerializedName("categories")
    @Expose
    val categories: List<Category>,
    @SerializedName("questions")
    @Expose
    val questions: List<Question>,
    @SerializedName("posts")
    @Expose
    val posts: List<Post>
)