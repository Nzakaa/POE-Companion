package com.example.poeproladder.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object BuildConfig {
    const val CHARACTERWINDOWURL = "https://www.pathofexile.com"
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}