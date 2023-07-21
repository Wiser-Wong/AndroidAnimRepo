package com.wiser.animationlistdemo

import android.app.Application
import android.content.Context

/**
 *
 * @Author qingyang
 * 邮箱：qingyang@ksjgs.com
 * 创建时间: 2023/6/13  19:02
 * 用途
 * **************************************
 */
class BaseApp: Application() {

    companion object {
        var sContext: Context? = null

        fun getContext() = sContext
    }

    override fun onCreate() {
        super.onCreate()
        sContext = this
    }
}