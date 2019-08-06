package com.example.poeproladder.session

import android.content.Context
import android.preference.PreferenceManager
import com.example.poeproladder.domain.CharacterRequest
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class SessionServiceImpl(
    val context: Context
) : SessionService {

    private val characterSubject = BehaviorSubject.create<CharacterRequest>()
    private var account:String = ""
    private var character:String = ""

    override fun saveAccount(accountName: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(ACCOUNT_KEY, accountName)
            .apply()

        account = accountName

        updateObservable()
    }

    override fun saveCharacter(characterName: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(CHARACTER_KEY, characterName)
            .apply()

        character = characterName

        updateObservable()
    }

    override fun getCharacter(): String? {
        val characterName = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(CHARACTER_KEY, "default")
        if (characterName != "default") character = characterName
        updateObservable()
        return characterName
    }

    override fun getAccount(): String? {
        val accountName = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(ACCOUNT_KEY, "default")

        if (accountName != "default") account = accountName
//        updateObservable()
        return accountName
    }

    override fun getCharacterObservable(): Observable<CharacterRequest> {
        return characterSubject
    }

    private fun updateObservable() {
        var characterRequestInfo: CharacterRequest
        if (account != "" && character != "") {
            characterRequestInfo = CharacterRequest(account, character)
            characterSubject.onNext(characterRequestInfo)
        }
    }

    companion object {
        const val ACCOUNT_KEY = "Account"
        const val CHARACTER_KEY = "Character"
    }
}