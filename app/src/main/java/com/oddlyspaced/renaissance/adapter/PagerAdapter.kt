package com.oddlyspaced.renaissance.adapter

import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.oddlyspaced.renaissance.fragment.EmptyFragment

class PagerAdapter(activity: AppCompatActivity, private val fragmentList: ArrayList<Fragment>): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}