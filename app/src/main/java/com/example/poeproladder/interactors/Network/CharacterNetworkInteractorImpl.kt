package com.example.poeproladder.interactors.Network

import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import com.example.poeproladder.network.Network
import io.reactivex.Single

class CharacterNetworkInteractorImpl (
    val database: CharacterDatabase,
    val network: Network,
    val databaseInteractor: CharacterDatabaseInteractor

): CharacterNetworkInteractor {
    override fun getCharacters(accountName: String): Single<List<CharacterWindowCharacterJson>> {
        return network.characterApi.getAccountInfo(accountName)
            .doOnSuccess { data -> databaseInteractor.saveCharacters(data, accountName)}

    }

    override fun getCharacterItems(accountName: String, characterName: String): Single<CharacterWindowItemsJson> {
        return network.characterApi.getCharacterInfo(accountName, characterName)
            .doOnSuccess{data -> databaseInteractor.saveItems(data, characterName)}
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