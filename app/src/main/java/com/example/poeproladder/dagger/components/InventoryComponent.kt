package com.example.poeproladder.dagger.components

import com.example.poeproladder.dagger.modules.InventoryModule
import com.example.poeproladder.ui.inventory.InventoryFragment
import dagger.Subcomponent

@Subcomponent(modules = [InventoryModule::class])
interface InventoryComponent {
    fun inject(fragment: InventoryFragment)
}