package com.example.poeproladder.dagger.components

import com.example.poeproladder.BaseApp
import com.example.poeproladder.dagger.modules.*
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class, NetworkModule::class, DatabaseModule::class, RepositoryModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(baseApp: BaseApp)
    fun plus(module: CharacterSelectionModule): CharacterSelectionComponent
    fun plus(module: InventoryModule): InventoryComponent
    fun plus(module: SkillGemsModule): SkillGemsComponent
}
