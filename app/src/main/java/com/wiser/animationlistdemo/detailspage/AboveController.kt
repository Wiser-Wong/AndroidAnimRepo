package com.wiser.animationlistdemo.detailspage

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.core.animation.addListener
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.nineoldandroids.view.ViewHelper
import kotlin.math.abs


/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/2     15:33
 * 用途: 更新说明
 ***************************************
 */
class AboveController @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 屏幕高度
     */
    private var screenHeight: Int = ScreenTools.getScreenDPI(context)

    /**
     * 最小临界重叠距离
     */
    private var minEdgeOverlapDistance = 900

    /**
     * 上层视图居顶部距离
     */
    private var marginTopDistance = screenHeight - minEdgeOverlapDistance - 50

    /**
     * 打开时居顶部的距离是多少
     */
    private var marginTopDistanceForOpen = marginTopDistance - 250f

    /**
     * 上层视图滑动的最大距离，松手打开或者关闭
     */
    private var scrollMaxDistance = marginTopDistance / 2f

    private var touchX = 0f

    private var touchY = 0f

    /**
     * 二楼开关状态
     */
    private var aboveLayoutToggleState = ABOVE_CLOSE

    /**
     * 是否拦截该控件
     */
    private var isIntercept = true

    /**
     * 列表控件 用于处理滑动冲突
     */
    private var listView: View? = null

    private var mapListView: HashMap<Int, View?>? = HashMap()

    private val mTouchSlop by lazy { ViewConfiguration.get(getContext()).scaledTouchSlop }

    /**
     * 上次滑动的距离
     */
    private var lastDownY = 0f

    private var mScrollPointerId: Int = -1

    private var isDownScroll = false

    companion object {

        /**
         * CLOSE
         */
        const val ABOVE_CLOSE = 0

        /**
         * OPEN
         */
        const val ABOVE_OPEN = 1

        /**
         * DEFAULT 默认
         */
        const val ABOVE_DEFAULT = 2
    }

    private var isDispatch = false

    fun setDispatch(isDispatch: Boolean) {
        this.isDispatch = isDispatch
    }

    private var isInitOverlap = true

    fun isInitOverlap(): Boolean = isInitOverlap

    fun setInitOverlap(isInitOverlap: Boolean) {
        this.isInitOverlap = isInitOverlap
    }

    fun resetInitOverlap() {
        this.isInitOverlap = false
    }

    fun initAboveController(
        minEdgeOverlapDistance: Int,
        marginTopDistance: Int,
        scrollMaxDistance: Float,
        marginTopDistanceForOpen: Float,
        initOverlapDistance: Int
    ) {
        this.minEdgeOverlapDistance = minEdgeOverlapDistance
        this.marginTopDistance = marginTopDistance
        this.scrollMaxDistance = scrollMaxDistance
        this.marginTopDistanceForOpen = marginTopDistanceForOpen
        setTransY(-initOverlapDistance.toFloat())
    }

    private fun floorTouch(event: MotionEvent?): Boolean {
        println("============above===>>${event?.actionMasked}======floorTouch")
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
                val index = event.findPointerIndex(mScrollPointerId)
                if (index < 0) return false
                val rawY = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    event.getRawY(index)
                } else {
                    event.rawY
                }
                val moveY = rawY - lastDownY
                isDownScroll = (rawY - touchY) > 0
                val y = moveY + translationY
                lastDownY = rawY
                when (aboveLayoutToggleState) {
                    ABOVE_CLOSE -> {
                        if (abs(y) > marginTopDistanceForOpen) {
                            setTransY(-marginTopDistanceForOpen)
                            return false
                        }
                        if (translationY > 0) {
                            setTransY(0f)
                            return false
                        }
                        setTransY(y)
                    }
                    ABOVE_OPEN -> {
                        if (abs(y) > marginTopDistanceForOpen) {
                            setTransY(-marginTopDistanceForOpen)
                            return false
                        }
                        if (translationY > 0) {
                            setTransY(0f)
                            return false
                        }
                        setTransY(y)
                    }
                    ABOVE_DEFAULT -> {

                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (abs(translationY) - scrollMaxDistance < 0) {
                    if (isInitOverlap) {
                        startAboveToggleAnim(false)
                    } else {
                        if (abs(translationY) - scrollMaxDistance / 2 < 0) {
                            startAboveToggleAnim(false)
                        } else {
                            startAboveToggleAnim(true)
                        }
                    }
                } else {
                    if (getState() == ABOVE_OPEN) {
                        if (isDownScroll) {
                            startAboveToggleAnim(false)
                            return false
                        }
                    }
                    startAboveToggleAnim(true)
                }
            }
        }
        return true
    }

    fun isDispatch(): Boolean = isDispatch

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        println("============above===>>${event?.actionMasked}======Above")
        val actionIndex = event?.actionIndex ?: 0
        if (event?.actionMasked == MotionEvent.ACTION_DOWN) {
            isDispatch = true
            println("============分发===>>true")
        }
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
                (listView as? CustomRecyclerView)?.setDispatch(false)
                setDispatch(false)
                println("============分发above===>>false")
                if (abs(rawY - touchY) < mTouchSlop || (abs(rawX - touchX) > abs(rawY - touchY))) {
                    return super.dispatchTouchEvent(event)
                }
            }
        }
        // 上层控件处于打开状态，需要单独处理
        if (aboveLayoutToggleState == ABOVE_OPEN) {
            if (isRunning()) {
                return floorTouch(event)
            }
            if (event?.actionMasked == MotionEvent.ACTION_MOVE) {
                if ((listView as? CustomRecyclerView)?.isDispatch() == true) {
                    if (isTouchDown(event) && ScrollingUtil.isViewToTop(listView, mTouchSlop)) {
                        return floorTouch(event)
                    }
                    return super.dispatchTouchEvent(event)
                }
                return floorTouch(event)
            }
            return super.dispatchTouchEvent(event)
        }
        // 如果滚动到底，交由父控件处理滚动事件
        if (getParentController()?.isBelowScrollBottom() == true) {
            return super.dispatchTouchEvent(event)
        }
        if (event?.actionMasked != MotionEvent.ACTION_DOWN) {
            return floorTouch(event)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (listView == null || isDispatch) return super.onInterceptTouchEvent(
            event
        )
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                isIntercept = false
            }
            MotionEvent.ACTION_MOVE -> {
                isIntercept = aboveLayoutToggleState == ABOVE_CLOSE
            }
            MotionEvent.ACTION_UP -> isIntercept = false
        }
        return isIntercept
    }

    /**
     * 启动上层视图打开关闭动画
     */
    fun startAboveToggleAnim(isOpen: Boolean) {
        resetInitOverlap()
        val duration = 180L
        if (isOpen) {
            val openAnimator =
                ValueAnimator.ofFloat(
                    translationY,
                    -marginTopDistanceForOpen
                )
            openAnimator.addUpdateListener {
                ViewHelper.setTranslationY(this, it.animatedValue as Float)
            }
            openAnimator.addListener(onEnd = {
                aboveLayoutToggleState = ABOVE_OPEN
            })
            openAnimator.duration = duration
            openAnimator.start()
        } else {
            val closeAnimator =
                ValueAnimator.ofFloat(translationY, 0f)
            closeAnimator.addUpdateListener {
                ViewHelper.setTranslationY(this, it.animatedValue as Float)
            }
            closeAnimator.addListener(onEnd = {
                aboveLayoutToggleState = ABOVE_CLOSE
            })
            closeAnimator.duration = duration
            closeAnimator.start()
        }
    }

    private fun isRunning(): Boolean {
        return abs(ViewHelper.getTranslationY(this)) != marginTopDistanceForOpen
    }

    private fun isTouchDown(event: MotionEvent?): Boolean {
        val index = event?.findPointerIndex(mScrollPointerId) ?: -1
        if (index < 0) {
            return false
        }
        val y = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            event?.getRawY(index)
        } else {
            event?.rawY
        } ?: 0f
        val x = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            event?.getRawX(index)
        } else {
            event?.rawX
        } ?: 0f
        val dx = x - touchX
        val dy = y - touchY
        return abs(dy) > mTouchSlop && dy > 0 && abs(dy) >= abs(dx)
    }

    private fun setTransY(translationY: Float) {
        ViewHelper.setTranslationY(
            this,
            translationY
        )
    }

    fun scrollBy(dy: Int) {
        if (this.listView is NestedScrollView || this.listView is ScrollView || this.listView is RecyclerView) {
            this.listView?.scrollBy(0, dy)
        }
    }

    fun updateToggleState(state: Int) {
        this.aboveLayoutToggleState = state
        if (state == ABOVE_OPEN) {
            listView?.parent?.requestDisallowInterceptTouchEvent(true)
        }
    }

    fun getState(): Int = aboveLayoutToggleState

    fun getScrollListView(): View? = listView

    fun addContentView(view: View?) {
        addView(view)
    }

    fun isScrollTop(): Boolean {
        if (this.listView is RecyclerView) {
            return ScrollingUtil.isRecyclerViewToTop(this.listView as? RecyclerView)
        }
        if (this.listView is NestedScrollView) {
            return ScrollingUtil.isScrollViewToBottom(this.listView as? NestedScrollView)
        }
        if (this.listView is ScrollView) {
            return ScrollingUtil.isScrollViewToBottom(this.listView as? ScrollView)
        }
        return false
    }

    fun isListViewDispatch(): Boolean = (listView as? CustomRecyclerView)?.isDispatch() ?: false

    /**
     * 添加滚动控件
     */
    fun addScrollListView(position: Int, view: View?) {
        this.mapListView?.put(position, view)
        if (this.listView == null) {
            this.listView = view
        }
    }

    fun setCurrentListViewIndex(position: Int) {
        this.listView = mapListView?.get(position)
    }

    private fun getParentController(): ParentController? = parent as? ParentController

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.mapListView?.clear()
        this.mapListView = null
        this.listView = null
    }
}