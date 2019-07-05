package com.example.poeproladder.network

import com.squareup.moshi.*
import java.lang.NullPointerException

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
    val depth: PoeLadderDepthJson = PoeLadderDepthJson(-1, -1)
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
    val enchantedMods: List<String> = ArrayList(),
    @Json(name = "frameType") val itemRarity: Int = -1,  //frameType 0=white, 1=magic, 2=rare, 3=unique
    val inventoryId: String,
    val socketedItems: List<SocketedItemJson> = ArrayList()
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


//@JsonClass(generateAdapter = true)
//class ItemPropertyValuesJson(
//    val name: String,
//    val values: Array<Array<Any>>
//)

//class ItemPropertyValuesJsonAdapter {
//    @FromJson
//    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<ItemPropertyValuesJson>): ItemPropertyValuesJson? {
//        val value = jsonReader.nextString()
//        return if (value.startsWith("in-progress")) Stage.IN_PROGRESS else delegate.fromJsonValue(value)
//    }
//}

//class ItemPropertiesJsonJsonAdapter(moshi: Moshi) : JsonAdapter<ItemPropertiesJson>() {
//    private val options: JsonReader.Options = JsonReader.Options.of("name", "values")
//
//    private val stringAdapter: JsonAdapter<String> =
//        moshi.adapter<String>(String::class.java, kotlin.collections.emptySet(), "name")
//
//    private val listOfItemPropertyValuesJsonAdapter: JsonAdapter<List<ItemPropertyValuesJson>> =
//        moshi.adapter<List<ItemPropertyValuesJson>>(Types.newParameterizedType(List::class.java, ItemPropertyValuesJson::class.java), kotlin.collections.emptySet(), "values")
//
//    override fun toString(): String = "GeneratedJsonAdapter(ItemPropertiesJson)"
//
//    override fun fromJson(reader: JsonReader): ItemPropertiesJson {
//        var name: String? = null
//        var values: List<ItemPropertyValuesJson>? = null
//        reader.beginObject()
//        while (reader.hasNext()) {
//            when (reader.selectName(options)) {
//                0 -> name = stringAdapter.fromJson(reader) ?: throw JsonDataException("Non-null value 'name' was null at ${reader.path}")
//                1 -> values = listOfItemPropertyValuesJsonAdapter.fromJson(reader) ?: throw JsonDataException("Non-null value 'values' was null at ${reader.path}")
//                -1 -> {
//                    // Unknown name, skip it.
//                    reader.skipName()
//                    reader.skipValue()
//                }
//            }
//        }
//        reader.endObject()
//        var result = ItemPropertiesJson(
//            name = name ?: throw JsonDataException("Required property 'name' missing at ${reader.path}"))
//        result = ItemPropertiesJson(
//            name = name,
//            values = values ?: result.values)
//        return result
//    }
//
//    override fun toJson(writer: JsonWriter, value: ItemPropertiesJson?) {
//        if (value == null) {
//            throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
//        }
//        writer.beginObject()
//        writer.name("name")
//        stringAdapter.toJson(writer, value.name)
//        writer.name("values")
//        listOfItemPropertyValuesJsonAdapter.toJson(writer, value.values)
//        writer.endObject()
//    }
//}

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
    val colour: String,
    @Json(name = "category") val category: ItemCategoryJson = ItemCategoryJson(),
    @Json(name = "properties") val socketedItem: List<ItemPropertiesJson> = ArrayList()
    )

@JsonClass(generateAdapter = true)
class ItemCategoryJson(
    val gems: List<String> = ArrayList()
)













