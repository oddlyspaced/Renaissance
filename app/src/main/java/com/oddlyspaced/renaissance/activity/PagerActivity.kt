package com.oddlyspaced.renaissance.activity

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.adapter.PagerAdapter
import com.oddlyspaced.renaissance.fragment.*
import kotlinx.android.synthetic.main.activity_pager.*
import kotlinx.android.synthetic.main.layout_bottom_bar.*
import kotlinx.android.synthetic.main.layout_bottom_bar.view.*

class PagerActivity : AppCompatActivity() {

    private lateinit var listCardBar: ArrayList<CardView>
    private lateinit var listIconBar: ArrayList<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)
        listCardBar = arrayListOf(include.cvHome, include.cvGlobal, include.cvQuickBits, include.cvSaved)
        listIconBar = arrayListOf(include.imgHome, include.imgGlobal, include.imgQuickBits, include.imgSaved)

        val list = arrayListOf(UserFeedFragment(), GlobalFeedFragment(), QuickBitsFeedFragment()/*, SavedFeedFragment()*/)
        val titles = arrayOf("For You", "Global", "Quick Bits", "Saved")
        val adapter = PagerAdapter(this, list)
        viewPagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                txPageTitle.text = titles[position]
                activateBottomBarItem(position)
            }
        })
        viewPagerMain.adapter = adapter
        setupBottomBar()
    }

    private fun setupBottomBar() {
        for (i in 0 until listCardBar.size) {
            listCardBar[i].setOnClickListener {
                activateBottomBarItem(i);
            }
        }
        activateBottomBarItem(0)
    }

    private fun resetBottomBarColor() {
        for (i in 0 until listCardBar.size) {
            listCardBar[i].setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            listIconBar[i].setTint(R.color.colorTextLight)
        }
    }

    private fun activateBottomBarItem(index: Int) {
        viewPagerMain.setCurrentItem(index, true)
        resetBottomBarColor()
        listCardBar[index].setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorCardLight))
        listIconBar[index].setTint(R.color.colorText)
    }

    private fun ImageView.setTint(@ColorRes colorRes: Int) {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
    }
}