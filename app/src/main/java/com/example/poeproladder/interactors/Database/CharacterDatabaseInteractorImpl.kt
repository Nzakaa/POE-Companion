package com.example.poeproladder.interactors.Database

import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.database.asCharacterDb
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import com.example.poeproladder.network.asDatabaseModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CharacterDatabaseInteractorImpl @Inject constructor(
    val database: CharacterDatabase
) : CharacterDatabaseInteractor {

    private val observable = BehaviorSubject.create<CharacterItemsDb>()

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

    override fun saveItems(items: CharacterWindowItemsJson, characterName: String) {
        val itemsDb = items.asDatabaseModel(characterName)
        database.itemsDao.deleteItems(characterName)
        database.itemsDao.saveItems(itemsDb)
        val disp = getItemsByName(characterName)
            .subscribeOn(Schedulers.io())
            .subscribe { result -> observable.onNext(result) }
    }

    override fun getItemsByName(characterName: String): Single<CharacterItemsDb> {
        return database.itemsDao.getItemsByName(characterName)
    }

    override fun getAccountCharacters(accountName: String): Single<List<CharacterDb>> {
        return database.characterDao.getAccountCharacters(accountName)
            .subscribeOn(Schedulers.io())
    }

    override fun getAllCharactersWithItemsPerAccount(accountName: String): Single<List<CharacterDb>> {
        return database.itemsDao.getAllItemsPerAccount(accountName)
            .map { items ->
                items.filter { it.characterItems.isNotEmpty() }
                .map { it.asCharacterDb() }
            }
    }
}
