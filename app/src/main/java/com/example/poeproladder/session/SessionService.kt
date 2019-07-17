package com.example.poeproladder.session

interface SessionService {
    fun saveCharacter(characterName: String)
    fun saveAccount(accountName: String)
    fun getCharacter(): String?
    fun getAccount(): String?
}