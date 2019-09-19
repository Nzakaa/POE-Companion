package com.example.poeproladder.database

import androidx.room.*
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CharacterDao {
    @Query("select * from CharacterDb")
    fun getRecentCharacters(): Maybe<List<CharacterDb>>

    @Query("select * from CharacterDb where accountName = :accountName")
    fun getAccountCharacters(accountName: String): Single<List<CharacterDb>>

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
    fun getItemsByName(characterName: String): Single<CharacterItemsDb>

    @Transaction
    @Query("select * from CharacterDb where accountName = :accountName")
    fun getAllItemsPerAccount(accountName: String): Single<List<CharacterItemsDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveItems(items: List<ItemDb>)

    @Query("delete from ItemDb where character_name = :characterName")
    fun deleteItems(characterName: String)

    @Query("delete from ItemDb")
    fun deleteAllItems()
}

@Database(entities = [CharacterDb::class, ItemDb::class], version = 1, exportSchema = false)
@TypeConverters(
    SocketsTypeConverter::class,
    PropertiesTypeConverter::class,
    listStringTypeConverter::class,
    SocketedItemJsonTypeConverter::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao
    abstract val itemsDao: ItemsDao
}
