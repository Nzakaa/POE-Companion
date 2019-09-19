package com.example.poeproladder.ui.inventory

import com.example.poeproladder.BaseContract
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.ItemDb

interface InventoryContract {
    interface InventoryView : BaseContract.BaseView {
        fun showItems(items :HashMap<String, ItemDb>)
        fun showCharacterInfo(character: CharacterDb)
        fun showItem(item: ItemDb)
        fun showProgressBar(show: Boolean)
    }

    interface InventoryPresenter : BaseContract.BasePresenter {
        fun openItemInfo(item: ItemDb)
        fun onBind()
    }
}