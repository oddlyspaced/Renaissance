package com.oddlyspaced.renaissance.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PagerAdapter
import com.oddlyspaced.renaissance.fragment.EmptyFragment
import com.oddlyspaced.renaissance.fragment.UserFeedFragment
import kotlinx.android.synthetic.main.activity_pager.*
import kotlinx.android.synthetic.main.layout_bottom_bar.*

class PagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        val list = arrayListOf(UserFeedFragment(), EmptyFragment())
        val titles = arrayOf("For You", "Global")
        val adapter = PagerAdapter(this, list)
        viewPagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                txPageTitle.text = titles[position]
            }
        })
        viewPagerMain.adapter = adapter
    }
}