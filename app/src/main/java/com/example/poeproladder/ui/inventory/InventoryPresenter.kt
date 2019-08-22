package com.example.poeproladder.ui.inventory

import com.example.poeproladder.BaseApp
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.database.ItemDb
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.session.SessionService
import com.example.poeproladder.session.SessionServiceImpl
import com.example.poeproladder.ui.BaseFragmentPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class InventoryPresenter @Inject constructor(
    view: InventoryContract.InventoryView,
    val repository: CharactersRepository
) : BaseFragmentPresenter<InventoryContract.InventoryView>(), InventoryContract.InventoryPresenter {

//    private val session = BaseApp.session
    @Inject lateinit var session: SessionService

    init {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun onBind() {
        val itemsObservable = session.getCharacterObservable()
        compositeDisposable.add(
            itemsObservable
                .subscribe({ character ->
                    if (character.characterName != "default" && character.accountName != "default")
                        getItemsFromRepo(character.accountName, character.characterName)
                }, { error ->
                    view?.showError(error.localizedMessage)
                }))
    }

    override fun openItemInfo(item: ItemDb) {
        view?.let { it.showItem(item) }
    }


    private fun getItemsFromRepo(accountName: String, characterName: String) {
        compositeDisposable.add(repository.getItemsByName(accountName, characterName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                view?.showCharacterInfo(items.character)
                view?.showItems(convertResponseToItemsDomain(items))
            }, {
                view?.showError(it.localizedMessage)
            }
            ))
    }

    private fun convertResponseToItemsDomain(characterItemsDb: CharacterItemsDb)
            : HashMap<String, ItemDb> {
        var itemInventory: HashMap<String, ItemDb> = HashMap()
        for (item in characterItemsDb.characterItems) {
            when {
                item.inventoryId == "Weapon" -> itemInventory["weapon"] = item
                item.inventoryId == "Helm" -> itemInventory["helm"] = item
                item.inventoryId == "Amulet" -> itemInventory["amulet"] = item
                item.inventoryId == "Gloves" -> itemInventory["gloves"] = item
                item.inventoryId == "Ring2" -> itemInventory["ring2"] = item
                item.inventoryId == "Boots" -> itemInventory["boots"] = item
                item.inventoryId == "Ring" -> itemInventory["ring"] = item
                item.inventoryId == "BodyArmour" -> itemInventory["bodyarmour"] = item
                item.inventoryId == "Belt" -> itemInventory["belt"] = item
                item.inventoryId == "Offhand" -> itemInventory["offhand"] = item
                item.inventoryId == "Flask" && item.x == 0 -> itemInventory["flask1"] = item
                item.inventoryId == "Flask" && item.x == 1 -> itemInventory["flask2"] = item
                item.inventoryId == "Flask" && item.x == 2 -> itemInventory["flask3"] = item
                item.inventoryId == "Flask" && item.x == 3 -> itemInventory["flask4"] = item
                item.inventoryId == "Flask" && item.x == 4 -> itemInventory["flask5"] = item
            }
        }
        return itemInventory
    }
}