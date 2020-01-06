package com.aallam.underwave.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.aallam.underwave.Underwave
import com.aallam.underwave.sample.extension.setOnPageSelectedListener
import com.aallam.underwave.sample.sample.ListFragment
import com.aallam.underwave.sample.sample.SingleFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.viewPager = findViewById(R.id.viewPager)
        this.viewPager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)
        setupNavigation()
    }

    private fun setupNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.single -> viewPager.currentItem = 0
                R.id.list -> viewPager.currentItem = 1
            }
            false
        }
        viewPager.setOnPageSelectedListener { position ->
            prevMenuItem?.setChecked(false) ?: bottomNavigationView.menu
                .getItem(0).setChecked(false)
            bottomNavigationView.menu.getItem(position).isChecked = true
            prevMenuItem = bottomNavigationView.menu.getItem(position)
        }
    }

    override fun onBackPressed() {
        when (viewPager.currentItem) {
            0 -> super.onBackPressed()
            else -> viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> Underwave.with(this).clear().also {
                Toast.makeText(this, R.string.toast_clear, Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private class ScreenSlidePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        val fragments = listOf(SingleFragment::class.java, ListFragment::class.java)
        override fun getCount(): Int = fragments.size
        override fun getItem(position: Int): Fragment = fragments[position].newInstance()
    }
}
