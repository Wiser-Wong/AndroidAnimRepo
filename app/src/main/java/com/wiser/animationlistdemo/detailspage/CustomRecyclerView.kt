package com.wiser.animationlistdemo.detailspage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/6     16:41
 * 用途: 更新说明
 ***************************************
 */
class CustomRecyclerView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var touchX = 0f

    private var touchY = 0f

    private val mTouchSlop by lazy { ViewConfiguration.get(getContext()).scaledTouchSlop }

    private var isDispatch = false

    fun setDispatch(isDispatch: Boolean) {
        this.isDispatch = isDispatch
    }

    fun isDispatch(): Boolean = isDispatch

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        println("============above===>>${event?.actionMasked}======customRecyclerView")
        when(event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                this.isDispatch = true
                touchX = event.rawX
                touchY = event.rawY
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.rawX
                val endY = event.rawY
                val disX = abs(endX - touchX)
                val disY = abs(endY - touchY)
                if (disX > mTouchSlop) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(true)//下拉的时候是false
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                this.isDispatch = false
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun isScrollTop(): Boolean {
        return ScrollingUtil.isRecyclerViewToTop(this)
    }

}