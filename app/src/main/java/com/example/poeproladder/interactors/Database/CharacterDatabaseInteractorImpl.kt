package com.example.poeproladder.interactors.Database

import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import com.example.poeproladder.network.asDatabaseModel
import com.example.poeproladder.session.SessionService
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class CharacterDatabaseInteractorImpl(
    val database: CharacterDatabase,
    val session: SessionService
) : CharacterDatabaseInteractor {
    val observable = BehaviorSubject.create<CharacterItemsDb>()

    override fun saveCharacters(characters: List<CharacterWindowCharacterJson>, accountName: String) {
        val charactersDb = characters.map {
            it.asDatabaseModel(accountName)
        }
        database.characterDao.saveCharacters(charactersDb)
    }

    override fun getCharacterItemsObservable(): Observable<CharacterItemsDb> {
        return observable
    }

    override fun observableOnNext(items: CharacterItemsDb) {
        observable.onNext(items)
    }

    override fun saveCharacter(character: CharacterDb) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveItems(items: CharacterWindowItemsJson, characterName: String) {
        val itemsDb = items.asDatabaseModel(characterName)
        database.itemsDao.saveItems(itemsDb)
        getItemsByName(characterName)
            .subscribeOn(Schedulers.io())
            .subscribe { result ->  observable.onNext(result)}
    }

    override fun getItemsByName(characterName: String): Single<CharacterItemsDb> {
        return database.itemsDao.getItemsByName(characterName)
    }


    override fun getAccountCharacters(accountName: String): Single<List<CharacterDb>> {
        return database.characterDao.getAccountCharacters(accountName)
            .subscribeOn(Schedulers.io())
    }

    override fun getRecentCharacters(): Maybe<List<CharacterDb>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        private var INSTANCE: CharacterDatabaseInteractorImpl? = null

        @JvmStatic fun getInstance(
            database: CharacterDatabase,
            session: SessionService
        ): CharacterDatabaseInteractorImpl {
            return INSTANCE ?: CharacterDatabaseInteractorImpl(database, session)
                .apply { INSTANCE = this }
        }
    }
}