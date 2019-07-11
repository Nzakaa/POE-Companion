package com.example.poeproladder.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.poeproladder.network.ItemPropertiesJson
import com.example.poeproladder.network.ItemSocketJson
import com.example.poeproladder.network.SocketedItemJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity(indices = arrayOf(Index(value = ["characterName"], unique = true)))

data class CharactersDb constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    val characterName: String,
    val league: String,
    val classPoe: String,
    val level: Int,
    val accountName: String
)

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = CharactersDb::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("character_id"),
            onDelete = CASCADE,
            deferred = true
        )
    )
)
data class ItemsDb constructor(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "character_id")
    val characterId: Long?,
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
    val itemRarity: Int = -1,  //frameType 0=white, 1=magic, 2=rare, 3=unique
    val inventoryId: String,
    val socketedItems: List<SocketedItemJson> = ArrayList()
)

class CharacterInformationDb(
    val name: String,
    val league: String,
    val classPoe: String,
    val level: Int
)

//class ItemsDb(
//    val items: Array<ItemDb>
//)

// inventoryID = MainInventoty excluded
//class ItemDb(
//    val width: Int,
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
//)

class SocketsTypeConverter() {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()).build()
}

