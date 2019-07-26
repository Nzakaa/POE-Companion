package com.example.poeproladder.session

import android.content.Context
import android.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class SessionServiceImpl(
    val context: Context
) : SessionService {

    private val characterSubject = BehaviorSubject.create<String>()
    private val characterObs = characterSubject.filter { v -> v != EMPTY}


    override fun saveAccount(accountName: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(ACCOUNT_KEY, accountName)
            .apply()
        characterSubject.onNext(EMPTY)
    }

    override fun saveCharacter(characterName: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(CHARACTER_KEY, characterName)
            .apply()

        characterSubject.onNext(characterName)
    }

    override fun getCharacter(): String? {
        val characterName = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(CHARACTER_KEY, "default")
        characterSubject.onNext(characterName)
        return characterName
    }

    override fun getAccount(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(ACCOUNT_KEY, "default")
    }

    override fun getCharacterObservable(): Observable<String> {
        return characterObs
    }

    companion object {
        const val ACCOUNT_KEY = "Account"
        const val CHARACTER_KEY = "Character"
        const val EMPTY = ""
        private var INSTANCE: SessionServiceImpl? = null

        @JvmStatic fun getInstance(context: Context): SessionServiceImpl {
            return INSTANCE ?: SessionServiceImpl(context)
                .apply { INSTANCE = this }
        }
    }
}