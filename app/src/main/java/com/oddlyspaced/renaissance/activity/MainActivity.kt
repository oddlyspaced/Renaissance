package com.oddlyspaced.renaissance.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.HomeResponse
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchHome()
    }

    private fun fetchHome() {
        // 5 is english, 6 is hindi
        val language = "5"
        val client = ApiClient.getApiClient()
        val apiInterface = client.create(ApiInterface::class.java)
        apiInterface.fetchLatestHome(language).enqueue(object : retrofit2.Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "${response.body()?.posts?.size} ${response.body()?.slides?.size} ${response.body()?.categories?.size} ${response.body()?.questions?.size}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to fetch posts", Toast.LENGTH_SHORT).show()
            }

        })
    }
}