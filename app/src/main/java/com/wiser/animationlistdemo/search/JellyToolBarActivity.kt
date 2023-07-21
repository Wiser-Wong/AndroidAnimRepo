package com.wiser.animationlistdemo.search

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.search.listener.JellyListener
import com.wiser.animationlistdemo.search.widget.JellyToolbar


class JellyToolBarActivity: AppCompatActivity() {

    private var toolbar: JellyToolbar? = null
    private var editText: AppCompatEditText? = null
    private val jellyListener: JellyListener = object : JellyListener() {
        override fun onCancelIconClicked() {
            if (TextUtils.isEmpty(editText?.text)) {
                toolbar?.collapse()
            } else {
                editText?.text?.clear()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)

        toolbar = findViewById<View>(R.id.toolbar) as JellyToolbar
        toolbar?.toolbar?.setNavigationIcon(R.drawable.ic_menu)
        toolbar?.jellyListener = jellyListener
        toolbar?.toolbar?.setPadding(0, getStatusBarHeight(), 0, 0)

        editText = LayoutInflater.from(this).inflate(R.layout.edit_text, null) as AppCompatEditText
        editText?.setBackgroundResource(0)
        toolbar?.contentView = editText

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}