package com.example.poeproladder.ui.characterselection

import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.session.SessionService
import com.example.poeproladder.ui.BaseFragmentPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharacterSelectionPresenter @Inject constructor(
    view: CharacterSelectionContract.MyAccountView,
    val repository: CharactersRepository
//    val session: SessionService
) : BaseFragmentPresenter<CharacterSelectionContract.MyAccountView>(), CharacterSelectionContract.CharacterSelectionPresenter {

//    @Inject lateinit var repository: CharactersRepository
    @Inject lateinit var session: SessionService
//    private val session = BaseApp.session!!

    init {
        this.view = view
    }

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
        compositeDisposable.add(repository.getAccountData(accountName, session.getNetworkStatus())
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