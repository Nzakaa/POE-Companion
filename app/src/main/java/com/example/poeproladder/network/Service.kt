package com.example.poeproladder.network

import com.example.poeproladder.util.BuildConfig.CHARACTERWINDOWURL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface CharacterWindowApi {
    @GET("/character-window/get-characters")
    fun getAccountInfo(@Query("accountName") accountName: String?): Single<List<CharacterWindowCharacterJson>>

    @GET("/character-window/get-items")
    fun getCharacterInfo(
        @Query("accountName") account: String?,
        @Query("character") characterName: String?
    ): Single<CharacterWindowItemsJson>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {

    val characterApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(CHARACTERWINDOWURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return@lazy retrofit.create(CharacterWindowApi::class.java)
    }
}