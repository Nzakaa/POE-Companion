package com.example.poeproladder.database

import android.content.Context
import androidx.room.*
import io.reactivex.Single

@Dao
interface CharacterDao {
    @Query("select * from CharactersDb")
    fun getCharacters(): Single<List<CharactersDb>>

    @Query("select * from CharactersDb where characterName = :characterName")
    fun getCharacter(characterName: String): Single<CharactersDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: CharactersDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<CharactersDb>)

    @Query("delete from CharactersDb")
    fun deleteAll()
}

@Dao
interface ItemsDao {
    @Transaction
    @Query("select * from CharactersDb where characterName = :characterName")
    fun getItems(characterName: String): Single<CharacterItemsDb>

    @Transaction
    @Query("select * from CharactersDb" )
    fun getItemsAll(): Single<List<CharacterItemsDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(items: List<ItemsDb>)

    @Query("delete from ItemsDb")
    fun deleteAll()
}

@Database(entities = [CharactersDb::class, ItemsDb::class], version = 1)
@TypeConverters(SocketsTypeConverter::class)
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