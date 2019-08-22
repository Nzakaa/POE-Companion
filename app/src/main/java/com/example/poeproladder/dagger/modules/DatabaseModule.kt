package com.example.poeproladder.dagger.modules

import android.content.Context
import androidx.room.Room
import com.example.poeproladder.database.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    @JvmStatic
    fun providesDatabase(context: Context): CharacterDatabase {
        return Room.databaseBuilder(context.applicationContext,
                CharacterDatabase::class.java,
                "characters")
            .fallbackToDestructiveMigration()
            .build()
    }
}


//fun getDatabase(context: Context): CharacterDatabase {
//    synchronized(CharacterDatabase::class.java) {
//        if (!::INSTANCE.isInitialized) {
//            INSTANCE = Room.databaseBuilder(context.applicationContext,
//                CharacterDatabase::class.java,
//                "characters").build()
//        }
//    }
//    return INSTANCE