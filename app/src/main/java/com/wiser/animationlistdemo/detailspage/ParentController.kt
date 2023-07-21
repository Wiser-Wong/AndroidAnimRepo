package com.wiser.animationlistdemo.detailspage

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.OverScroller
import androidx.core.animation.addListener
import androidx.core.view.VelocityTrackerCompat
import androidx.core.view.ViewCompat
import kotlin.math.abs

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/2     11:36
 * 用途: 更新说明
 ***************************************
 */
class ParentController @JvmOverloads constructor(
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
    private var minEdgeOverlapDistance = 400

    /**
     * 初始重叠距离
     */
    private var initOverlapDistance = screenHeight / 2

    /**
     * 上层视图由于有弧度角问题，所以增加了padding值
     */
    private var topPadding = 50

    /**
     * 上层视图初始居顶部距离
     */
    private var marginTopInitDistance = screenHeight - minEdgeOverlapDistance - topPadding

    /**
     * 上层视图打开后居顶部距离
     */
    private var marginTopOpenDistance = 230f

    /**
     * 上层视图关闭和打开之间距离
     */
    private var marginTopDistanceForOpen = marginTopInitDistance - marginTopOpenDistance

    /**
     * 控件高度
     */
    private var controllerHeight: Int = screenHeight * 2 - minEdgeOverlapDistance

    /**
     * 上层视图滑动
     */
    private var scrollMaxDistance = marginTopInitDistance / 2f


    /**
     * 下层控件
     */
    private var belowLayout: BelowController? = null

    /**
     * 上层控件
     */
    private var aboveLayout: AboveController? = null

    private val mTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop

    private var touchX = 0f

    private var touchY = 0f

    private var mScrollPointerId: Int = -1

    private val velocityTracker: VelocityTracker? by lazy { VelocityTracker.obtain() }

    /**
     * 上次滑动的距离
     */
    private var lastDownY = 0f

    private var scrollState = SCROLL_STATE_IDLE

    private val minFlingVelocity by lazy { ViewConfiguration.get(context).scaledMinimumFlingVelocity }

    private val maxFlingVelocity by lazy { ViewConfiguration.get(context).scaledMaximumFlingVelocity }

    private val flingRunnable: FlingRunnable = FlingRunnable()

    private var isRunningOpenAnim = false

    companion object {
        const val SCROLL_STATE_IDLE = 0
        const val SCROLL_STATE_DRAGGING = 1
        const val SCROLL_STATE_SETTLING = 2
    }

    init {
        initDistance()
        buildView()
    }

    fun buildView() {
        removeAllViews()
        belowLayout = BelowController(context)
        val belowParams =
            MarginLayoutParams(LayoutParams.MATCH_PARENT, screenHeight - minEdgeOverlapDistance)
        belowParams.bottomMargin = minEdgeOverlapDistance
        belowLayout?.layoutParams = belowParams

        aboveLayout = AboveController(context)
        val aboveParams = MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            screenHeight - marginTopOpenDistance.toInt()
        )
        aboveParams.topMargin = marginTopInitDistance
        aboveLayout?.layoutParams = aboveParams

        addView(belowLayout)
        addView(aboveLayout)

        aboveLayout?.initAboveController(
            minEdgeOverlapDistance = minEdgeOverlapDistance,
            marginTopDistance = marginTopInitDistance,
            scrollMaxDistance = scrollMaxDistance,
            marginTopDistanceForOpen = marginTopDistanceForOpen,
            initOverlapDistance = initOverlapDistance
        )
    }

    private fun initDistance() {
        screenHeight = (screenHeight - marginTopOpenDistance).toInt()
        marginTopInitDistance = screenHeight - minEdgeOverlapDistance - topPadding
        initOverlapDistance = marginTopInitDistance / 4
        marginTopDistanceForOpen = marginTopInitDistance - marginTopOpenDistance
        controllerHeight = screenHeight * 2 - minEdgeOverlapDistance
        scrollMaxDistance = marginTopInitDistance / 2f
        aboveLayout?.initAboveController(
            minEdgeOverlapDistance = minEdgeOverlapDistance,
            marginTopDistance = marginTopInitDistance,
            scrollMaxDistance = scrollMaxDistance,
            marginTopDistanceForOpen = marginTopDistanceForOpen,
            initOverlapDistance = initOverlapDistance
        )
    }

    /**
     * 设置顶部距离
     */
    fun setTopDistance(topDistance: Float): ParentController {
        this.marginTopOpenDistance = topDistance
        initDistance()
        return this
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        println("===========above===>>${ev?.actionMasked}======parent====${aboveLayout?.isDispatch()}")
        println("===========below===>>${ev?.actionMasked}======parent====${belowLayout?.isDispatch()}")
        val actionIndex = ev?.actionIndex ?: 0
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                flingRunnable.stop()
                mScrollPointerId = ev.getPointerId(0)
                touchX = ev.rawX
                touchY = ev.rawY
                lastDownY = ev.rawY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mScrollPointerId = ev.getPointerId(actionIndex)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    touchX = ev.getRawX(ev.findPointerIndex(mScrollPointerId))
                    touchY = ev.getRawY(ev.findPointerIndex(mScrollPointerId))
                    lastDownY = ev.getRawY(ev.findPointerIndex(mScrollPointerId))
                } else {
                    touchX = ev.rawX
                    touchY = ev.rawY
                    lastDownY = ev.rawY
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val index = ev.findPointerIndex(mScrollPointerId)
                if (index < 0) return false
                val rawY = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ev.getRawY(index)
                } else {
                    ev.rawY
                }
                val rawX = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ev.getRawX(index)
                } else {
                    ev.rawX
                }
                // 释放来自于子控件点击
                if (abs(rawY - touchY) < mTouchSlop || (abs(rawX - touchX) > abs(rawY - touchY))) {
                    println("=========>>快速斜下角滑动")
                    return super.dispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val index = ev.findPointerIndex(mScrollPointerId)
                if (index < 0) return false
                val rawY = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ev.getRawY(index)
                } else {
                    ev.rawY
                }
                val rawX = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ev.getRawX(index)
                } else {
                    ev.rawX
                }
                // 释放来自于子控件点击
                if (abs(rawY - touchY) < mTouchSlop || (abs(rawX - touchX) > abs(rawY - touchY))) {
                    aboveLayout?.setDispatch(false)
                    belowLayout?.setDispatch(false)
                    return super.dispatchTouchEvent(ev)
                }
            }
        }
        // 触摸上层布局拦截事件
        if (aboveLayout?.isDispatch() == true) {
            // 抬起手重置上层布局拦截状态，同时也需要重置下层布局拦截状态， 因为会事件穿透到下层导致往下执行下面的逻辑
            if (ev?.actionMasked == MotionEvent.ACTION_UP || ev?.actionMasked == MotionEvent.ACTION_CANCEL) {
                aboveLayout?.setDispatch(false)
                belowLayout?.setDispatch(false)
                println("============分发parent===>>false")
            }
            if (aboveLayout?.getState() == AboveController.ABOVE_OPEN) {
                if (aboveLayout?.isScrollTop() == true && isTouchDown(ev)) {
                    return aboveLayout?.dispatchTouchEvent(ev) ?: super.dispatchTouchEvent(ev)
                }
                return handleVelocity(ev)
            }
            // tip：下层和上层未发生连接，只处理上层控件事件
            // 下层滚动控件未达到底部，此时交由上层控件处理拖动打开关闭操作上层控件
            if (belowLayout?.isScrollBottom() == false) {
                return aboveLayout?.dispatchTouchEvent(ev) ?: super.dispatchTouchEvent(ev)
            }
            return handleVelocity(ev)
        }
        // 触摸下层布局拦截事件
        if (belowLayout?.isDispatch() == true) {
            if (aboveLayout?.isInitOverlap() == true) {
                startAboveToggleAnim(isOpen = false)
            }
            // 抬起手重置下层布局拦截状态
            if (ev?.actionMasked == MotionEvent.ACTION_UP || ev?.actionMasked == MotionEvent.ACTION_CANCEL) {
                belowLayout?.setDispatch(false)
            }
            // 触摸下层控件并且上层控件状态是关闭状态，只处理当前父控件的触摸滚动效果，获取下层控件执行scrollBy,达到下层控件可以滚动
            // 如果上层控件是打开状态，则禁止处理下层滑动控件
            return if (aboveLayout?.getState() == AboveController.ABOVE_CLOSE) {
                // 因为如果是滚动形成上下层控件连接上之后到达的顶部距离，此时上层控件状态也是关闭状态，所以会执行到该逻辑中，所以判断条件改为是否滚动到顶部
                // 如果滚动到顶部，则禁止处理下层滑动控件
                if (isParentTopEdge()) {
                    return true
                }
                // 否则处理滚动速度，达到触摸下层控件滚动效果
                handleVelocity(ev)
            } else {
                // 如果上层控件是打开状态，则禁止处理下层滑动控件
                true
            }
        }
        return super.dispatchTouchEvent(ev)
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

    /**
     * 处理速度
     */
    private fun handleVelocity(event: MotionEvent?): Boolean {
        val actionIndex = event?.actionIndex ?: 0
        var eventAddedToVelocityTracker = false
        val vtev: MotionEvent = MotionEvent.obtain(event)
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mScrollPointerId = event.getPointerId(0)
                scrollState = SCROLL_STATE_IDLE
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
                var dy: Float = lastDownY - rawY
                lastDownY = rawY
                if (scrollState != SCROLL_STATE_DRAGGING) {
                    var startScroll = false
                    if (abs(dy) > mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop
                        } else {
                            dy += mTouchSlop
                        }
                        startScroll = true
                    }
                    if (startScroll) {
                        setScrollState(SCROLL_STATE_DRAGGING)
                    }
                }

                if (scrollState == SCROLL_STATE_DRAGGING) {
                    handleEdgeScrollBottom(dy.toInt(), isBelowScrollBottom())
                }
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker?.addMovement(vtev)
                eventAddedToVelocityTracker = true
                velocityTracker?.computeCurrentVelocity(1000, maxFlingVelocity.toFloat())
                var yVelocity: Int =
                    (-VelocityTrackerCompat.getYVelocity(velocityTracker, 0)).toInt()
                yVelocity = if (abs(yVelocity) < minFlingVelocity) {
                    0
                } else {
                    (-maxFlingVelocity).coerceAtLeast(yVelocity.coerceAtMost(maxFlingVelocity))
                }
                println("==========yVelocity=====>>>>$yVelocity")
                if (yVelocity != 0) {
                    flingRunnable.fling(yVelocity)
                } else {
                    setScrollState(SCROLL_STATE_IDLE)
                }
                resetTouch()
            }
            MotionEvent.ACTION_CANCEL -> {
                resetTouch()
            }
        }
        if (!eventAddedToVelocityTracker) {
            velocityTracker?.addMovement(vtev)
        }
        vtev.recycle()
        return true
    }

    /**
     * 添加下层视图
     */
    fun addBelowView(view: View?) {
        view?.apply {
            belowLayout?.addView(this)
        }
    }

    /**
     * 添加上层视图
     */
    fun addAboveView(view: View?) {
        view?.apply {
            aboveLayout?.addView(this)
        }
    }

    /**
     * 添加上层视图滚动的列表View
     */
    fun addAboveScrollListView(position: Int, view: View?) {
        aboveLayout?.addScrollListView(position, view)
    }

    fun setAboveCurrentListViewIndex(position: Int) {
        aboveLayout?.setCurrentListViewIndex(position)
    }

    /**
     * 添加下层视图滚动的列表View
     */
    fun addBelowScrollListView(view: View?) {
        belowLayout?.addScrollListView(view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, controllerHeight)
    }

    fun isParentTopEdge(): Boolean {
        return abs(scrollY) == marginTopDistanceForOpen.toInt()
    }

    fun isAboveTop(): Boolean = aboveLayout?.getState() == AboveController.ABOVE_OPEN

    fun isAboveBottom(): Boolean = aboveLayout?.getState() == AboveController.ABOVE_CLOSE

    fun isParentBottomEdge(): Boolean {
        return scrollY == 0
    }

    fun isAboveBellowTopAndBottomCenter(): Boolean {
        return abs(scrollY) > 0 && abs(scrollY) < marginTopDistanceForOpen
    }

    fun isBelowScrollBottom(): Boolean = belowLayout?.isScrollBottom() == true

    fun isAboveListViewScrollTop(): Boolean = aboveLayout?.isScrollTop() == true

    fun startAboveToggleAnim(isOpen: Boolean) {
        aboveLayout?.startAboveToggleAnim(isOpen = isOpen)
    }

    fun openAbove() {
        if (aboveLayout?.getState() == AboveController.ABOVE_CLOSE && isBelowScrollBottom()) {
            startParentScrollToAboveTop()
            return
        }
        if (aboveLayout?.getState() == AboveController.ABOVE_CLOSE && !isBelowScrollBottom()) {
            startAboveToggleAnim(true)
        }
    }

    private fun startParentScrollToAboveTop() {
        if (isRunningOpenAnim) return
        val duration = 180L
        val openAnimator =
            ValueAnimator.ofInt(
                scrollY,
                marginTopDistanceForOpen.toInt()
            )
        openAnimator.addUpdateListener {
            scrollTo(0, it.animatedValue as Int)
        }
        openAnimator.addListener(onStart = {
            isRunningOpenAnim = true
        }, onEnd = {
            isRunningOpenAnim = false
        })
        openAnimator.duration = duration
        openAnimator.start()
    }

    private val quinticInterpolator: Interpolator = Interpolator { t ->
        var t = t
        t -= 1.0f
        t * t * t * t * t + 1.0f
    }

    private fun resetTouch() {
        velocityTracker?.clear()
    }

    private fun setScrollState(state: Int) {
        if (state == scrollState) {
            return
        }
        scrollState = state
        if (state != SCROLL_STATE_SETTLING) {
            flingRunnable.stop()
        }
    }

    inner class FlingRunnable : Runnable {
        private var lastFlingY = 0

        private val scroller: OverScroller by lazy { OverScroller(context, quinticInterpolator) }

        private var eatRunOnAnimationRequest = false

        private var reSchedulePostAnimationCallback = false

        override fun run() {
            disableRunOnAnimationRequests()
            val scroller = scroller
            if (scroller.computeScrollOffset()) {
                val y = scroller.currY
                val dy = y - lastFlingY
                if (dy != 0) {
                    if (!this.scroller.isFinished) {
                        handleEdgeScrollBottom(dy, isBelowScrollBottom())
                    }
                    lastFlingY = y
                }
                postOnAnimation()
            }
            enableRunOnAnimationRequests()
        }

        fun fling(velocityY: Int) {
            lastFlingY = 0
            setScrollState(SCROLL_STATE_SETTLING)
            scroller.fling(
                0,
                0,
                0,
                velocityY,
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                Int.MIN_VALUE,
                Int.MAX_VALUE
            )
            postOnAnimation()
        }

        fun stop() {
            removeCallbacks(this)
            scroller.abortAnimation()
        }

        private fun disableRunOnAnimationRequests() {
            reSchedulePostAnimationCallback = false
            eatRunOnAnimationRequest = true
        }

        private fun enableRunOnAnimationRequests() {
            eatRunOnAnimationRequest = false
            if (reSchedulePostAnimationCallback) {
                postOnAnimation()
            }
        }

        private fun postOnAnimation() {
            if (eatRunOnAnimationRequest) {
                reSchedulePostAnimationCallback = true
            } else {
                removeCallbacks(this)
                ViewCompat.postOnAnimation(this@ParentController, this)
            }
        }
    }

    /**
     * 处理滚动
     */
    fun handleEdgeScrollBottom(dy: Int, isScrollBottom: Boolean) {
        println("=============handleEdgeScroll=11111>>${dy}")
        if (isScrollBottom) {
            // 下边界
            if (dy < 0 && dy + scrollY < 0) {
                scrollBy(0, -scrollY)
                belowLayout?.scrollBy(dy)
            } else if (dy > 0 && dy + scrollY > marginTopDistanceForOpen) { // 上边界
                scrollBy(0, (marginTopDistanceForOpen - scrollY).toInt())
                aboveLayout?.scrollBy(dy)
            } else {
                if (isParentTopEdge() && aboveLayout?.isScrollTop() == false) {
                    // 处理点击非RecyclerView上时，处理滑动
                    if (aboveLayout?.isDispatch() == true) {
                        if (aboveLayout?.isListViewDispatch() == true) {
                            aboveLayout?.scrollBy(dy)
                        } else {
                            scrollBy(0, dy)
                        }
                    } else {
                        aboveLayout?.scrollBy(dy)
                    }
                } else {
                    scrollBy(0, dy)
                }
            }
        } else {
            if (aboveLayout?.getState() == AboveController.ABOVE_OPEN) {
                aboveLayout?.scrollBy(dy)
            } else {
                belowLayout?.scrollBy(dy)
            }
        }

    }
}