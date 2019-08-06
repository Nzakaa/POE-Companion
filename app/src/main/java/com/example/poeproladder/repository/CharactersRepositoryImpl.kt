package com.example.poeproladder.repository

import android.util.Log
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractor
import com.example.poeproladder.network.CharacterWindowCharacterJson
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CharactersRepositoryImpl(
    val databaseInteractor: CharacterDatabaseInteractor,
    val networkInteractor: CharacterNetworkInteractor
) : CharactersRepository {
    var disposable: Disposable? = null
    override fun getItemsByName(accountName: String, characterName: String): Observable<CharacterItemsDb> {
        val databaseObservable =
            databaseInteractor.getItemsByName(characterName!!)
        val networkObservable =
            networkInteractor.getCharacterItems(accountName!!, characterName)
        if (!isNetworkInProgress()!!) {
            disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    when {
                        result.characterItems.isNotEmpty() -> databaseInteractor.observableOnNext(result)
                        else -> {
                            networkObservable
                                .subscribeOn(Schedulers.io())
                                .subscribe({result -> databaseInteractor.saveItems(result, characterName)
                                }, { error -> Log.d("error", "${error.localizedMessage}") })
                        }
                    }
                },
                    { error -> Log.d("error", "${error.localizedMessage}") })
        }
        return databaseInteractor.getCharacterItemsObservable()
    }

    override fun saveAccountData(characters: List<CharacterWindowCharacterJson>, accountName: String) {
        databaseInteractor.saveCharacters(characters, accountName)
    }

    override fun getAccountData(accountName: String): Single<List<CharacterDb>> {
        return networkInteractor.getCharacters(accountName)

    }

    private fun isNetworkInProgress(): Boolean? {
        val currentDisposable = disposable
        return currentDisposable != null && !currentDisposable.isDisposed
    }

    companion object {
        private var INSTANCE: CharactersRepositoryImpl? = null

        @JvmStatic
        fun getInstance(
            databaseInteractor: CharacterDatabaseInteractor,
            networkInteractor: CharacterNetworkInteractor
        ): CharactersRepositoryImpl {
            return INSTANCE ?: CharactersRepositoryImpl(databaseInteractor, networkInteractor)
                .apply { INSTANCE = this }
        }
    }
}