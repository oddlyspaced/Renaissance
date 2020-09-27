package com.oddlyspaced.renaissance.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oddlyspaced.renaissance.modal.Category

class SharedPreferenceManager(context: Context) {

    companion object {
        const val KEY_USER_CATEGORIES = "user_category"
        const val KEY_USER_LANGUAGE = "user_lang"
    }

    private val sharedPreferencesKey = "shared_preference"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
    private val editor by lazy { sharedPreferences.edit() }
    private val gson = Gson()

    // saves a list object to sharedpreferences using gson library
    fun saveUserCategories(list: ArrayList<Category>) {
        val listJson = gson.toJson(list)
        editor.putString(KEY_USER_CATEGORIES, listJson)
        editor.apply()
    }

    // loads a particular list from sharedpreference
    fun loadUserCategories(): ArrayList<Category> {
        val json = sharedPreferences.getString(KEY_USER_CATEGORIES, null)
        val type = object: TypeToken<ArrayList<Category>>(){}.type
        var list: ArrayList<Category>? = gson.fromJson(json, type)
        if (list == null) {
            list = arrayListOf()
        }
        return list
    }

    fun saveLanguage(lang: Int) {
        editor.putInt(KEY_USER_LANGUAGE, lang)
        editor.apply()
    }

    fun getLanguage(): Int {
        return sharedPreferences.getInt(KEY_USER_LANGUAGE, 0)
    }

}