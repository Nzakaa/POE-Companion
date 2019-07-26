package com.example.poeproladder.ui.characterselection

import com.example.poeproladder.BaseContract
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import io.reactivex.Observable

interface CharacterSelectionContract {
    interface CharacterSelectionPresenter : BaseContract.BasePresenter {
        fun getCharacters(accountName: String)
        fun onCharacterPicked(characterName: String)
    }

    interface MyAccountView: BaseContract.BaseView {
        fun showProgressBar(show: Boolean)
        fun navigateToInventory()
        fun showDefaultScreen()
        fun showCharacterList(characters: List<CharacterDb>)
        fun showCharacterWindow(items:CharacterItemsDb)
        fun showCurrentAccount(accountName: String)
    }
}