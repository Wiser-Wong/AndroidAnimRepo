package com.wiser.animationlistdemo.loginanim

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 ***************************************
 * 项目名称:LoginPageAnimator
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/3/7     10:40
 * 用途: 更新说明
 ***************************************
 */
class BaseLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var cacheCanvas: Canvas? = null
    private var isDrawToCache = false

    override fun draw(canvas: Canvas?) {
        if (isDrawToCache) {
            if (cacheCanvas != null) {
                cacheCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
                super.draw(cacheCanvas)
            }
        } else {
            super.draw(canvas)
        }
    }

    fun setCacheCanvas(cacheCanvas: Canvas?) {
        this.cacheCanvas = cacheCanvas
    }

    fun setDrawToCache(drawToCache: Boolean) {
        if (isDrawToCache != drawToCache) {
            isDrawToCache = drawToCache
            invalidate()
        }
    }
}