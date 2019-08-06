package com.example.poeproladder

import android.app.Application
import android.content.Context
import com.example.poeproladder.session.SessionService
import com.example.poeproladder.session.SessionServiceImpl

class BaseApp : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        session = SessionServiceImpl(applicationContext)
        super.onCreate()
    }

    companion object {
        private var instance: BaseApp? = null
        var session: SessionService? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}