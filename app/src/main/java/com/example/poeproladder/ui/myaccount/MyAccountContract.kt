package com.example.poeproladder.ui.myaccount

import com.example.poeproladder.BaseContract
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import io.reactivex.Observable

interface MyAccountContract {
    interface MyAccountPresenter: BaseContract.BasePresenter<BaseContract.BaseView> {
        fun getCharacters(accountName: String)
        fun getItems() : Observable<CharacterItemsDb>
        fun loadItemIcons()
    }

    interface MyAccountView: BaseContract.BaseView {
        fun showProgressBar(show: Boolean)
        fun showCharacterList(characters: List<CharacterDb>)
        fun showCharacterWindow(items:CharacterItemsDb)
    }
}