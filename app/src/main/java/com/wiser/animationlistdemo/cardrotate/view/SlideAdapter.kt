package com.wiser.animationlistdemo.cardrotate.view

import android.view.View
import android.view.ViewGroup

/**
 * @param <T>
 * @author Wiser
 */
internal interface SlideAdapter<T> {

    val counts: Int

    fun getItem(position: Int): T

    fun getItemView(viewGroup: ViewGroup?, position: Int): View?
}