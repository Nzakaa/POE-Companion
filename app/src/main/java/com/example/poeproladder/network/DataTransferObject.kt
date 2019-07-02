package com.example.poeproladder.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NetworkLadderContainerJson(
    @Json(name = "total") val total: Int,
    @Json(name = "cached_since") val cachedSince: String,
    @Json(name = "entries") val profiles: List<PoeProfileJson>
)

@JsonClass(generateAdapter = true)
data class PoeProfileJson(
    @Json(name = "rank") val rank: Int,
    @Json(name = "dead") val dead: Boolean,
    @Json(name = "online") val online: Boolean,
    @Json(name = "character") val character: PoeCharacterJson,
    @Json(name = "account") val account: PoeAccountJson
)

@JsonClass(generateAdapter = true)
data class PoeCharacterJson(
    @Json(name = "name") val name: String,
    @Json(name = "level") val level: Int,
    @Json(name = "class") val poeClass: String,
    @Json(name = "id") val characterId: String,
    @Json(name = "experience") val experience: Long,
    @Json(name = "depth") val depth: PoeDepthJson = PoeDepthJson(-1,-1)
)

@JsonClass(generateAdapter = true)
data class PoeAccountJson(
    @Json(name = "name") val name: String,
    @Json(name = "realm") val realm: String,
    @Json(name = "challenges") val challenges: PoeAccountChallengesJson,
    @Json(name = "twitch") val twitch: PoeAccountTwitchJson
)

@JsonClass(generateAdapter = true)
data class PoeAccountChallengesJson(
    @Json(name = "total") val total: Int
)

@JsonClass(generateAdapter = true)
data class PoeAccountTwitchJson(
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class PoeDepthJson(
    @Json(name = "default") val default: Int = -1,
    @Json(name = "solo") val solo: Int = -1
)





