package com.example.poeproladder.repository

import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractor
import com.example.poeproladder.session.SessionService
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

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
            databaseInteractor.getItemsByName(characterName!!).toObservable()
        val networkObservable =
            networkInteractor.getCharacterItems(accountName!!, characterName).toObservable()
        if (!isNetworkInProgress()!!) {
            disposable = Observable.concat(databaseObservable, networkObservable)
                .firstElement()
                .subscribe()
        }
        return databaseInteractor.getItemsByNameObservable()
    }

    fun isNetworkInProgress(): Boolean? {
        return disposable?.let { !it.isDisposed }
    }

    companion object {
        private var INSTANCE: CharactersRepositoryImpl? = null

        @JvmStatic fun getInstance(
            session: SessionService,
            databaseInteractor: CharacterDatabaseInteractor,
            networkInteractor: CharacterNetworkInteractor
        ): CharactersRepositoryImpl {
            return INSTANCE ?: CharactersRepositoryImpl(session, databaseInteractor, networkInteractor)
                .apply { INSTANCE = this }
        }
    }
}