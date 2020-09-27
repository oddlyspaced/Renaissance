package com.oddlyspaced.renaissance.activity

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.oddlyspaced.renaissance.modal.HomeResponse
import kotlinx.android.synthetic.main.activity_category_select.*
import retrofit2.Call
import retrofit2.Response

class CategorySelectActivity : AppCompatActivity() {

    private val list = arrayListOf(arrayOf<CategoryOption>())
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_select)

        rvCategory.setHasFixedSize(true)
        rvCategory.layoutManager = LinearLayoutManager(applicationContext)
        adapter = CategoryAdapter(list)
        rvCategory.adapter = adapter

        fetchCategory()
    }

    private fun fetchCategory() {
        // 5 is english, 6 is hindi
        val language = "5"
        val client = ApiClient.getApiClient()
        val apiInterface = client.create(ApiInterface::class.java)
        apiInterface.allCategoriesByLanguage(language).enqueue(object : retrofit2.Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val cats = response.body()!!
                    Log.e("SIZE", cats.size.toString())
                    list.clear()
                    for (i in cats.indices step 2) {
                        Log.e("AAA", i.toString())
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
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(applicationContext, "Unable to load", Toast.LENGTH_LONG).show()
            }
        })
    }
}