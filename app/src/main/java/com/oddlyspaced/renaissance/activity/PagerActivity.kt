package com.oddlyspaced.renaissance.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PagerAdapter
import com.oddlyspaced.renaissance.fragment.EmptyFragment
import com.oddlyspaced.renaissance.fragment.UserFeedFragment
import kotlinx.android.synthetic.main.activity_pager.*

class PagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        val list = arrayListOf<Fragment>(UserFeedFragment(), EmptyFragment())
        val adapter = PagerAdapter(this, list)
        viewPagerMain.adapter = adapter
    }
}