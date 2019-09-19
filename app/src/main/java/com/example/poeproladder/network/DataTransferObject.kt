package com.example.poeproladder.network

import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.ItemDb
import com.squareup.moshi.*

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