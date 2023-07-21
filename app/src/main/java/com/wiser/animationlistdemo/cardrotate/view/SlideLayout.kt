package com.wiser.animationlistdemo.cardrotate.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.cardrotate.ScreenUtil.getScreenWidth
import kotlin.math.abs

/**
 * @author Wiser
 */
class SlideLayout @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    /**
     * 按下X坐标
     */
    private var downX = 0f

    /**
     * 按下Y坐标
     */
    private var downY = 0f

    /**
     * 动画开始的X坐标
     */
    private var animStartX = 0f

    /**
     * 动画开始的Y坐标
     */
    private var animStartY = 0f
    private var isRunningAnim = false

    /**
     * 是否可以触摸
     */
    private var isCanTouch = false

    /**
     * 动画时长
     */
    private var duration = 300L

    /**
     * 获取方向
     *
     * @return
     */
    var direction = NO_SLIDE
        private set
    private var pageView: SlidePageView? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_slide, this, false)
        addView(view)
        if (parent is SlidePageView) {
            pageView = parent as SlidePageView?
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (!isCanTouch) return false
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.rawX
                downY = event.rawY
                return super.dispatchTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.rawX
                val moveY = event.rawY
                if (abs(moveX - downX) > 20 || abs(moveY - downY) > 20) {
                    if (pageView?.isClose() == false) {
                        setDirection(moveX, moveY)
                        translationX = moveX - downX
                        translationY = moveY - downY
                    } else {
                        return false
                    }
                } else {
                    return super.dispatchTouchEvent(event)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (abs(event.rawX - downX) > 20 || abs(event.rawY - downY) > 20) {
                    if (pageView?.isClose() == false) {
                        animStartX = event.rawX - downX
                        animStartY = event.rawY - downY
                        if (isSlideHalf) {
                            removeAnim()
                        } else {
                            resetAnim()
                        }
                    } else {
                        return false
                    }
                } else {
                    return super.dispatchTouchEvent(event)
                }
            }
        }
        return true
    }

    fun setCanTouch(isCanTouch: Boolean) {
        this.isCanTouch = isCanTouch
    }

    fun setPageView(pageView: SlidePageView?) {
        if (this.pageView == null)
            this.pageView = pageView
    }

    fun isRunningAnim(): Boolean? = pageView?.isRunningAnim()

    fun setToggleOtherAnim(isOpen: Boolean) {
        if (isOpen) {
            pageView?.setOpenOtherAnim()
        } else {
            pageView?.setCloseOtherAnim()
        }
    }

    /**
     * 判断是否滑动一半
     *
     * @return
     */
    private val isSlideHalf: Boolean
        get() = abs(animStartX) > width / 2f || abs(animStartY) > height / 2f

    /**
     * 设置滑动方向
     *
     * @param moveX
     * @param moveY
     */
    private fun setDirection(moveX: Float, moveY: Float) {
        val x = moveX - downX
        val y = moveY - downY
        direction = if (x < 0 && abs(x) > abs(y)) {
            LEFT_SLIDE
        } else if (y < 0 && abs(y) > abs(x)) {
            TOP_SLIDE
        } else if (x > 0 && x > abs(y)) {
            RIGHT_SLIDE
        } else if (y > 0 && y > abs(x)) {
            BOTTOM_SLIDE
        } else {
            NO_SLIDE
        }
    }

    /**
     * 重置动画
     */
    private fun resetAnim() {
        if (isRunningAnim) return
        isRunningAnim = true
        val animator = ValueAnimator.ofObject(
            BesselTypeEvaluator(),
            Point(animStartX.toInt(), animStartY.toInt()),
            Point(0, 0)
        )
        animator.duration = duration
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isRunningAnim = false
            }
        })
        animator.addUpdateListener { animation ->
            val point = animation.animatedValue as Point
            translationX = point.x.toFloat()
            translationY = point.y.toFloat()
        }
        animator.start()
    }

    /**
     * 移除动画
     */
    private fun removeAnim() {
        if (isRunningAnim) return
        isRunningAnim = true
        val endPoint = Point()
        when (direction) {
            LEFT_SLIDE -> {
                endPoint.x =
                    -getScreenWidth(context) - paddingLeft - (layoutParams as MarginLayoutParams).leftMargin
                endPoint.y = translationY.toInt()
            }
            RIGHT_SLIDE -> {
                endPoint.x =
                    getScreenWidth(context) + paddingRight + (layoutParams as MarginLayoutParams).rightMargin
                endPoint.y = translationY.toInt()
            }
            TOP_SLIDE -> {
                endPoint.x = translationX.toInt()
                endPoint.y = -height - paddingTop - (layoutParams as MarginLayoutParams).topMargin
            }
            BOTTOM_SLIDE -> {
                endPoint.x = translationX.toInt()
                endPoint.y =
                    height + paddingBottom + (layoutParams as MarginLayoutParams).bottomMargin
            }
            else -> {
                resetAnim()
            }
        }
        val animator = ValueAnimator.ofObject(
            BesselTypeEvaluator(),
            Point(animStartX.toInt(), animStartY.toInt()),
            endPoint
        )
        animator.duration = duration
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isRunningAnim = false
                pageView?.removeTopView(this@SlideLayout)
            }
        })
        animator.addUpdateListener { animation ->
            val point = animation.animatedValue as Point
            translationX = point.x.toFloat()
            translationY = point.y.toFloat()
        }
        animator.start()
        pageView?.setLeftRightCardAnim()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        downX = 0f
        downY = 0f
        animStartX = 0f
        animStartY = 0f
        isRunningAnim = false
    }

    companion object {
        private const val TOP_SLIDE = 0X10002
        private const val BOTTOM_SLIDE = 0X10003
        private const val LEFT_SLIDE = 0X10004
        private const val RIGHT_SLIDE = 0X10005
        private const val NO_SLIDE = 0X10006
    }
}