package com.example.poeproladder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.poeproladder.R
import com.example.poeproladder.activities.adapters.PagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_hosting.*

class HostingActivity : AppCompatActivity() {

    val TAG = "HostingActivity"


//    private lateinit var characterList: List<CharacterDb>
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

