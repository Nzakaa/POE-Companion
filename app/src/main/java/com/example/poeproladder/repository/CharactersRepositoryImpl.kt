package com.example.poeproladder.repository

import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractor
import com.example.poeproladder.network.CharacterWindowCharacterJson
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    val databaseInteractor: CharacterDatabaseInteractor,
    val networkInteractor: CharacterNetworkInteractor
) : CharactersRepository {
    private var disposable: Disposable? = null
    override fun getItemsObservable(): Observable<CharacterItemsDb> {
        return databaseInteractor.getCharacterItemsObservable()
    }
    override fun getItemsByName(
        accountName: String,
        characterName: String
    ): Observable<CharacterItemsDb> {
        val databaseObservable =
            databaseInteractor.getItemsByName(characterName)
        val networkObservable =
            networkInteractor.getCharacterItems(accountName, characterName)
        if (!isNetworkInProgress()!!) {
            disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    when {
                        result.characterItems.isNotEmpty() -> {
                            databaseInteractor.observableOnNext(result)
                        }
                        else -> {
                            networkObservable
                                .subscribeOn(Schedulers.io())
                                .subscribe({ response ->
                                    databaseInteractor.saveItems(response, characterName)
                                }, { error -> })
                        }
                    }
                },
                    { error -> })
        }
        return databaseInteractor.getCharacterItemsObservable()
    }

    override fun saveAccountData(
        characters: List<CharacterWindowCharacterJson>,
        accountName: String
    ) {
        databaseInteractor.saveCharacters(characters, accountName)
    }

    override fun getAccountData(
        accountName: String,
        networkIsActive: Boolean
    ): Single<List<CharacterDb>> {
        return if (networkIsActive) networkInteractor.getCharacters(accountName)
        else databaseInteractor.getAllCharactersWithItemsPerAccount(accountName)
    }

    private fun isNetworkInProgress(): Boolean? {
        val currentDisposable = disposable
        return currentDisposable != null && !currentDisposable.isDisposed
    }
}