package com.example.poeproladder.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.poeproladder.R
import com.example.poeproladder.network.CharacterWindowItemsJson
import com.example.poeproladder.network.Network
import com.example.poeproladder.util.BuildConfig.LEAGUE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView

    var compositeDisposable = CompositeDisposable()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.recent_characters)
                fetchCharacterItemsApi()
//                fetchCharacterItemsApiCall()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.account)
                fetchAccountApi()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.ladder)
                Log.d("Result", "Successful call with total ladder positions = 0")
                fetchLadderApi()
//                apiCallRequest()
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

    fun fetchLadderApi() {
        val ladderApi = Network.ladderApi
        val disposable = ladderApi.getLadder(LEAGUE, 10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Log.d("Result", "Successful call with total ladder positions = ${result.total}")
                textMessage.setText(result.total.toString())
            }, { error ->
                error.printStackTrace()
                Log.d("Result", "Successful call with total ladder positions = 0")
                textMessage.setText(error.printStackTrace().toString())
            })

        compositeDisposable.add(disposable)
    }

    fun fetchAccountApi() {
        val accountApi = Network.characterApi
        val disposable = accountApi.getAccountInfo("nzaka")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Log.d("Result", "Successful call with total ladder positions = ${result.size}")
                textMessage.text = result[0].name
            }, { error ->
                error.printStackTrace()
                Log.d("Result", "Successful call with total ladder positions = 0")
                textMessage.setText("Error")
            })

        compositeDisposable.add(disposable)
    }

    fun fetchCharacterItemsApi() {
        val accountApi = Network.characterApi
        val disposable = accountApi.getCharacterInfo( "nzaka", "vvideHardo")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Log.d("Result", "Successful call = ${result.items.size}")
                textMessage.text = result.items.size.toString()
            }, { error ->
                error.printStackTrace()
                Log.d("Result", "Successful call with total ladder positions = 0")
                textMessage.setText("Error")
            })

        compositeDisposable.add(disposable)
    }

    fun fetchCharacterItemsApiCall() {
        val accountApi = Network.characterApi
        val call = accountApi.getCharacterInfoCall("vvideHardo")

        call.enqueue(object : Callback<CharacterWindowItemsJson> {
            override fun onFailure(call: Call<CharacterWindowItemsJson>, t: Throwable) {
                t.printStackTrace()
                textMessage.setText("Error")
            }

            override fun onResponse(
                call: Call<CharacterWindowItemsJson>,
                response: Response<CharacterWindowItemsJson>
            ) {
                textMessage.text = response.body()!!.items[1].sockets.size.toString()
            }
        })

    }

//
}
