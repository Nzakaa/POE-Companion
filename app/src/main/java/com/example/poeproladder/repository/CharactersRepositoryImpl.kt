package com.example.poeproladder.repository

import android.util.Log
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractor
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import com.example.poeproladder.session.SessionService
import com.squareup.moshi.JsonDataException
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException

class CharactersRepositoryImpl(
    val session: SessionService,
    val databaseInteractor: CharacterDatabaseInteractor,
    val networkInteractor: CharacterNetworkInteractor
) : CharactersRepository {
    var disposable: Disposable? = null
    override fun getItemsByName(): Observable<CharacterItemsDb> {
        val accountName = session.getAccount()
        val characterName = session.getCharacter()
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
                                .subscribe{result -> databaseInteractor.saveItems(result, characterName)
                                }
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

    fun isNetworkInProgress(): Boolean? {
        val currentDisposable = disposable
        return currentDisposable != null && !currentDisposable.isDisposed
    }

    fun handleNonHttpException(throwable: Throwable) {
        if (throwable is HttpException) {

        } else if (throwable is JsonDataException) {

        } else if (throwable is SocketTimeoutException) {

        } else if (throwable is ConnectException) {

        } else {
            throw RuntimeException(throwable)
        }
    }

    companion object {
        private var INSTANCE: CharactersRepositoryImpl? = null

        @JvmStatic
        fun getInstance(
            session: SessionService,
            databaseInteractor: CharacterDatabaseInteractor,
            networkInteractor: CharacterNetworkInteractor
        ): CharactersRepositoryImpl {
            return INSTANCE ?: CharactersRepositoryImpl(session, databaseInteractor, networkInteractor)
                .apply { INSTANCE = this }
        }
    }
}