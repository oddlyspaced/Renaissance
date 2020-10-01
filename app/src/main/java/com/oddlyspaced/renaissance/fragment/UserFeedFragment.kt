package com.oddlyspaced.renaissance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PostAdapter
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.Category
import com.oddlyspaced.renaissance.modal.Post
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_user_feed.*
import retrofit2.Call
import retrofit2.Response

class UserFeedFragment: Fragment() {

    private val TAG = "UserFeedFragment"

    private val sharedPreferenceManager by lazy { SharedPreferenceManager(context!!) }
    private lateinit var language: String
    private lateinit var categories: ArrayList<Category>

    private val client = ApiClient.getApiClient()
    private val apiInterface = client.create(ApiInterface::class.java)

    private val posts = arrayListOf<Post>()
    private lateinit var postAdapter: PostAdapter
    private val pages = arrayListOf<Int>()

    private val stagingPosts = arrayListOf<Post>()

    private var isLoading = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        language = sharedPreferenceManager.getLanguage().toString()
        categories = sharedPreferenceManager.loadUserCategories()
        categories.forEach { _ ->
            pages.add(0)
        }

        rvUserFeed.layoutManager = LinearLayoutManager(context!!)
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
                Toast.makeText(context, "Failed to load posts!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun addPostsToAdapter() {
        val c = pages[0]
        pages.forEach {
            if (c != it) {
                return
            }
        }
        stagingPosts.shuffle()
        stagingPosts.sortBy {
            it.created
        }
        // TODO: Check for posts with same title and remove them
        posts.addAll(stagingPosts)
        stagingPosts.clear()
        postAdapter.notifyDataSetChanged()
        isLoading = false
        pbLoading.isVisible = false
        rvUserFeed.isVisible = true
        cvLoading.isVisible = false
    }

}