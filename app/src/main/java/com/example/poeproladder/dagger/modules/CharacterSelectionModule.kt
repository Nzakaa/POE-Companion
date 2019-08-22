package com.example.poeproladder.dagger.modules

import com.example.poeproladder.ui.characterselection.CharacterSelectionContract
import com.example.poeproladder.ui.characterselection.CharacterSelectionPresenter
import dagger.Module
import dagger.Provides

@Module
class CharacterSelectionModule(val view: CharacterSelectionContract.MyAccountView) {

    @Provides
    fun providesView(): CharacterSelectionContract.MyAccountView {
        return view
    }

    @Provides
    fun providesPresenter(presenter: CharacterSelectionPresenter): CharacterSelectionContract.CharacterSelectionPresenter {
        return presenter
    }
}