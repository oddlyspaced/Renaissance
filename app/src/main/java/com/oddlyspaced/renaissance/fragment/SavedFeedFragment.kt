package com.oddlyspaced.renaissance.fragment

import android.os.Bundle
import android.util.Log
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
import com.oddlyspaced.renaissance.util.SavedPostDatabaseManager
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_feed.*
import retrofit2.Call
import retrofit2.Response

class SavedFeedFragment : Fragment() {

    private val TAG = "SavedFeedActivity"

    private val sharedPreferenceManager by lazy { SharedPreferenceManager(context!!) }
    private val savedPostDatabaseManager by lazy { SavedPostDatabaseManager(context!!) }
    private lateinit var language: String

    private val client = ApiClient.getApiClient()
    private val apiInterface = client.create(ApiInterface::class.java)

    private val posts = arrayListOf<Post>()
    private lateinit var postAdapter: PostAdapter

    private var isLoading = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language = sharedPreferenceManager.getLanguage().toString()

        rvFeed.layoutManager = LinearLayoutManager(context)
        rvFeed.setHasFixedSize(true)
        postAdapter = PostAdapter(posts, PostAdapter.TYPE_SINGLE)
        rvFeed.adapter = postAdapter

        if (sharedPreferenceManager.getLayoutStyle() == PostAdapter.LAYOUT_EXPANDED) {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvFeed)
        }

        /*rvFeed.setOnScrollChangeListener { _, _, _, _, _ ->
            val current = (rvFeed.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (current > posts.size - 10) {
                if (!isLoading) {
                    isLoading = true
                    loadSaved()
                }
            }
            if (current > posts.size - 3) {
                cvLoading.isVisible = true
            }
        }*/

        loadSaved()
    }

    private fun loadSaved() {
        posts.addAll(savedPostDatabaseManager.getSavedPostsList())
        postAdapter.notifyDataSetChanged()
        pbLoading.isVisible = false
        rvFeed.isVisible = true
        cvLoading.isVisible = false
        isLoading = false
    }

}