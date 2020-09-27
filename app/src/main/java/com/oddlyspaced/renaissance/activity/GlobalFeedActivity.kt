package com.oddlyspaced.renaissance.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PostAdapter
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.Post
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_global_feed.*
import retrofit2.Call
import retrofit2.Response

class GlobalFeedActivity : AppCompatActivity() {

    private val tag = "GlobalFeedActivity"

    private val sharedPreferenceManager by lazy { SharedPreferenceManager(applicationContext) }
    private lateinit var language: String

    private val client = ApiClient.getApiClient()
    private val apiInterface = client.create(ApiInterface::class.java)

    private val posts = arrayListOf<Post>()
    private lateinit var postAdapter: PostAdapter
    private var page = 0

    private var isLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_feed)

        language = sharedPreferenceManager.getLanguage().toString()

        rvGlobal.layoutManager = LinearLayoutManager(applicationContext)
        rvGlobal.setHasFixedSize(true)
        postAdapter = PostAdapter(posts, PostAdapter.TYPE_SINGLE)
        rvGlobal.adapter = postAdapter

        rvGlobal.setOnScrollChangeListener { _, _, _, _, _ ->
            val current = (rvGlobal.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (current > posts.size - 10) {
                if (!isLoading) {
                    isLoading = true
                    fetchHome()
                }
            }
            if (current > posts.size - 3) {
                cvLoading.isVisible = true
            }
        }

        fetchHome()
    }

    private fun fetchHome() {
        // 5 is english, 6 is hindi
        val client = ApiClient.getApiClient()
        val apiInterface = client.create(ApiInterface::class.java)
        apiInterface.postsByPage(page, language).enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        posts.add(it)
                    }
                    page++
                    postAdapter.notifyDataSetChanged()
                    pbLoading.isVisible = false
                    rvGlobal.isVisible = true
                    cvLoading.isVisible = false
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to load posts!", Toast.LENGTH_LONG).show()
            }
        })
    }

}