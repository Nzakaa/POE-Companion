package com.example.poeproladder.network

import com.example.poeproladder.util.BuildConfig.GGGAPIURL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GGGApi {
    @GET("/ladders/{id}")
    fun getLadder(@Path("id") leagueId: String, @Query("limit") limit: Int?): Single<NetworkLadderContainerJson>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    val gggApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(GGGAPIURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return@lazy retrofit.create(GGGApi::class.java)
    }

}