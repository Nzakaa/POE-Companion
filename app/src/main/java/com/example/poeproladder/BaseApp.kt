package com.example.poeproladder

import android.app.Application
import android.content.Context
import com.example.poeproladder.dagger.components.ApplicationComponent
import com.example.poeproladder.dagger.components.DaggerApplicationComponent
import com.example.poeproladder.session.SessionService
import com.example.poeproladder.session.SessionServiceImpl

class BaseApp : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent.builder().build()
        applicationComponent?.inject(this)
    }

    companion object {
        private var applicationComponent: ApplicationComponent? = null
        private var instance: BaseApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        fun getAppComponent(): ApplicationComponent {
            return applicationComponent!!
        }
    }
}