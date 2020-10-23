package com.oddlyspaced.renaissance.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.activity.LanguageSelectActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consTopics.setOnClickListener {
            startActivity(Intent(context!!, LanguageSelectActivity::class.java))
        }
    }
}