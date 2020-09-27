package com.oddlyspaced.renaissance.api

import com.oddlyspaced.renaissance.global.Global
import com.oddlyspaced.renaissance.modal.Category
import com.oddlyspaced.renaissance.modal.HomeResponse
import com.oddlyspaced.renaissance.modal.Language
import com.oddlyspaced.renaissance.modal.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    /*
     @GET("home/latest/{language}/" + Global.SECURE_KEY + "/" + Global.ITEM_PURCHASE_CODE + "/")
    Call<HomeResponse> home(@Path("language") String language);
     */

    @GET("home/latest/{language}/" + Global.SECURE_KEY + "/" + Global.ITEM_PURCHASE_CODE + "/")
    fun fetchLatestHome(@Path("language") language: String): Call<HomeResponse>

    @GET("post/all/{page}/id/{language}/" + Global.SECURE_KEY + "/" + Global.ITEM_PURCHASE_CODE + "/")
    fun postsByPage(@Path("page") page: Int, @Path("language") language: String): Call<List<Post>>

    @GET("category/all/{language}/" + Global.SECURE_KEY + "/" + Global.ITEM_PURCHASE_CODE + "/")
    fun allCategoriesByLanguage(@Path("language") language: String): Call<List<Category>>

    @GET("language/all/" + Global.SECURE_KEY + "/" + Global.ITEM_PURCHASE_CODE + "/")
    fun languageAll(): Call<List<Language>>

}