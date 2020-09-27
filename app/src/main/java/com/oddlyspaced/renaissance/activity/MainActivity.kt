package com.oddlyspaced.renaissance.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PostAdapter
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.HomeResponse
import com.oddlyspaced.renaissance.modal.Post
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val posts = arrayListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGlobalNews.layoutManager = LinearLayoutManager(applicationContext)
        rvGlobalNews.setHasFixedSize(true)

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
                    response.body()?.posts?.forEach {
                        posts.add(it)
                    }
                    rvGlobalNews.adapter = PostAdapter(posts)
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to fetch posts", Toast.LENGTH_SHORT).show()
            }

        })
    }
}