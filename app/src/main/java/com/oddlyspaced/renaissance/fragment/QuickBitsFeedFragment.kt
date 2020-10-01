package com.oddlyspaced.renaissance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PostAdapter
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.Post
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_feed.*
import retrofit2.Call
import retrofit2.Response

class QuickBitsFeedFragment: Fragment() {

    private val TAG = "QuickBitsFeedFragment"

    private val sharedPreferenceManager by lazy { SharedPreferenceManager(context!!) }
    private lateinit var language: String

    private val client = ApiClient.getApiClient()
    private val apiInterface = client.create(ApiInterface::class.java)

    private val posts = arrayListOf<Post>()
    private lateinit var postAdapter: PostAdapter
    private var page = 0

    private var isLoading = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language = sharedPreferenceManager.getLanguage().toString()

        rvFeed.layoutManager = LinearLayoutManager(context)
        rvFeed.setHasFixedSize(true)
        postAdapter = PostAdapter(posts, PostAdapter.TYPE_QUICK)
        rvFeed.adapter = postAdapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvFeed)

        rvFeed.setOnScrollChangeListener { _, _, _, _, _ ->
            val current = (rvFeed.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
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
        apiInterface.quickBitsByPage(page, language).enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        posts.add(it)
                    }
                    page++
                    postAdapter.notifyDataSetChanged()
                    pbLoading.isVisible = false
                    rvFeed.isVisible = true
                    cvLoading.isVisible = false
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(context, "Failed to load posts!", Toast.LENGTH_LONG).show()
            }
        })
    }

}