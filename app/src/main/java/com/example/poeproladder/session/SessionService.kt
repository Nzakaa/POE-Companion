package com.example.poeproladder.session

import io.reactivex.Observable


interface SessionService {
    fun saveCharacter(characterName: String)
    fun saveAccount(accountName: String)
    fun getCharacter(): String?
    fun getAccount(): String?

    fun getCharacterObservable(): Observable<String>
}