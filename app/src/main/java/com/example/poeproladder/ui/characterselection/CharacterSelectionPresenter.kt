package com.example.poeproladder.ui.characterselection

import com.example.poeproladder.BaseApp
import com.example.poeproladder.BaseContract
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.repository.CharactersRepositoryImpl
import com.example.poeproladder.session.SessionServiceImpl
import com.example.poeproladder.ui.BaseFragmentPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CharacterSelectionPresenter(
    view: CharacterSelectionContract.MyAccountView,
    private val repository: CharactersRepository
) : BaseFragmentPresenter<CharacterSelectionContract.MyAccountView>(view), CharacterSelectionContract.CharacterSelectionPresenter {

    private val session = BaseApp.session!!

    override fun getCharacters(accountName: String) {
        getCharactersFromRepo(accountName)
    }

    override fun onCharacterPicked(characterName: String) {
        session.saveCharacter(characterName)
        view?.navigateToInventory()
    }

    override fun onBind() {
        getSessionAccount()
    }

    private fun getSessionAccount() {
        if (session.getAccount() != "default") {
            getCharactersFromRepo(session.getAccount()!!)
        } else view?.showDefaultScreen()
    }

    private fun getCharactersFromRepo(accountName: String){
        view?.showProgressBar(true)
        compositeDisposable.add(repository.getAccountData(accountName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.showCharacterList(it)
                view?.showCurrentAccount(accountName)
                view?.showProgressBar(false)
                session.saveAccount(accountName)
            },{
                view?.showProgressBar(false)
                view?.showError(it.localizedMessage)
            }))
    }
}