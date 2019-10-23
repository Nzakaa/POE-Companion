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
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class CharactersRepositoryImpl @Inject constructor(
    private val databaseInteractor: CharacterDatabaseInteractor,
    private val networkInteractor: CharacterNetworkInteractor
) : CharactersRepository {
    private var disposable: Disposable? = null
    override fun getItemsObservable(): Observable<CharacterItemsDb> {
        return databaseInteractor.getCharacterItemsObservable()
    }

    // Check if there is an entry in the database
    // if there is immediately display current data from database and make network call to update it,
    // if not make network call to get data
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
                    if (result.characterItems.isNotEmpty()) {
                        databaseInteractor.observableOnNext(result)
                        networkObservable
                            .subscribeOn(Schedulers.io())
                            .subscribe({ response ->
                                databaseInteractor.saveItems(response, characterName)
                            }, { this.handleNonHttpException(it) })
                    } else {
                        networkObservable
                            .subscribeOn(Schedulers.io())
                            .subscribe({ response ->
                                databaseInteractor.saveItems(response, characterName)
                            }, { this.handleNonHttpException(it) })
                    }
                },  { error -> throw RuntimeException(error) })
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

    private fun handleNonHttpException(throwable: Throwable) {
        // if not an HttpException throw further
        if (throwable is HttpException) {

        } else if (throwable is JsonDataException) {

        } else if (throwable is SocketTimeoutException) {

        } else if (throwable is UnknownHostException) {

        } else if (throwable is ConnectException) {

        } else {
            throw RuntimeException(throwable)
        }
    }
}