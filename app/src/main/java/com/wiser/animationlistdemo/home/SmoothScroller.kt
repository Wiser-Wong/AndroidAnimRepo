package com.wiser.animationlistdemo.home

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * @date: 2023/6/7 18:42
 * @author: jiaruihua
 * @desc :
 *
 */
class SmoothScroller(context: Context):LinearSmoothScroller(context) {

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
        return if (displayMetrics==null) super.calculateSpeedPerPixel(displayMetrics) else 100f/displayMetrics.densityDpi
    }
}