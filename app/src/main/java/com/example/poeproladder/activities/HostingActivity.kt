package com.example.poeproladder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.poeproladder.R
import com.example.poeproladder.activities.adapters.PagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_hosting.*

class HostingActivity : AppCompatActivity() {

    val TAG = "HostingActivity"


//    private lateinit var characterList: List<CharacterDb>
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosting)

        initViews()
    }

    private fun initViews() {
        viewPager = view_pager
        tabLayout = tab_layout
        val pagerAdapter = PagerAdapter(this, supportFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2
        tabLayout.setupWithViewPager(viewPager, true)
    }

    fun navigateToPage(page: Int) {
        viewPager.currentItem = page
    }

    companion object {

        const val CHARACTERPAGE = 0
        const val INVENTORYPAGE = 1
        const val PASSIVESPAGE = 2
    }
}


//    fun fetchLadderApi() {
//        val ladderApi = Network.ladderApi
//        val disposable = ladderApi.getLadder(LEAGUE, 10)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ result ->
//                Log.d("Result", "Successful call with total ladder positions = ${result.total}")
//                textMessage.setText(result.total.toString())
//            }, { error ->
//                error.printStackTrace()
//                Log.d("Result", "Successful call with total ladder positions = 0")
//                textMessage.setText(error.printStackTrace().toString())
//            })
//
//        compositeDisposable.add(disposable)
//    }
//
//    fun fetchAccountApi(accountName: String) {
//        val accountApi = Network.characterApi
//        val disposable = accountApi.getAccountInfo(accountName)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ result ->
//                Log.d("Result", "Successful call with total ladder positions = ${result.size}")
//                val builder = StringBuilder()
//                for (item in result) {
//                    builder.append("${item.name}\n")
//                }
//                textMessage.text = builder.toString()
//                insertCharactersIntoDbRx(result, accountName)
//            }, { error ->
//                error.printStackTrace()
//                Log.d("Result", "Successful call with total ladder positions = 0")
//                textMessage.setText("Error")
//            })
//
//        compositeDisposable.add(disposable)
//    }
//
//    fun fetchCharacterItemsApi() {
//        val accountApi = Network.characterApi
//        val disposable = accountApi.getCharacterInfo("nzaka", "vvideHardo")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ result ->
//                Log.d("Result", "Successful call = ${result.items.size}")
//                textMessage.text = "Items count: ${result.items.size}"
//                insertItemsIntoDbRx(result)
//            }, { error ->
//                error.printStackTrace()
//                Log.d("Result", "Error during network call")
//                textMessage.setText("Error")
//            })
//
//        compositeDisposable.add(disposable)
//    }
//
//    fun insertCharactersIntoDbRx(
//        result: List<CharacterWindowCharacterJson>,
//        accountName: String
//    ) {
//        val characters = result.map {
//            it.asDatabaseModel(accountName)
//        }
//        Completable.fromAction {
//            database.characterDao.saveCharacters(characters)
//        }.subscribeOn(Schedulers.io())
//            .subscribe()
//    }
//
//    fun getCharactersFromDbRx() {
//        val disposable = database.characterDao.getRecentCharacters()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ result ->
//                when {
//                    result.isEmpty() -> textMessage.text = "Database is empty"
//                    else -> {
//                        val builder = StringBuilder()
//                        for (item in result) {
//                            builder.append("${item.characterName}\n")
//                        }
//                        textMessage.text = builder.toString()
//                        characterList = result
//                    }
//                }
//
//            }, { error ->
//                error.printStackTrace()
//                textMessage.text = error.printStackTrace().toString()
//            })
//        compositeDisposable.add(disposable)
//    }
//
////    fun getCharacterFromDbRx(): Long? {
////        var characterId: Long? = null
////        val disposable = database.characterDao.getAccountCharacters("vvideHardo")
////            .subscribeOn(Schedulers.io())
////            .observeOn(AndroidSchedulers.mainThread())
////            .subscribe({ result ->
////                textMessage.text = "name = ${result.characterName}, id = ${result.id}"
////                characterId = result.id
////            }, { error ->
////                error.printStackTrace()
////                textMessage.text = error.printStackTrace().toString()
////            })
////        compositeDisposable.add(disposable)
////        return characterId
////    }
//
//    fun wipeDatabase() {
//        val disposable = Completable.fromAction {
//            database.characterDao.deleteAllCharacters()
//        }.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                textMessage.text = "Database cleared"
//            }, {
//                it.printStackTrace()
//                textMessage.text = it.printStackTrace().toString()
//            })
//
//        val disposable2 = Completable.fromAction {
//            database.itemsDao.deleteAllItems()
//        }.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                textMessage.text = "Database cleared1"
//            }, {
//                it.printStackTrace()
//                textMessage.text = it.printStackTrace().toString()
//            })
//
//        compositeDisposable.add(disposable)
//        compositeDisposable.add(disposable2)
//    }
//
//    fun insertItemsIntoDbRx(items: CharacterWindowItemsJson) {
//        var characterName: String = "vvideHardo"
//
//        val itemsDb = items.asDatabaseModel(characterName)
//        val disposable = Completable.fromAction {
//            database.itemsDao.saveItems(itemsDb)
//        }.subscribeOn(Schedulers.io())
//            .subscribe()
//        compositeDisposable.add(disposable)
//    }
//
//    fun fetchItemsFromDbRx(characterName: String) {
//        val disposable = database.itemsDao.getItemsByName(characterName)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { result ->
//                    val builder = StringBuilder()
//                    for (item in result.characterItems) {
//                        if (builder.isNotEmpty()) builder.append(", ")
//                        when {
//                            item.name == "" -> builder.append("${item.base}")
//                            else -> builder.append("${item.name} ${item.base}")
//                        }
//                    }
//                    textMessage.text = builder.toString()
//                },
//                { error ->
//                    error.printStackTrace()
//                })
//        compositeDisposable.add(disposable)
//    }


//

