package com.example.poeproladder.ui.myaccount

import com.example.poeproladder.BaseContract
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.ui.BaseFragmentPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyAccountPresenter(
    view: MyAccountContract.MyAccountView,
    private val repository: CharactersRepository
) : BaseFragmentPresenter<MyAccountContract.MyAccountView>(view), MyAccountContract.MyAccountPresenter {


    override fun getCharacters(accountName: String) {
        compositeDisposable.add(repository.getAccountData(accountName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showCharacterList(it)
            },{
                view.showError()
            }))
    }

    override fun getItems(): Observable<CharacterItemsDb> {
        TODO("not implemented")
    }

    override fun loadItemIcons() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun attachView(view: BaseContract.BaseView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBind() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}