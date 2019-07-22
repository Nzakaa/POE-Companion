package com.example.poeproladder.activities.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.poeproladder.R
import com.example.poeproladder.ui.ladder.LadderFragment
import com.example.poeproladder.ui.myaccount.MyAccountFragment
import com.example.poeproladder.ui.recent.RecentFragment

class PagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> RecentFragment.newInstance()
            1 -> MyAccountFragment.newInstance()
            2 -> LadderFragment.newInstance()
            else -> throw IllegalArgumentException("Position: ${position} is out of range")
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.recent_characters)
            1 -> context.getString(R.string.account)
            2 -> context.getString(R.string.ladder)
            else -> null
        }
    }
}