package com.wiser.animationlistdemo.search.listener

abstract class JellyListener {

    open fun onToolbarExpandingStarted() = Unit

    open fun onToolbarCollapsingStarted() = Unit

    open fun onToolbarExpanded() = Unit

    open fun onToolbarCollapsed() = Unit

    abstract fun onCancelIconClicked()

}