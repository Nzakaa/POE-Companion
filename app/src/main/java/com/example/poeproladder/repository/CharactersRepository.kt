package com.example.poeproladder.repository

import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.network.CharacterWindowCharacterJson
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface CharactersRepository {
    fun getItemsByName(): Observable<CharacterItemsDb>
    fun getAccountData(accountName: String) : Single<List<CharacterDb>>
    fun saveAccountData(characters: List<CharacterWindowCharacterJson>, accountName: String)
}