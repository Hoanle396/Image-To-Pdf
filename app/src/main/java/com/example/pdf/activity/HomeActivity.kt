package com.example.pdf.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pdf.R
import com.google.android.material.tabs.TabLayout


class HomeActivity : AppCompatActivity()
{
    lateinit var viewPager:ViewPager2
    lateinit var tabLayout:TabLayout

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager=findViewById(R.id.viewPager)
        tabLayout=findViewById(R.id.tabLayout)
        setUpTabBar()
    }
    private fun setUpTabBar()
    {
        val adapter = TabPageAdapter(this, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}