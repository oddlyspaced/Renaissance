package com.oddlyspaced.renaissance.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PostAdapter
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.Post
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_user_feed.*
import retrofit2.Call
import retrofit2.Response

class UserFeedActivity : AppCompatActivity() {

    private val tag = "UserFeedActivity"

    private val sharedPreferenceManager by lazy { SharedPreferenceManager(applicationContext) }
    private lateinit var language: String
    private lateinit var categories: ArrayList<Int>

    private val client = ApiClient.getApiClient()
    private val apiInterface = client.create(ApiInterface::class.java)

    private val posts = arrayListOf<Post>()
    private lateinit var postAdapter: PostAdapter
    private val pages = arrayListOf<Int>()

    private val stagingPosts = arrayListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_feed)

        language = sharedPreferenceManager.getLanguage().toString()
        categories = sharedPreferenceManager.loadUserCategories()
        categories.forEach { _ ->
            pages.add(0)
        }

        Log.e(tag, "SIZE: ${categories.size}  ${pages.size}")

        rvUserFeed.layoutManager = LinearLayoutManager(applicationContext)
        rvUserFeed.setHasFixedSize(true)
        postAdapter = PostAdapter(posts, PostAdapter.TYPE_SINGLE)
        rvUserFeed.adapter = postAdapter

        categories.forEach {
            fetchFeed(it)
        }
    }

    private fun fetchFeed() {
        apiInterface.postsByCategory(0, language, categories[0]).enqueue(object : retrofit2.Callback<List<Post>> {
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

    private fun fetchFeed(cat: Int) {
        apiInterface.postsByCategory(pages[categories.indexOf(cat)], language, cat).enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        stagingPosts.add(it)
                    }
                    pages[categories.indexOf(cat)]++
                    addPostsToAdapter()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to load posts!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun addPostsToAdapter() {
        val c = pages[0]
        pages.forEach {
            if (c != it) {
                Log.e(tag, "OK")
                return
            }
        }
        Log.e(tag, "HMMMM")

        while (stagingPosts.isNotEmpty()) {
            val randomPost = stagingPosts.random()
            posts.add(randomPost)
            stagingPosts.remove(randomPost)
        }
        postAdapter.notifyDataSetChanged()
    }


}