package com.oddlyspaced.renaissance.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
    fun saveUserCategories(list: ArrayList<Int>) {
        val listJson = gson.toJson(list)
        editor.putString(KEY_USER_CATEGORIES, listJson)
        editor.apply()
    }

    // loads a particular list from sharedpreference
    fun loadUserCategories(): ArrayList<Int> {
        val json = sharedPreferences.getString(KEY_USER_CATEGORIES, null)
        val type = object: TypeToken<ArrayList<Int>>(){}.type
        var list: ArrayList<Int>? = gson.fromJson(json, type)
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