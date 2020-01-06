package com.aallam.underwave.sample.extension

import androidx.viewpager.widget.ViewPager

fun ViewPager.setOnPageSelectedListener(block: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            // Empty
        }

        override fun onPageSelected(position: Int) {
            block(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            // Empty
        }
    })
}
