package com.example.poeproladder.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.poeproladder.R
import com.example.poeproladder.network.Network
import com.example.poeproladder.util.BuildConfig.LEAGUE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView

    var compositeDisposable = CompositeDisposable()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                apiRequest()
                apiCallRequest()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun apiRequest() {
        val ladder = Network.gggApi
        val disposable = ladder.getLadder(LEAGUE, 10)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                result ->
                Log.d("Result", "Successful call with total ladder positions = ${result.total}")
            }, {
                error ->
                error.printStackTrace()
                Log.d("Result", "Successful call with total ladder positions = 0")
            })

        compositeDisposable.add(disposable)
    }

    fun apiCallRequest() {
        val ladder = Network.gggApi
        val disposable = ladder.getLadder(LEAGUE, 10)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                    result ->
                Log.d("Result", "Successful call with total ladder positions = ${result.total}")
            }, {
                    error ->
                error.printStackTrace()
                Log.d("Result", "Successful call with total ladder positions = 0")
            })

        compositeDisposable.add(disposable)
    }
}
