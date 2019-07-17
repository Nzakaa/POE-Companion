package com.example.poeproladder.interactors.Database

import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import com.example.poeproladder.network.asDatabaseModel
import com.example.poeproladder.session.SessionService
import io.reactivex.Maybe
import io.reactivex.Observable
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

    override fun saveCharacter(character: CharacterDb) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveItems(items: CharacterWindowItemsJson, characterName: String) {
        val itemsDb = items.asDatabaseModel(characterName)
        database.itemsDao.saveItems(itemsDb)
    }

    override fun getItemsByName(characterName: String): Maybe<CharacterItemsDb> {
        return database.itemsDao.getItemsByName(characterName)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { data ->
                session.saveAccount(data.character.accountName)
                session.saveCharacter(data.character.characterName)
                observable.onNext(data)
            }
    }

    override fun getAccountCharacters(accountName: String): Maybe<List<CharacterDb>> {
        return database.characterDao.getAccountCharacters(accountName)
            .subscribeOn(Schedulers.io())
    }

    override fun getRecentCharacters(): Maybe<List<CharacterDb>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsByNameObservable(): Observable<CharacterItemsDb> {
        return observable
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