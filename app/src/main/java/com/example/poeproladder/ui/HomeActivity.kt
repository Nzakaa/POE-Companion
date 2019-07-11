package com.example.poeproladder.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.poeproladder.R
import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.database.CharactersDb
import com.example.poeproladder.database.getDatabase
import com.example.poeproladder.network.CharacterWindowCharacterJson
import com.example.poeproladder.network.CharacterWindowItemsJson
import com.example.poeproladder.network.Network
import com.example.poeproladder.network.asDatabaseModel
import com.example.poeproladder.util.BuildConfig.LEAGUE
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    var compositeDisposable = CompositeDisposable()
    lateinit var database: CharacterDatabase

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
                fetchAccountApi("nzaka")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.ladder)
                database.characterDao.getCharacters()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({result ->
                        textMessage.setText(result.size.toString())
                    },{ error ->
                        error.printStackTrace()
                    })
//                val dbQuery = getCharactersFromDbRx()
//                textMessage.text = dbQuery.size.toString()
//                fetchLadderApi()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = getDatabase(application)
        wipeDatabase()

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

    fun fetchAccountApi(accountName: String) {
        val accountApi = Network.characterApi
        val disposable = accountApi.getAccountInfo(accountName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Log.d("Result", "Successful call with total ladder positions = ${result.size}")
                textMessage.text = result[1].name
                insertCharactersIntoDbRx(result, accountName)
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
//                val character = result
//                database.characterDao.insertCharacter(character.asDatabaseModel())
//                val responseDb = database.characterDao.getCharacters()
//                textMessage.text = "Character database size: ${responseDb.size}"
            }, { error ->
                error.printStackTrace()
                Log.d("Result", "Successful call with total ladder positions = 0")
                textMessage.setText("Error")
            })

        compositeDisposable.add(disposable)
    }

    fun insertCharactersIntoDbRx(
        result: List<CharacterWindowCharacterJson>,
        accountName:String
    ){
        val characters = result.map {
            it.asDatabaseModel(accountName)
        }
        Completable.fromAction {
            database.characterDao.insertCharacters(characters)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getCharactersFromDbRx(): List<CharactersDb>{
        var characters: List<CharactersDb> = ArrayList()
        val disposable = database.characterDao.getCharacters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({result ->
                characters = result
            },{ error ->
                error.printStackTrace()
            })
        return characters
    }

    fun wipeDatabase() {
        Completable.fromAction {
            database.characterDao.deleteAll()
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

//
}
