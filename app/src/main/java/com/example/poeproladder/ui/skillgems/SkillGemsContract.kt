package com.example.poeproladder.ui.skillgems

import com.example.poeproladder.BaseContract
import com.example.poeproladder.domain.SkillGemsLinks
import com.example.poeproladder.ui.BaseFragmentPresenter

interface SkillGemsContract {
    interface SkillGemsView : BaseContract.BaseView {
        fun showSkillGems(links: List<SkillGemsLinks>)
    }

    interface SkillGemsPresenter : BaseContract.BasePresenter {
        fun onBind()
    }
}