package com.wiser.animationlistdemo.search

import android.view.View
import androidx.annotation.DimenRes

fun View.getDimen(@DimenRes res: Int) = context.resources.getDimensionPixelOffset(res).toFloat()