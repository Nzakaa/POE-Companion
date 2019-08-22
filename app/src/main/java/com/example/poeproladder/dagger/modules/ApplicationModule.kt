package com.example.poeproladder.dagger.modules

import android.content.Context
import com.example.poeproladder.BaseApp
import com.example.poeproladder.session.SessionService
import com.example.poeproladder.session.SessionServiceImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
object ApplicationModule {

    @Provides @JvmStatic @Reusable
    fun provideContext() : Context {
        return BaseApp.applicationContext()
    }

    @Provides @JvmStatic @Singleton
    fun provideSession(sessionService: SessionServiceImpl) : SessionService {
        return sessionService
    }
}
