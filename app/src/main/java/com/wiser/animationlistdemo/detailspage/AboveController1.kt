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
class AboveController1 @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mScrollPointerId: Int = -1

    private var touchX = 0f

    private var touchY = 0f

    private var lastDownY = 0f

    private val mTouchSlop by lazy { ViewConfiguration.get(getContext()).scaledTouchSlop }

    private var isDownScroll = false

    /**
     * 上层视图居顶部距离
     */
    private var marginTopDistance = 0f

    /**
     * 因为上层布局处在ScrollView中，所以上层布局需要设置初始位置，让上层布局盖住底层布局
     */
    private var initAboveTransY = 0f

    /**
     * 列表控件 用于处理滑动冲突
     */
    private var listView: View? = null

    private var mapListView: HashMap<Int, View?>? = HashMap()

    private var aboveLayoutToggleState = ABOVE_CLOSE

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

    fun initAbove(marginTopDistance: Float, initAboveTransY: Float) {
        this.marginTopDistance = marginTopDistance
        this.initAboveTransY = initAboveTransY
        isClickable = true
        isFocusable = true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // 如果滚动到底，交由父控件处理滚动事件
        if (getParentController()?.isBelowScrollBottom() == true && getParentController()?.isAboveBetweenTopAndBottom() == true){
            return false
        }
        // scrollView下层布局滚动到底时，直接事件交由listView处理
        val actionIndex = event?.actionIndex ?: 0
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mScrollPointerId = event.getPointerId(0)
                touchX = event.rawX
                touchY = event.rawY
                lastDownY = event.rawY
                getParentController()?.setScrollable(false)
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
                getParentController()?.setScrollable(true)
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
        // 上层控件处于打开状态，需要单独处理
        if (aboveLayoutToggleState == ABOVE_OPEN) {
            if (isRunning()) {
                return floorTouch(event)
            }
            if (event?.actionMasked == MotionEvent.ACTION_MOVE) {
                if ((listView as? CustomRecyclerView1)?.isDispatch() == true) {
                    if (isTouchDown(event) && ScrollingUtil.isViewToTop(listView, mTouchSlop)) {
                        return floorTouch(event)
                    }
                    return listView?.dispatchTouchEvent(event)?:super.dispatchTouchEvent(event)
                }
                return floorTouch(event)
            }
            return super.dispatchTouchEvent(event)
        }

        if (getParentController()?.isBelowScrollBottom() == true) {
            return super.dispatchTouchEvent(event)
        }

        if (event?.actionMasked != MotionEvent.ACTION_DOWN) {
            return floorTouch(event)
        }
        return super.dispatchTouchEvent(event)
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
                println("===================|||y===>>$y")
                println("===================|||t===>>${abs(initAboveTransY) + marginTopDistance}")
                println("===================|||i===>>${abs(initAboveTransY)}")
                lastDownY = rawY
                when (aboveLayoutToggleState) {
                    ABOVE_CLOSE -> {
                        if (abs(y) > abs(initAboveTransY) + marginTopDistance) {
                            setTransY(-(abs(initAboveTransY) + marginTopDistance))
                            return false
                        }
                        if (abs(y) < abs(initAboveTransY)) {
                            setTransY(initAboveTransY)
                            return false
                        }
                        setTransY(y)
                    }
                    ABOVE_OPEN -> {
                        if (abs(y) > abs(initAboveTransY) + marginTopDistance) {
                            setTransY(-(abs(initAboveTransY) + marginTopDistance))
                            return false
                        }
                        if (abs(y) < abs(initAboveTransY)) {
                            setTransY(initAboveTransY)
                            return false
                        }
                        setTransY(y)
                    }
                    ABOVE_DEFAULT -> {

                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isDownScroll) {
                    startAboveToggleAnim(false)
                } else {
                    startAboveToggleAnim(true)
                }
            }
        }
        return true
    }

    /**
     * 启动上层视图打开关闭动画
     */
    fun startAboveToggleAnim(isOpen: Boolean) {
        val duration = 180L
        if (isOpen) {
            val openAnimator =
                ValueAnimator.ofFloat(
                    translationY,
                    -(abs(initAboveTransY) + marginTopDistance)
                )
            openAnimator.addUpdateListener {
                ViewHelper.setTranslationY(this, it.animatedValue as Float)
            }
            openAnimator.addListener(onEnd = {
                aboveLayoutToggleState = ABOVE_OPEN
                (listView as? CustomRecyclerView1)?.setOpen(true)
            })
            openAnimator.duration = duration
            openAnimator.start()
        } else {
            val closeAnimator =
                ValueAnimator.ofFloat(translationY, initAboveTransY)
            closeAnimator.addUpdateListener {
                ViewHelper.setTranslationY(this, it.animatedValue as Float)
            }
            closeAnimator.addListener(onEnd = {
                aboveLayoutToggleState = ABOVE_CLOSE
                (listView as? CustomRecyclerView1)?.setOpen(false)
            })
            closeAnimator.duration = duration
            closeAnimator.start()
        }
    }

    private fun isRunning(): Boolean {
        return abs(ViewHelper.getTranslationY(this)) != abs(initAboveTransY) + marginTopDistance
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

    fun getState(): Int = aboveLayoutToggleState

    private fun setTransY(translationY: Float) {
        ViewHelper.setTranslationY(
            this,
            translationY
        )
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (listView == null || getParentController()?.isBelowScrollBottom() == true) return super.onInterceptTouchEvent(
            event
        )
        return false
    }

    fun addScrollListView(position: Int, view: View?) {
        this.mapListView?.put(position, view)
        if (this.listView == null) {
            this.listView = view
        }
    }

    fun setCurrentListViewIndex(position: Int) {
        this.listView = mapListView?.get(position)
    }

    private fun getParentController(): CustomScrollView1? = parent.parent as? CustomScrollView1

}