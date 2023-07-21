package com.wiser.animationlistdemo.detailspage

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.animation.Interpolator
import android.widget.OverScroller
import androidx.core.view.VelocityTrackerCompat
import androidx.core.view.ViewCompat
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
class CustomRecyclerView1 @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val mTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop

    private var touchX = 0f

    private var touchY = 0f

    private var mScrollPointerId: Int = -1

    private val velocityTracker: VelocityTracker? by lazy { VelocityTracker.obtain() }

    /**
     * 上次滑动的距离
     */
    private var lastDownY = 0f

    private var newScrollState = SCROLL_STATE_IDLE

    private val flingRunnable: CustomRecyclerView1.FlingRunnable = FlingRunnable()

    private var isOpen = false

    private var isDispatch = false

    fun setDispatch(isDispatch: Boolean) {
        this.isDispatch = isDispatch
    }

    fun isDispatch(): Boolean = isDispatch

    fun setOpen(isOpen: Boolean) {
        this.isOpen = isOpen
    }

    fun isScrollTop(): Boolean = ScrollingUtil.isRecyclerViewToTop(this)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        println("============above===>>${event?.actionMasked}======customRecyclerView")
        val actionIndex = event?.actionIndex ?: 0
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                this.isDispatch = true
                mScrollPointerId = event.getPointerId(0)
                touchX = event.rawX
                touchY = event.rawY
                lastDownY = event.rawY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                this.isDispatch = true
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
                isDispatch = false
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
//        if (ScrollingUtil.isRecyclerViewToTop(this)) {
//            return super.dispatchTouchEvent(event)
//        }
        return handleVelocity(event)
//        if (isOpen) {
//            return handleVelocity(event)
//        }
//        return super.dispatchTouchEvent(event)
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
                newScrollState = SCROLL_STATE_IDLE
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
                    handleEdgeScrollBottom(dy.toInt())
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
        newScrollState = state
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
                        handleEdgeScrollBottom(dy)
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
                ViewCompat.postOnAnimation(this@CustomRecyclerView1, this)
            }
        }
    }

    fun handleEdgeScrollBottom(dy: Int) {
        println("=============handleEdgeScroll=11111>>${dy}")
        scrollBy(0, dy)
    }
}