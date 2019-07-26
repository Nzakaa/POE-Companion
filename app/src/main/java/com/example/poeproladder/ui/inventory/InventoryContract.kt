package com.example.poeproladder.ui.inventory

import com.example.poeproladder.BaseContract
import com.example.poeproladder.database.CharacterItemsDb

interface InventoryContract {
    interface InventoryView : BaseContract.BaseView {
        fun showItems(items :CharacterItemsDb)
    }

    interface InventoryPresenter : BaseContract.BasePresenter {
        fun getItems()
    }
}