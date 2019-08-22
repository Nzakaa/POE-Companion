package com.example.poeproladder.dagger.modules

import com.example.poeproladder.network.CharacterWindowApi
import com.example.poeproladder.util.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @JvmStatic
    @Reusable
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.CHARACTERWINDOWURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @JvmStatic
    @Reusable
    fun provideCharacterWindowApi(retrofit: Retrofit): CharacterWindowApi {
        return retrofit.create(CharacterWindowApi::class.java)
    }

//    object Network {
//        val characterApi by lazy {
//            val retrofit = Retrofit.Builder()
//                .baseUrl(BuildConfig.CHARACTERWINDOWURL)
//                .addConverterFactory(MoshiConverterFactory.create(moshi))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//            return@lazy retrofit.create(CharacterWindowApi::class.java)
//        }
}