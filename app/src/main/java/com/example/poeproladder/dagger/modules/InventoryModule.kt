package com.example.poeproladder.dagger.modules

import com.example.poeproladder.ui.inventory.InventoryContract
import com.example.poeproladder.ui.inventory.InventoryPresenter
import dagger.Module
import dagger.Provides

@Module
class InventoryModule(val view: InventoryContract.InventoryView) {

    @Provides
    fun providesView(): InventoryContract.InventoryView {
        return view
    }

    @Provides
    fun providesPresenter(presenter: InventoryPresenter): InventoryContract.InventoryPresenter {
        return presenter
    }
}