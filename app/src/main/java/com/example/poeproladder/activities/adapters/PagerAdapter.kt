package com.example.poeproladder.activities.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.poeproladder.R
import com.example.poeproladder.ui.ladder.PassiveTreeFragment
import com.example.poeproladder.ui.characterselection.CharacterSelectionFragment
import com.example.poeproladder.ui.inventory.InventoryFragment

class PagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CharacterSelectionFragment.newInstance()
            1 -> InventoryFragment.newInstance()
            2 -> PassiveTreeFragment.newInstance()
            else -> throw IllegalArgumentException("Position: ${position} is out of range")
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.character)
            1 -> context.getString(R.string.inventory)
            2 -> context.getString(R.string.passives)
            else -> null
        }
    }
}