package com.oddlyspaced.renaissance.api

import com.oddlyspaced.renaissance.global.Global
import com.oddlyspaced.renaissance.modal.HomeResponse
import org.intellij.lang.annotations.Language
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

}