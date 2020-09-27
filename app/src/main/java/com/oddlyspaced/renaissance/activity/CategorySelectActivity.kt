package com.oddlyspaced.renaissance.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.CategoryAdapter
import com.oddlyspaced.renaissance.api.ApiClient
import com.oddlyspaced.renaissance.api.ApiInterface
import com.oddlyspaced.renaissance.modal.Category
import com.oddlyspaced.renaissance.modal.CategoryOption
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_category_select.*
import retrofit2.Call
import retrofit2.Response

class CategorySelectActivity : AppCompatActivity() {

    private val categoryLimitMin = 1

    private val list = arrayListOf(arrayOf<CategoryOption>())
    private lateinit var adapter: CategoryAdapter
    private val sharedPreferenceManager by lazy { SharedPreferenceManager(applicationContext) }
    private var isLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_select)

        setupUI()
        fetchCategory()
    }

    private fun setupUI() {
        rvCategory.setHasFixedSize(true)
        rvCategory.layoutManager = LinearLayoutManager(applicationContext)
        adapter = CategoryAdapter(list)
        rvCategory.adapter = adapter
        cvFinish.setOnClickListener {
            if (!isLoaded) {
                Toast.makeText(applicationContext, "Please wait for loading to finish!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val categoriesToSave = arrayListOf<Category>()
            list.forEach {
                if (it[0].isSelected) {
                    categoriesToSave.add(it[0].category)
                }
                if (it.size > 1) {
                    if (it[1].isSelected) {
                        categoriesToSave.add(it[1].category)
                    }
                }
            }
            if (categoriesToSave.size < categoryLimitMin ) {
                Toast.makeText(applicationContext, "Please select atleast $categoryLimitMin topics!", Toast.LENGTH_LONG).show()
            }
            else {
                sharedPreferenceManager.saveUserCategories(categoriesToSave)
                startActivity(Intent(applicationContext, UserFeedActivity::class.java))
                finish()
            }
        }
    }

    private fun fetchCategory() {
        val language = sharedPreferenceManager.getLanguage().toString()
        val client = ApiClient.getApiClient()
        val apiInterface = client.create(ApiInterface::class.java)
        apiInterface.allCategoriesByLanguage(language).enqueue(object : retrofit2.Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val cats = response.body()!!
                    list.clear()
                    for (i in cats.indices step 2) {
                        if (i + 1 != cats.size) {
                            list.add(arrayOf(CategoryOption(cats[i]), CategoryOption(cats[i+1])))
                        }
                        else {
                            list.add(arrayOf(CategoryOption(cats[i])))
                        }
                    }
                    adapter.notifyDataSetChanged()
                    pbLoading.isVisible = false
                    rvCategory.isVisible = true
                    isLoaded = true
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(applicationContext, "Unable to load", Toast.LENGTH_LONG).show()
            }
        })
    }
}