package com.example.poeproladder.dagger.components

import com.example.poeproladder.dagger.modules.SkillGemsModule
import com.example.poeproladder.ui.skillgems.SkillGemsFragment
import dagger.Subcomponent

@Subcomponent(modules = [SkillGemsModule::class])
interface SkillGemsComponent {
    fun inject(fragment: SkillGemsFragment)
}