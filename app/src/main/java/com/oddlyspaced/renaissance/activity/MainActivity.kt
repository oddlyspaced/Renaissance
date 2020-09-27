package com.oddlyspaced.renaissance.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private lateinit var postAdapter: PostAdapter
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGlobalNews.layoutManager = LinearLayoutManager(applicationContext)
        rvGlobalNews.setHasFixedSize(true)
        postAdapter = PostAdapter(posts, PostAdapter.TYPE_SINGLE)
        rvGlobalNews.adapter = postAdapter

        rvGlobalNews.setOnScrollChangeListener { _, _, _, _, _ ->
            val current = (rvGlobalNews.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (current > posts.size - 5) {
                page++
                fetchHome()
            }
        }
        startActivity(Intent(applicationContext, LanguageSelectActivity::class.java))
        finish()
        //fetchHome()
    }

    private fun fetchHome() {
        // 5 is english, 6 is hindi
        val language = "5"
        val client = ApiClient.getApiClient()
        val apiInterface = client.create(ApiInterface::class.java)
        apiInterface.postsByPage(page, language).enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        posts.add(it)
                    }

                    postAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to load posts!", Toast.LENGTH_LONG).show()
            }
        })
    }
}