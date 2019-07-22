package com.example.poeproladder

interface BaseContract {
    interface BasePresenter<in T> {
        fun attachView(view: T)
        fun detachView()
        fun onBind()
        fun onStop()
    }

    interface BaseView {
        fun showError()
    }
}