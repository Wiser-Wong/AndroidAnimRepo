package com.wiser.animationlistdemo.cardrotate.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @param <T>
 * @author Wiser
 */
abstract class SlidePageAdapter<T> : SlideAdapter<T> {
    private var mInflater: LayoutInflater? = null
    private var context: Context? = null
    private var pageView: SlidePageView? = null
    fun initAdapter(
        pageView: SlidePageView?,
        context: Context?,
        inflater: LayoutInflater?
    ) {
        this.context = context
        mInflater = inflater
        this.pageView = pageView
    }

    fun context(): Context? {
        return context
    }

    fun inflater(viewGroup: ViewGroup?, id: Int): View? {
        return mInflater?.inflate(id, viewGroup, false)
    }

    fun notifyDataAdapter() {
        pageView?.notifyDataAdapter()
    }

    fun detach() {
        mInflater = null
        context = null
        pageView = null
    }
}