package com.example.poeproladder.database

import android.content.Context
import androidx.room.*
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CharacterDao {
    @Query("select * from CharacterDb")
    fun getRecentCharacters(): Maybe<List<CharacterDb>>

    @Query("select * from CharacterDb where accountName = :accountName")
    fun getAccountCharacters(accountName: String): Maybe<List<CharacterDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCharacter(character: CharacterDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCharacters(characters: List<CharacterDb>)

    @Query("delete from CharacterDb")
    fun deleteAllCharacters()
}

@Dao
interface ItemsDao {
    @Transaction
    @Query("select * from CharacterDb where characterName = :characterName")
    fun getItemsByName(characterName: String): Maybe<CharacterItemsDb>

    @Transaction
    @Query("select * from CharacterDb")
    fun getAllItems(): Maybe<List<CharacterItemsDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveItems(items: List<ItemsDb>)

    @Query("delete from ItemsDb")
    fun deleteAllItems()
}

@Database(entities = [CharacterDb::class, ItemsDb::class], version = 1, exportSchema = false)
@TypeConverters(
    SocketsTypeConverter::class,
    PropertiesTypeConverter::class,
    listStringTypeConverter::class,
    SocketedItemJsonTypeConverter::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao
    abstract val itemsDao: ItemsDao
}

@Volatile
private lateinit var INSTANCE: CharacterDatabase

fun getDatabase(context: Context): CharacterDatabase {
    synchronized(CharacterDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                CharacterDatabase::class.java,
                "characters").build()
        }
    }
    return INSTANCE
}