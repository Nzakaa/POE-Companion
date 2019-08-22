package com.example.poeproladder.ui.skillgems

import com.example.poeproladder.database.asSkillGemsLinks
import com.example.poeproladder.domain.SkillGemsLinks
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.ui.BaseFragmentPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SkillGemsPresenter @Inject constructor(
    view: SkillGemsContract.SkillGemsView,
    val repository: CharactersRepository
) : BaseFragmentPresenter<SkillGemsContract.SkillGemsView>(), SkillGemsContract.SkillGemsPresenter {

    init {
        this.view = view
    }


    override fun detachView() {
        view = null
    }

    override fun onBind() {
        val currentCharacterItems = repository.getItemsObservable()
        val skillGemsInfo = currentCharacterItems.map { items -> items.asSkillGemsLinks() }

        compositeDisposable.add(skillGemsInfo
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            view?.showSkillGems(it)
        }, { error ->
            showError(error.localizedMessage)
        }))
    }

    override fun onStop() {
//        TODO
    }
}