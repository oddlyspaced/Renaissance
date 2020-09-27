package com.oddlyspaced.renaissance.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.LanguageAdapter
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.Language
import com.oddlyspaced.renaissance.modal.LanguageOption
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_language_select.*
import retrofit2.Call
import retrofit2.Response
import java.util.*

class LanguageSelectActivity : AppCompatActivity() {

    private val tag = "LanguageSelectActivity"

    private val list = arrayListOf(arrayOf<LanguageOption>())
    private lateinit var adapter: LanguageAdapter
    private val sharedPreferenceManager by lazy { SharedPreferenceManager(applicationContext) }
    private var isLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_select)

        setupUI()
        fetchLanguages()
    }

    private fun setupUI() {
        rvLanguage.setHasFixedSize(true)
        rvLanguage.layoutManager = LinearLayoutManager(applicationContext)
        adapter = LanguageAdapter(list)
        rvLanguage.adapter = adapter
        cvFinish.setOnClickListener {
            if (!isLoaded) {
                Toast.makeText(applicationContext, "Please wait for loading to finish!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var langCode = -1
            list.forEach {
                if (it[0].isSelected) {
                    langCode = (it[0].language.id)
                }
                if (it.size > 1) {
                    if (it[1].isSelected) {
                        langCode = (it[1].language.id)
                    }
                }
            }
            if (langCode == -1) {
                Toast.makeText(applicationContext, "Please select your preferred language!", Toast.LENGTH_LONG).show()
            }
            else {
                sharedPreferenceManager.saveLanguage(langCode)
                startNext()
            }
        }
    }

    private fun fetchLanguages() {
        val client = ApiClient.getApiClient()
        val apiInterface = client.create(ApiInterface::class.java)
        val call: Call<List<Language>> = apiInterface.languageAll()
        call.enqueue(object : retrofit2.Callback<List<Language>> {
            override fun onResponse(call: Call<List<Language>>, response: Response<List<Language>>) {
                if (response.isSuccessful) {
                    val langs = response.body()!!
                    list.clear()
                    for (i in langs.indices step 2) {
                        if (i + 1 != langs.size) {
                            list.add(arrayOf(LanguageOption(getStylisedLanguageTitle(langs[i])), LanguageOption(getStylisedLanguageTitle(langs[i+1]))))
                        }
                        else {
                            list.add(arrayOf(LanguageOption(getStylisedLanguageTitle(langs[i]))))
                        }
                    }
                    adapter.notifyDataSetChanged()
                    pbLoading.isVisible = false
                    rvLanguage.isVisible = true
                    isLoaded = true
                }
            }

            override fun onFailure(call: Call<List<Language>>, t: Throwable) {
                Log.e(tag, t.message.toString())
                Toast.makeText(applicationContext, "Unable to load", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getStylisedLanguageTitle(lang: Language): Language {
        val ll = lang.language.toLowerCase(Locale.ROOT)
        lang.language =  when (ll) {
            "english" -> {
                "English"
            }
            "hindi" -> {
                "हिन्दी"
            }
            else -> lang.language
        }
        return lang
    }

    private fun startNext() {
        startActivity(Intent(applicationContext, CategorySelectActivity::class.java))
        finish()
    }

}