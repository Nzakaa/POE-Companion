package com.example.poeproladder.interactors.Network

import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import com.example.poeproladder.network.Network
import io.reactivex.Single

class CharacterNetworkInteractorImpl : CharacterNetworkInteractor {

    private val database: CharacterDatabase
    private val network: Network
    private val databaseInteractor: CharacterDatabaseInteractor

    private constructor(
        database: CharacterDatabase,
        network: Network,
        databaseInteractor: CharacterDatabaseInteractor
    ) {
        this.database = database
        this.network = network
        this.databaseInteractor = databaseInteractor
    }

    override fun getCharacters(accountName: String): Single<List<CharacterDb>> {
        return network.characterApi.getAccountInfo(accountName)
            .map{ data -> databaseInteractor.saveCharacters(data, accountName) }
            .flatMap { databaseInteractor.getAccountCharacters(accountName) }

    }

    override fun getCharacterItems(accountName: String, characterName: String): Single<CharacterWindowItemsJson> {
        return network.characterApi.getCharacterInfo(accountName, characterName)
    }

    companion object {
        private var INSTANCE: CharacterNetworkInteractorImpl? = null

        @JvmStatic fun getInstance(
            database: CharacterDatabase,
            network: Network,
            databaseInteractor: CharacterDatabaseInteractor
        ): CharacterNetworkInteractorImpl {
            return INSTANCE ?: CharacterNetworkInteractorImpl(database, network, databaseInteractor)
                .apply { INSTANCE = this }
        }
    }

}