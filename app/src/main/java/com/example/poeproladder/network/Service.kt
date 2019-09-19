package com.example.poeproladder.network

import io.reactivex.Single
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
