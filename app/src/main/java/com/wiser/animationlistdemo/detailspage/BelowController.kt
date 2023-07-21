package com.wiser.animationlistdemo.detailspage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/2     15:33
 * 用途: 更新说明
 ***************************************
 */
class BelowController @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 列表控件 用于处理滑动冲突
     */
    private var scrollView: View? = null

    private var isDispatch = false

    fun setDispatch(isDispatch: Boolean) {
        this.isDispatch = isDispatch
    }

    fun isDispatch(): Boolean = isDispatch

    fun isScrollBottom(): Boolean {
        if (this.scrollView is NestedScrollView) {
            return ScrollingUtil.isScrollViewToBottom(this.scrollView as? NestedScrollView)
        }
        if (this.scrollView is ScrollView) {
            return ScrollingUtil.isScrollViewToBottom(this.scrollView as? ScrollView)
        }
        return false
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                setDispatch(true)
            }
            MotionEvent.ACTION_UP -> {
                setDispatch(false)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    /**
     * 添加滚动控件
     */
    fun addScrollListView(view: View?) {
        this.scrollView = view
    }

    fun getScrollListView(): View? = scrollView

    fun scrollBy(dy: Int) {
        if (this.scrollView is NestedScrollView || this.scrollView is ScrollView || this.scrollView is RecyclerView) {
            this.scrollView?.scrollBy(0, dy)
        }
    }

}