package com.example.poeproladder

interface BaseContract {
    interface BasePresenter {
        fun detachView()
        fun onStop()
    }

    interface BaseView {
        fun showError(error: String)
    }
}