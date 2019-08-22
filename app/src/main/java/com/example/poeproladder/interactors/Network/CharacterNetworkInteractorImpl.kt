package com.example.poeproladder.interactors.Network

import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.network.CharacterWindowApi
import com.example.poeproladder.network.CharacterWindowItemsJson
import io.reactivex.Single
import javax.inject.Inject

class CharacterNetworkInteractorImpl : CharacterNetworkInteractor {

    private val database: CharacterDatabase
    private val network: CharacterWindowApi
    private val databaseInteractor: CharacterDatabaseInteractor

    @Inject
    constructor(
        database: CharacterDatabase,
        network: CharacterWindowApi,
        databaseInteractor: CharacterDatabaseInteractor
    ) {
        this.database = database
        this.network = network
        this.databaseInteractor = databaseInteractor
    }

    override fun getCharacters(accountName: String): Single<List<CharacterDb>> {
        return network.getAccountInfo(accountName)
            .map{ data -> databaseInteractor.saveCharacters(data, accountName) }
            .flatMap { databaseInteractor.getAccountCharacters(accountName) }

    }

    override fun getCharacterItems(accountName: String, characterName: String): Single<CharacterWindowItemsJson> {
        return network.getCharacterInfo(accountName, characterName)
    }

//    companion object {
//        private var INSTANCE: CharacterNetworkInteractorImpl? = null
//
//        @JvmStatic fun getInstance(
//            database: CharacterDatabase,
//            network: Network,
//            databaseInteractor: CharacterDatabaseInteractor
//        ): CharacterNetworkInteractorImpl {
//            return INSTANCE ?: CharacterNetworkInteractorImpl(database, network, databaseInteractor)
//                .apply { INSTANCE = this }
//        }
//    }

}