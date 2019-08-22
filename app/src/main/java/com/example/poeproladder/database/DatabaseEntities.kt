package com.example.poeproladder.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.poeproladder.domain.SkillGemsLinks
import com.example.poeproladder.network.ItemPropertiesJson
import com.example.poeproladder.network.ItemSocketJson
import com.example.poeproladder.network.SocketedItemJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity(indices = arrayOf(Index(value = ["characterName"], unique = true)))
data class CharacterDb constructor(
    @PrimaryKey
//    var id: Long?,
    val characterName: String,
    val league: String,
    val classPoe: String,
    val level: Int,
    val accountName: String
)

// TODO ask about managing IDs in room
@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = CharacterDb::class,
            parentColumns = arrayOf("characterName"),
            childColumns = arrayOf("character_name"),
            onDelete = CASCADE,
            deferred = true
        )
    ),
    indices = arrayOf(Index(value = ["character_name"]))
)
data class ItemDb constructor(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "character_name") val characterName: String,
    val width: Int,
    val height: Int,
    val itemLevel: Int = 0,
    val icon: String,   //TODO
    val sockets: List<ItemSocketJson> = ArrayList(),
    val name: String,
    val base: String,
    val properties: List<ItemPropertiesJson> = ArrayList(),
    val implicitMods: List<String> = ArrayList(),
    val craftedMods: List<String> = ArrayList(),
    val enchantedMods: List<String> = ArrayList(),
    val corrupted: Boolean = false,
    val itemRarity: Int = -1,  //frameType 0=white, 1=magic, 2=rare, 3=unique
    val inventoryId: String,
    val socketedItems: List<SocketedItemJson> = ArrayList(),
    val x: Int,
    val explicitMods: List<String> = ArrayList()
)

class CharacterItemsDb {

    @Embedded
    lateinit var character: CharacterDb

    @Relation(parentColumn = "characterName", entityColumn = "character_name")
    var characterItems: List<ItemDb> = arrayListOf()

}

fun CharacterItemsDb.asCharacterDb(): CharacterDb {
    return CharacterDb(
        characterName = character.characterName,
        classPoe = character.classPoe,
        league = character.league,
        accountName = character.accountName,
        level = character.level
    )
}

fun CharacterItemsDb.asSkillGemsLinks(): List<SkillGemsLinks> {
//    val sortedGems = characterItems.filter { it.socketedItems.isNotEmpty() }


    val linksList = characterItems.map {

        var socketsWithGems = ArrayList<Int>()
        for (socketedGem in it.socketedItems) {
            socketsWithGems.add(socketedGem.socket)
        }

        var groupLinks = hashMapOf<Int, Array<SocketedItemJson>>()
        var lastGroup = 0
        var group = 0
        var link = mutableListOf<SocketedItemJson>()
        if (socketsWithGems.isNotEmpty()) {
            for ((index, socket) in it.sockets.withIndex()) {
                lastGroup = group
                group = socket.group
                if (group == lastGroup) {
                    if (socketsWithGems.contains(index))
                        link.add(it.socketedItems[socketsWithGems.indexOf(index)])
                } else {
                    groupLinks.put(lastGroup, link.toTypedArray())
                    link = mutableListOf()
                    link.add(it.socketedItems[socketsWithGems.indexOf(index)])
                    lastGroup = group
                }
                if (index == it.sockets.size - 1) groupLinks.put(lastGroup, link.toTypedArray())
            }
        }

        SkillGemsLinks(
            links = groupLinks,
            inventoryId = it.inventoryId,
            sockets = socketsWithGems.size
        )
    }

    return linksList.filter { it.links.isNotEmpty() }.sortedByDescending { it.sockets }
}


class SocketsTypeConverter {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val listSockets = Types.newParameterizedType(List::class.java, ItemSocketJson::class.java)
    val jsonAdapter = moshi.adapter<List<ItemSocketJson>>(listSockets)

    @TypeConverter
    fun listSocketstoJson(sockets: List<ItemSocketJson>): String {
        return jsonAdapter.toJson(sockets)
    }

    @TypeConverter
    fun jsonToListSockets(jsonSocket: String): List<ItemSocketJson>? {
        return jsonAdapter.fromJson(jsonSocket)
    }
}

class PropertiesTypeConverter {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val listProperties = Types.newParameterizedType(List::class.java, ItemPropertiesJson::class.java)
    val jsonAdapter = moshi.adapter<List<ItemPropertiesJson>>(listProperties)

    @TypeConverter
    fun listPropertiestoJson(sockets: List<ItemPropertiesJson>): String {
        return jsonAdapter.toJson(sockets)
    }

    @TypeConverter
    fun jsonTolistProperties(jsonSocket: String): List<ItemPropertiesJson>? {
        return jsonAdapter.fromJson(jsonSocket)
    }
}

class listStringTypeConverter {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val listStrings = Types.newParameterizedType(List::class.java, String::class.java)
    val jsonAdapter = moshi.adapter<List<String>>(listStrings)

    @TypeConverter
    fun listStringstoJson(sockets: List<String>): String {
        return jsonAdapter.toJson(sockets)
    }

    @TypeConverter
    fun jsonToListStrings(jsonSocket: String): List<String>? {
        return jsonAdapter.fromJson(jsonSocket)
    }
}

class SocketedItemJsonTypeConverter {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val listSocketedItemJson = Types.newParameterizedType(List::class.java, SocketedItemJson::class.java)
    val jsonAdapter = moshi.adapter<List<SocketedItemJson>>(listSocketedItemJson)

    @TypeConverter
    fun listPropertiestoJson(sockets: List<SocketedItemJson>): String {
        return jsonAdapter.toJson(sockets)
    }

    @TypeConverter
    fun jsonTolistProperties(jsonSocket: String): List<SocketedItemJson>? {
        return jsonAdapter.fromJson(jsonSocket)
    }
}

