package com.example.poeproladder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.poeproladder.R
import com.example.poeproladder.activities.adapters.PagerAdapter
import com.example.poeproladder.ui.characterselection.FragmentNotVisible
import com.example.poeproladder.util.hideKeyboard
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_hosting.*

class HostingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosting)

        initViews()
    }

    private fun initViews() {
        viewPager = view_pager
        tabLayout = tab_layout
        val pagerAdapter = PagerAdapter(this, supportFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2
        tabLayout.setupWithViewPager(viewPager, true)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                viewPager.hideKeyboard()
                val frag = pagerAdapter.instantiateItem(viewPager, position)
                if (frag is FragmentNotVisible) frag.notVisible()
            }
        })
    }

    override fun onBackPressed() {
        if (viewPager.currentItem != CHARACTERPAGE)
            navigateToPage(CHARACTERPAGE)
        else
            super.onBackPressed()
    }

    fun navigateToPage(page: Int) {
        viewPager.currentItem = page
    }

    companion object {
        const val CHARACTERPAGE = 0
        const val INVENTORYPAGE = 1
        const val PASSIVESPAGE = 2
    }
}

