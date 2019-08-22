package com.example.poeproladder.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.poeproladder.R

object BuildConfig {
    const val CHARACTERWINDOWURL = "https://www.pathofexile.com"
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

object LinkPosition {
    const val START = R.drawable.skill_gem_link_start
    const val MIDDLE = R.drawable.skill_gem_link_mid
    const val END = R.drawable.skill_gem_link_end
    const val SINGLE = R.drawable.skill_gem_link_single
}