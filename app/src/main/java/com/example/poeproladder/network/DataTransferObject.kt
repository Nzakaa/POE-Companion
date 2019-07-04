package com.example.poeproladder.network

import com.example.poeproladder.domain.ItemSocket
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Helper objects to parse ladder
@JsonClass(generateAdapter = true)
class NetworkLadderContainerJson(
    val total: Int,
    @Json(name = "cached_since") val cachedSince: String,
    @Json(name = "entries") val profiles: List<PoeLadderProfileJson>
)

@JsonClass(generateAdapter = true)
class PoeLadderProfileJson(
    val rank: Int,
    val dead: Boolean,
    val online: Boolean,
    val character: PoeLadderCharacterJson,
    val account: PoeLadderAccountJson
)

@JsonClass(generateAdapter = true)
class PoeLadderCharacterJson(
    val name: String,
    val level: Int,
    @Json(name = "class") val poeClass: String,
    @Json(name = "id") val characterId: String,
    val experience: Long,
    val depth: PoeLadderDepthJson = PoeLadderDepthJson(-1,-1)
)

@JsonClass(generateAdapter = true)
class PoeLadderAccountJson(
    val name: String,
    val realm: String,
    val challenges: PoeLadderAccountChallengesJson,
    val twitch: PoeLadderAccountTwitchJson
)

@JsonClass(generateAdapter = true)
class PoeLadderAccountChallengesJson(
    val total: Int
)

@JsonClass(generateAdapter = true)
class PoeLadderAccountTwitchJson(
    val name: String
)

@JsonClass(generateAdapter = true)
class PoeLadderDepthJson(
    val default: Int = -1,
    val solo: Int = -1
)

// Helper objects for account and characters parsing

@JsonClass(generateAdapter = true)
class CharacterWindowCharacterJson(
    val name: String,
    val league: String,
    @Json(name = "class") val classPoe: String,
    val level: Int)

@JsonClass(generateAdapter = true)
class CharacterWindowItemsJson(
    val items: List<ItemPoeJson>,
    val character: CharacterWindowCharacterJson
)

@JsonClass(generateAdapter = true)
class ItemPoeJson(
    @Json(name = "w") val width: Int,
    @Json(name = "h") val height: Int,
    @Json(name = "ilvl") val itemLevel: Int = 0,
    val icon: String,
    val sockets: List<ItemSocket> = ArrayList()
)












