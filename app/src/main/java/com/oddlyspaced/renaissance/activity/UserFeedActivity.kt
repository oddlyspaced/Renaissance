package com.oddlyspaced.renaissance.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PostAdapter
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.Category
import com.oddlyspaced.renaissance.modal.Post
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_user_feed.*
import kotlinx.android.synthetic.main.layout_bottom_bar.view.*
import retrofit2.Call
import retrofit2.Response

class UserFeedActivity : AppCompatActivity() {

    private val tag = "UserFeedActivity"

    private val sharedPreferenceManager by lazy { SharedPreferenceManager(applicationContext) }
    private lateinit var language: String
    private lateinit var categories: ArrayList<Category>

    private val client = ApiClient.getApiClient()
    private val apiInterface = client.create(ApiInterface::class.java)

    private val posts = arrayListOf<Post>()
    private lateinit var postAdapter: PostAdapter
    private val pages = arrayListOf<Int>()

    private val stagingPosts = arrayListOf<Post>()

    private var isLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_feed)

        language = sharedPreferenceManager.getLanguage().toString()
        categories = sharedPreferenceManager.loadUserCategories()
        categories.forEach { _ ->
            pages.add(0)
        }

        rvUserFeed.layoutManager = LinearLayoutManager(applicationContext)
        rvUserFeed.setHasFixedSize(true)
        postAdapter = PostAdapter(posts, PostAdapter.TYPE_MIXED)
        rvUserFeed.adapter = postAdapter

        categories.forEach {
            fetchFeed(it)
        }

        rvUserFeed.setOnScrollChangeListener { _, _, _, _, _ ->
            val current = (rvUserFeed.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (current > posts.size - 10) {
                if (!isLoading) {
                    isLoading = true
                    categories.forEach {
                        fetchFeed(it)
                    }
                }
            }
            if (current > posts.size - 3) {
                cvLoading.isVisible = true
            }
        }

        initBottomBar()
    }

    private fun fetchFeed(cat: Category) {
        apiInterface.postsByCategory(pages[categories.indexOf(cat)], language, cat.id).enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        it.category = cat.title
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
        stagingPosts.shuffle()
        stagingPosts.sortBy {
            it.created
        }
        posts.addAll(stagingPosts)
        stagingPosts.clear()
        postAdapter.notifyDataSetChanged()
        isLoading = false
        pbLoading.isVisible = false
        rvUserFeed.isVisible = true
        cvLoading.isVisible = false
    }

    private fun initBottomBar() {
        include.cvHome.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorCardLight))
        include.imgHome.setTint(applicationContext, R.color.colorIcon)
        include.cvGlobal.setOnClickListener {
            startActivity(Intent(applicationContext, GlobalFeedActivity::class.java))
        }
    }

    private fun ImageView.setTint(context: Context, @ColorRes colorId: Int) {
        val color = ContextCompat.getColor(context, colorId)
        val colorStateList = ColorStateList.valueOf(color)
        ImageViewCompat.setImageTintList(this, colorStateList)
    }


}