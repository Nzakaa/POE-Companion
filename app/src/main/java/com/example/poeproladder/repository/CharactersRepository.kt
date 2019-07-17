package com.example.poeproladder.repository

import com.example.poeproladder.database.CharacterItemsDb
import io.reactivex.Observable

interface CharactersRepository {
    fun getItemsByName(): Observable<CharacterItemsDb>
}