package com.example.poeproladder.interactors.Network

import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import io.reactivex.Single

interface CharacterNetworkInteractor {
    fun getCharacters(accountName: String): Single<List<CharacterDb>>
    fun getCharacterItems(accountName: String, characterName: String): Single<CharacterWindowItemsJson>
}