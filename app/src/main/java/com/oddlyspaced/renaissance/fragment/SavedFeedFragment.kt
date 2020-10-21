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
    private lateinit var savedPostDatabaseManager: SavedPostDatabaseManager
    private lateinit var language: String

    private lateinit var postAdapter: PostAdapter

    private var isLoading = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language = sharedPreferenceManager.getLanguage().toString()
        savedPostDatabaseManager = SavedPostDatabaseManager(context!!)

        rvFeed.layoutManager = LinearLayoutManager(context)
        rvFeed.setHasFixedSize(true)
        postAdapter = PostAdapter(savedPostDatabaseManager.savedPosts, PostAdapter.TYPE_SINGLE)
        rvFeed.adapter = postAdapter

        if (sharedPreferenceManager.getLayoutStyle() == PostAdapter.LAYOUT_EXPANDED) {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvFeed)
        }

    }

    override fun onResume() {
        super.onResume()
        savedPostDatabaseManager = SavedPostDatabaseManager(context!!)
        loadSaved()
    }

    private fun loadSaved() {
        postAdapter.notifyDataSetChanged()
        pbLoading.isVisible = false
        rvFeed.isVisible = true
        cvLoading.isVisible = false
        isLoading = false
    }

}