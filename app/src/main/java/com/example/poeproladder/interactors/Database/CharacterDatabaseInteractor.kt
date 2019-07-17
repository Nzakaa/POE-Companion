package com.example.poeproladder.interactors.Database

import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import io.reactivex.Maybe
import io.reactivex.Observable

interface CharacterDatabaseInteractor {
    fun saveCharacters(characters: List<CharacterWindowCharacterJson>, accountName: String)
    fun saveCharacter(character: CharacterDb)
    fun getAccountCharacters(accountName: String): Maybe<List<CharacterDb>>
    fun getRecentCharacters(): Maybe<List<CharacterDb>>
    fun saveItems(items: CharacterWindowItemsJson, characterName: String)
    fun getItemsByName(characterName: String): Maybe<CharacterItemsDb>
    fun getItemsByNameObservable() : Observable<CharacterItemsDb>
}