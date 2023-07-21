package com.wiser.animationlistdemo.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/6/2 17:11
 * @author: jiaruihua
 * @desc :
 *
 */
class DetailBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    private lateinit var content: View
    private var scroller: OverScroller? = null
    var tranx = 0F
    var beginX=0F

    private val scrollRunnable = object : Runnable {
        override fun run() {
            scroller?.let {
                if (it.computeScrollOffset()) {
                    content.translationY = it.currY.toFloat()
                    ViewCompat.postOnAnimation(content, this)
                }
            }
        }
    }


    private var cardBgHeight = 0
    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        tranx = child.resources.getDimension(R.dimen.ksl_dp_19)
        content = child
        beginX = child.x
        println("child------h=${child.height}--child.x=${child.x}---tranx=$tranx")
        parent.onLayoutChild(child, layoutDirection)
        cardBgHeight = (parent.findViewById<View>(R.id.cardBg).height * 0.5).toInt()
//        ViewCompat.offsetTopAndBottom(child, (cardBgHeight*(300/380f)).toInt())
        return true
    }

    private fun startAutoScroll(current: Int, target: Int, duration: Int) {
        if (scroller == null) {
            scroller = OverScroller(content.context)

        }

        scroller?.let {
            if (it.isFinished) {
                content.removeCallbacks(scrollRunnable)
                it.startScroll(0, current, 0, target - current, duration)
                ViewCompat.postOnAnimation(content, scrollRunnable)
            }
        }
    }

    fun stopAutoScroll() {
        scroller?.let {
            if (!it.isFinished) {
                it.abortAnimation()
                content.removeCallbacks(scrollRunnable)
            }
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        stopAutoScroll()
        child.setTag(R.id.scroll_tag, true)

        if (dy > 0) {
            val newDy = child.translationY - dy

            if (newDy >= -cardBgHeight) {
                // 完全消耗滑动距离后没有完全贴顶或刚好贴顶
                // 那么就声明消耗所有滑动距离，并上移 RecyclerView
                consumed[1] = dy // consumed[0/1] 分别用于声明消耗了x/y方向多少滑动距离
                child.translationY = newDy
            } else {
                // 如果完全消耗那么会导致 RecyclerView 超出可视区域
                // 那么只消耗恰好让 RecyclerView 贴顶的距离
                consumed[1] = cardBgHeight + child.translationY.toInt()
                child.translationY = -cardBgHeight.toFloat()
            }

            if (child.x>tranx){
                child.x--
            }
        }

    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        stopAutoScroll()
// 此时 RV 已经完成了滑动，dyUnconsumed 表示剩余未消耗的滑动距离
        super.onNestedScroll(
            coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            type, consumed
        )
        if (dyUnconsumed < 0) { // 只处理手指向下滑动的情况
            val newTransY = child.translationY - dyUnconsumed
            println("newTransY==------&$newTransY")
            if (newTransY <= 0) {
                child.translationY = newTransY
            } else {
                child.translationY = 0f
            }

            if (child.x<beginX){
                child.x++
            }
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)

        println("--------------------onStopNestedScroll")


        if (child.translationY >= 0 || child.translationY <= -cardBgHeight) {
            return
        }

        if (child.translationY <= -cardBgHeight * 0.5f) {
            stopAutoScroll()
            startAutoScroll(child.translationY.toInt(), -cardBgHeight, 1000)
        } else {
            stopAutoScroll()
            startAutoScroll(child.translationY.toInt(), 0, 600)
        }

    }

    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) {
        super.onNestedScrollAccepted(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )

        println("--------------------onNestedScrollAccepted")
    }
}