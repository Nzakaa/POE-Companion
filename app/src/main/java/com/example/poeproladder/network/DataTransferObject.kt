package com.example.poeproladder.network

import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.ItemDb
import com.squareup.moshi.*

// Helper objects to parse ladder
//@JsonClass(generateAdapter = true)
//class NetworkLadderContainerJson(
//    val total: Int,
//    @Json(name = "cached_since") val cachedSince: String,
//    @Json(name = "entries") val profiles: List<PoeLadderProfileJson>
//)
//
//@JsonClass(generateAdapter = true)
//class PoeLadderProfileJson(
//    val rank: Int,
//    val dead: Boolean,
//    val online: Boolean,
//    val character: PoeLadderCharacterJson,
//    val account: PoeLadderAccountJson
//)
//
//@JsonClass(generateAdapter = true)
//class PoeLadderCharacterJson(
//    val name: String,
//    val level: Int,
//    @Json(name = "class") val poeClass: String,
//    @Json(name = "id") val characterId: String,
//    val experience: Long,
//    val depth: PoeLadderDepthJson = PoeLadderDepthJson(-1, -1)
//)
//
//@JsonClass(generateAdapter = true)
//class PoeLadderAccountJson(
//    val name: String,
//    val realm: String,
//    val challenges: PoeLadderAccountChallengesJson,
//    val twitch: PoeLadderAccountTwitchJson
//)
//
//@JsonClass(generateAdapter = true)
//class PoeLadderAccountChallengesJson(
//    val total: Int
//)
//
//@JsonClass(generateAdapter = true)
//class PoeLadderAccountTwitchJson(
//    val name: String
//)
//
//@JsonClass(generateAdapter = true)
//class PoeLadderDepthJson(
//    val default: Int = -1,
//    val solo: Int = -1
//)

// Helper objects for account and characters parsing

@JsonClass(generateAdapter = true)
class CharacterWindowCharacterJson(
    val name: String,
    val league: String,
    @Json(name = "class") val classPoe: String,
    val level: Int
)

@JsonClass(generateAdapter = true)
class CharacterWindowItemsJson(
    val items: List<ItemPoeJson>
)

@JsonClass(generateAdapter = true)
class ItemPoeJson(
    @Json(name = "w") val width: Int,
    @Json(name = "h") val height: Int,
    @Json(name = "ilvl") val itemLevel: Int = 0,
    val icon: String,
    val sockets: List<ItemSocketJson> = ArrayList(),
    val name: String,
    @Json(name = "typeLine") val base: String,
    val properties: List<ItemPropertiesJson> = ArrayList(),
    val implicitMods: List<String> = ArrayList(),
    val craftedMods: List<String> = ArrayList(),
    val enchantMods: List<String> = ArrayList(),
    val explicitMods: List<String> = ArrayList(),
    val corrupted: Boolean = false,
    @Json(name = "frameType") val itemRarity: Int = -1,  //frameType 0=white, 1=magic, 2=rare, 3=unique
    val inventoryId: String,
    val socketedItems: List<SocketedItemJson> = ArrayList(),
    val x :Int
)

@JsonClass(generateAdapter = true)
class ItemSocketJson(
    val group: Int = -1,
    val attr: String = "",
    val sColour: String = ""
)

// Convert value type to proper colour 1=modified, 4=fire, 5=cold, 6=lightning

@JsonClass(generateAdapter = true)
class ItemPropertiesJson(
    val name: String,
    val values: Array<Array<Any>>
)

@JsonClass(generateAdapter = true)
class ValuesJson(
    val value: Any
)

// Ask Boris about smart way of getting level and quality values from array of gem properties
@JsonClass(generateAdapter = true)
class SocketedItemJson(
    @Json(name = "typeLine") val name: String,
    val icon: String,
    val socket: Int,
    val colour: String? = null,
    @Json(name = "category") val category: ItemCategoryJson = ItemCategoryJson(),
    @Json(name = "properties") val socketedItem: List<ItemPropertiesJson> = ArrayList()
)

@JsonClass(generateAdapter = true)
class ItemCategoryJson(
    val gems: List<String> = ArrayList()
)


/*
    Network to Domain and Database Models converter
*/


fun CharacterWindowItemsJson.asDatabaseModel(characterName: String): List<ItemDb> {
    val equippedItems = items.filter {
        it.inventoryId != "MainInventory"
    }
    return equippedItems.map {
        ItemDb(
            id = null,
            characterName = characterName,
            width = it.width,
            height = it.height,
            itemLevel = it.itemLevel,
            icon = it.icon,
            sockets = it.sockets,
            name = it.name,
            base = it.base,
            properties = it.properties,
            implicitMods = it.implicitMods,
            craftedMods = it.craftedMods,
            enchantedMods = it.enchantMods,
            itemRarity = it.itemRarity,
            inventoryId = it.inventoryId,
            socketedItems = it.socketedItems,
            x = it.x,
            corrupted = it.corrupted,
            explicitMods = it.explicitMods
        )
    }
}

fun CharacterWindowCharacterJson.asDatabaseModel(accountName: String): CharacterDb {
    return CharacterDb(
        characterName = this.name,
        league = this.league,
        classPoe = this.classPoe,
        level = this.level,
        accountName = accountName
    )

}

//fun CharacterWindowItemsJson.asDatabaseModel(): CharacterDb {
//    val equippedItems = items.filter {
//        it.inventoryId != "MainInventory"
//    }
//    val itemsEquipped = equippedItems.map {
//        ItemDb(
//            width = it.width,
//            height = it.height,
//            itemLevel = it.itemLevel,
//            icon = it.icon,
//            sockets = it.sockets,
//            characterName = it.characterName,
//            base = it.base,
//            properties = it.properties,
//            implicitMods = it.implicitMods,
//            craftedMods = it.craftedMods,
//            enchantedMods = it.enchantedMods,
//            itemRarity = it.itemRarity,
//            inventoryId = it.inventoryId,
//            socketedItems = it.socketedItems
//        )
//    }.toTypedArray()
//    val itemsDb: ItemDb = ItemDb(itemsEquipped)
//
//    val character = CharacterInformationDb(
//        characterName = this.character.characterName,
//        league = this.character.league,
//        classPoe = this.character.classPoe,
//        level = this.character.level
//    )
//
//    return CharacterDb(null, character, itemsDb)
//}


//val characterName: String,
//    val league: String,
//    val classPoe: String,
//    val level: Int

//val width: Int,
//    val height: Int,
//    val itemLevel: Int = 0,
//    val icon: String,   //TODO
//    val sockets: List<ItemSocketJson> = ArrayList(),
//    val characterName: String,
//    val base: String,
//    val properties: List<ItemPropertiesJson> = ArrayList(),
//    val implicitMods: List<String> = ArrayList(),
//    val craftedMods: List<String> = ArrayList(),
//    val enchantedMods: List<String> = ArrayList(),
//    val itemRarity: Int = -1,  //frameType 0=white, 1=magic, 2=rare, 3=unique
//    val inventoryId: String,
//    val socketedItems: List<SocketedItemJson> = ArrayList()











