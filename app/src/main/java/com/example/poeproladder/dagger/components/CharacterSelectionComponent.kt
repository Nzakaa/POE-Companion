package com.example.poeproladder.dagger.components

import com.example.poeproladder.dagger.modules.CharacterSelectionModule
import com.example.poeproladder.ui.characterselection.CharacterSelectionFragment
import dagger.Subcomponent

@Subcomponent(modules = [CharacterSelectionModule::class])
interface CharacterSelectionComponent {
    fun inject(fragment: CharacterSelectionFragment)
}