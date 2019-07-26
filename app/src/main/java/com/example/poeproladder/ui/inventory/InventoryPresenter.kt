package com.example.poeproladder.ui.inventory

import com.example.poeproladder.BaseApp
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.session.SessionServiceImpl
import com.example.poeproladder.ui.BaseFragmentPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class InventoryPresenter(
    view: InventoryContract.InventoryView,
    private val repository: CharactersRepository
) : BaseFragmentPresenter<InventoryContract.InventoryView>(view), InventoryContract.InventoryPresenter {

    private val session = SessionServiceImpl.getInstance(BaseApp.applicationContext())

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBind() {
        val character = session.getCharacter()
        val account = session.getAccount()
        if (character != null && account != null) getItemsFromRepo(account, character)
    }

    override fun getItems() {
        val account = session.getAccount()
        compositeDisposable.add(session.getCharacterObservable()
            .subscribe { character ->
                if (account != null) getItemsFromRepo(account, character)
            })
    }

    private fun getItemsFromRepo(accountName: String, characterName: String) {
        compositeDisposable.add(repository.getItemsByName(accountName, characterName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                view?.showItems(items)
            }, {
                view?.showError(it.localizedMessage)
            }
            ))
    }
}