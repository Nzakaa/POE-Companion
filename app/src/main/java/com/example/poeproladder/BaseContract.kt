package com.example.poeproladder

interface BaseContract {
    interface BasePresenter {
        fun detachView()
        fun onBind()
        fun onStop()
    }

    interface BaseView {
        fun showError(error: String)
    }
}