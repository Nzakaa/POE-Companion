package com.example.poeproladder.session

import com.example.poeproladder.domain.CharacterRequest
import io.reactivex.Observable


interface SessionService {
    fun saveCharacter(characterName: String)
    fun saveAccount(accountName: String)
    fun getCharacter(): String?
    fun getAccount(): String?
    fun getCharacterObservable(): Observable<CharacterRequest>
    fun getNetworkStatus():Boolean?
}