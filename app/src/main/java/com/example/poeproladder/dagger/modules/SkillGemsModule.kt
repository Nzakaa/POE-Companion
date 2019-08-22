package com.example.poeproladder.dagger.modules

import com.example.poeproladder.ui.skillgems.SkillGemsContract
import com.example.poeproladder.ui.skillgems.SkillGemsPresenter
import dagger.Module
import dagger.Provides

@Module
class SkillGemsModule(val view: SkillGemsContract.SkillGemsView) {

    @Provides
    fun providesView(): SkillGemsContract.SkillGemsView {
        return view
    }

    @Provides
    fun providesPresenter(presenter: SkillGemsPresenter): SkillGemsContract.SkillGemsPresenter {
        return presenter
    }
}