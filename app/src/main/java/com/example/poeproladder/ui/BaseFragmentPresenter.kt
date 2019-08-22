package com.example.poeproladder.ui

import com.example.poeproladder.BaseContract
import io.reactivex.disposables.CompositeDisposable

open class BaseFragmentPresenter<T:BaseContract.BaseView> : BaseContract.BasePresenter{
    var view:T? = null
    val compositeDisposable = CompositeDisposable()

    override fun detachView() {
        this.view = null
    }

    override fun onStop() {
        compositeDisposable.clear()
    }

    fun showError(error: String) {
        view?.showError(error)
    }
}