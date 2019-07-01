package com.example.poeproladder.network

import com.example.poeproladder.domain.PoeProfile
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NetworkLadderContainer(val profiles: List<PoeProfile>)