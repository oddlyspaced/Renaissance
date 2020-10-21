package com.oddlyspaced.renaissance.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oddlyspaced.renaissance.modal.Post

class SavedPostDatabaseManager(context: Context) {

    private companion object {
        const val KEY_SAVED = "saved"
    }

    private val sharedPreferencesKey = "saved_post"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
    private val editor by lazy { sharedPreferences.edit() }
    private val gson = Gson()

    private val savedPosts by lazy {
        val json = sharedPreferences.getString(KEY_SAVED, null)
        val type = object : TypeToken<ArrayList<Post>>(){}.type
        gson.fromJson(json, type)?: arrayListOf<Post>()
    }

    fun addSavedPost(post: Post) {
        savedPosts.add(post)
        editor.putString(KEY_SAVED, gson.toJson(savedPosts))
        editor.apply()
    }

    fun deleteSavedPost(post: Post) {
        if (savedPosts.contains(post)) {
            savedPosts.remove(post)
        }
        editor.putString(KEY_SAVED, gson.toJson(savedPosts))
        editor.apply()
    }

    fun checkPostSaved(post: Post): Boolean {
        return savedPosts.contains(post)
    }

    fun getSavedPostsList(): ArrayList<Post> {
        return savedPosts
    }

}