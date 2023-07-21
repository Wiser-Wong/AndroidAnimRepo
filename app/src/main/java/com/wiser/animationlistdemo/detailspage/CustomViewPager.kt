package com.wiser.animationlistdemo.detailspage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/26     14:06
 * 用途: 更新说明
 ***************************************
 */
class CustomViewPager @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null
) : ViewPager(context, attrs)  {

    private var touchX = 0f

    private var touchY = 0f

    private val mTouchSlop by lazy { ViewConfiguration.get(getContext()).scaledTouchSlop }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        println("============above===>>${event?.actionMasked}======CustomViewPager")
        when(event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.rawX
                touchY = event.rawY
                requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.rawX
                val endY = event.rawY
                val disX = abs(endX - touchX)
                val disY = abs(endY - touchY)
                requestDisallowInterceptTouchEvent(false)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.dispatchTouchEvent(event)
    }
}