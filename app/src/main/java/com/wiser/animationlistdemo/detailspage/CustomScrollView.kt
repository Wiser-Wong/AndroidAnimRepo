package com.wiser.animationlistdemo.detailspage

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.core.widget.NestedScrollView
import kotlin.math.abs

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/21     18:46
 * 用途: 更新说明
 ***************************************
 */
class CustomScrollView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private var touchX = 0f

    private var touchY = 0f

    /**
     * 上次滑动的距离
     */
    private var lastDownY = 0f

    private var mScrollPointerId: Int = -1

    private val mTouchSlop by lazy { ViewConfiguration.get(getContext()).scaledTouchSlop }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val actionIndex = event?.actionIndex ?: 0
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mScrollPointerId = event.getPointerId(0)
                touchX = event.rawX
                touchY = event.rawY
                lastDownY = event.rawY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mScrollPointerId = event.getPointerId(actionIndex)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    touchX = event.getRawX(event.findPointerIndex(mScrollPointerId))
                    touchY = event.getRawY(event.findPointerIndex(mScrollPointerId))
                    lastDownY = event.getRawY(event.findPointerIndex(mScrollPointerId))
                } else {
                    touchX = event.rawX
                    touchY = event.rawY
                    lastDownY = event.rawY
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (ScrollingUtil.isScrollViewToBottom(this)) {
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val index = event.findPointerIndex(mScrollPointerId)
                if (index < 0) return false
                val rawY = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    event.getRawY(index)
                } else {
                    event.rawY
                }
                val rawX = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    event.getRawX(index)
                } else {
                    event.rawX
                }
                if (abs(rawY - touchY) < mTouchSlop || (abs(rawX - touchX) > abs(rawY - touchY))) {
                    return super.dispatchTouchEvent(event)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}