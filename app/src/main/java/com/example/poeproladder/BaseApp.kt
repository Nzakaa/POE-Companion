package com.example.poeproladder

import android.app.Application
import android.content.Context

class BaseApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: BaseApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}