package com.example.poeproladder.network

import retrofit2.http.GET

interface GGGApi {
    @GET("/ladders/:id")
    fun getLadder(): List<NetworkLadderContainer>
}