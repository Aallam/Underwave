package com.aallam.underwave.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.aallam.underwave.sample.sample.ListFragment
import com.aallam.underwave.sample.sample.SingleFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.viewPager = findViewById(R.id.viewPager)
        this.viewPager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)
    }

    override fun onBackPressed() {
        when (viewPager.currentItem) {
            0 -> super.onBackPressed()
            else -> viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    private class ScreenSlidePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        val fragments = listOf(SingleFragment::class.java, ListFragment::class.java)
        override fun getCount(): Int = fragments.size
        override fun getItem(position: Int): Fragment = fragments[position].newInstance()
    }
}
