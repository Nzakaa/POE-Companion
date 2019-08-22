package com.example.poeproladder.dagger.modules

import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractorImpl
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractorImpl
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.repository.CharactersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideDatabase(databaseInteractor: CharacterDatabaseInteractorImpl): CharacterDatabaseInteractor {
        return databaseInteractor
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideNetwork(networkInteractor: CharacterNetworkInteractorImpl): CharacterNetworkInteractor {
        return networkInteractor
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideRepository(
        networkInteractor: CharacterNetworkInteractor,
        databaseInteractor: CharacterDatabaseInteractor
    ): CharactersRepository {
//        return CharactersRepositoryImpl(databaseInteractor, networkInteractor)
        return CharactersRepositoryImpl(databaseInteractor, networkInteractor)
    }
}