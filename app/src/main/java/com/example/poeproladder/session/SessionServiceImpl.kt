package com.example.poeproladder.session

import android.content.Context
import android.preference.PreferenceManager
import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractorImpl
import com.example.poeproladder.network.Network

class SessionServiceImpl(
    val context: Context
) : SessionService {

    override fun saveAccount(accountName: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(ACCOUNT_KEY, accountName)
            .apply()    }

    override fun saveCharacter(characterName: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(CHARACTER_KEY, characterName)
            .apply()
    }

    override fun getCharacter(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(ACCOUNT_KEY, "nzaka")
    }

    override fun getAccount(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(CHARACTER_KEY, "vvidehardo")    }

    companion object {
        val ACCOUNT_KEY = "Account"
        val CHARACTER_KEY = "Character"
        private var INSTANCE: SessionServiceImpl? = null

        @JvmStatic fun getInstance(context: Context): SessionServiceImpl {
            return INSTANCE ?: SessionServiceImpl(context)
                .apply { INSTANCE = this }
        }
    }
}