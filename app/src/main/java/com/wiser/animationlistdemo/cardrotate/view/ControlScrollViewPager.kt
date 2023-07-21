package com.wiser.animationlistdemo.cardrotate.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ControlScrollViewPager @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
) : ViewPager(context, attrs) {

    private var isCanScroll = true

    fun setCanScroll(isCanScroll: Boolean) {
        this.isCanScroll = isCanScroll
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onTouchEvent(ev)
    }
}