package com.example.poeproladder.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeActivity : AppCompatActivity() {

    //TODO Keep track of all disposables
    var compositeDisposable = CompositeDisposable()

    private lateinit var database: CharacterDatabase

    private lateinit var textMessage: TextView
    private lateinit var accountButton: Button
    private lateinit var itemsButton: Button
    private lateinit var accountDbButton: Button
    private lateinit var itemsDbButton: Button
    private lateinit var clearDbButton: Button

    private lateinit var characterList: List<CharactersDb>

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.recent_characters)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.account)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.ladder)
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

        accountButton = findViewById<Button>(R.id.button_account).apply {
            setOnClickListener { fetchAccountApi("nzaka") }
        }

        accountDbButton = findViewById<Button>(R.id.button_account_db).apply {
            setOnClickListener {
                textMessage.text = "parsing DB"
                getCharactersFromDbRx()
            }
        }

        itemsButton = findViewById<Button>(R.id.button_items).apply {
            setOnClickListener { fetchCharacterItemsApi() }
        }

        itemsDbButton = findViewById<Button>(R.id.button_items_db).apply {
            setOnClickListener { fetchItemsFromDbRx("vvideHardo") }
        }

        clearDbButton = findViewById<Button>(R.id.button_clear_db).apply {
            setOnClickListener { wipeDatabase() }
        }

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
                val builder = StringBuilder()
                for (item in result) {
                    builder.append("${item.name}\n")
                }
                textMessage.text = builder.toString()
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
        val disposable = accountApi.getCharacterInfo("nzaka", "vvideHardo")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Log.d("Result", "Successful call = ${result.items.size}")
                inserItemsIntoDbRx(result)
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
        accountName: String
    ) {
        val characters = result.map {
            it.asDatabaseModel(accountName)
        }
        Completable.fromAction {
            database.characterDao.insertCharacters(characters)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getCharactersFromDbRx() {
        val disposable = database.characterDao.getCharacters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                when {
                    result.isEmpty() -> textMessage.text = "Database is empty"
                    else -> {
                        val builder = StringBuilder()
                        for (item in result) {
                            builder.append("${item.characterName}\n")
                        }
                        textMessage.text = builder.toString()
                        characterList = result
                    }
                }

            }, { error ->
                error.printStackTrace()
                textMessage.text = error.printStackTrace().toString()
            })
        compositeDisposable.add(disposable)
    }

    fun getCharacterFromDbRx(): Long? {
        var characterId: Long? = null
        val disposable = database.characterDao.getCharacter("vvideHardo")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                textMessage.text = "name = ${result.characterName}, id = ${result.id}"
                characterId = result.id
            }, { error ->
                error.printStackTrace()
                textMessage.text = error.printStackTrace().toString()
            })
        compositeDisposable.add(disposable)
        return characterId
    }

    fun wipeDatabase() {
        val disposable = Completable.fromAction {
            database.characterDao.deleteAll()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                textMessage.text = "Database cleared"
            }, {
                it.printStackTrace()
                textMessage.text = it.printStackTrace().toString()
            })

        val disposable2 = Completable.fromAction {
            database.itemsDao.deleteAll()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                textMessage.text = "Database cleared"
            }, {
                it.printStackTrace()
                textMessage.text = it.printStackTrace().toString()
            })

        compositeDisposable.add(disposable)
    }

    fun inserItemsIntoDbRx(items: CharacterWindowItemsJson) {
        var characterId: Long? = null
        for (character in characterList) {
            if (character.characterName == "vvideHardo") characterId = character.id
        }

        val itemsDb = items.asDatabaseModel(characterId)
        val disposable = Completable.fromAction {
            database.itemsDao.insertItems(itemsDb)
        }.subscribeOn(Schedulers.io())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    fun fetchItemsFromDbRx(characterName: String) {
        val disposable = database.itemsDao.getItems(characterName)
//        val disposable = database.itemsDao.getItemsAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val builder = StringBuilder()
                    for (item in result.characterItems) {
                        builder.append("${item.name}\n")
                    }
                    textMessage.text = builder.toString()
                },
                { error ->
                    error.printStackTrace()
                })
        compositeDisposable.add(disposable)
    }


//
}
