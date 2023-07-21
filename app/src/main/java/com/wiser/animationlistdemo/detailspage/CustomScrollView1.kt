package com.wiser.animationlistdemo.detailspage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import com.nineoldandroids.view.ViewHelper
import com.wiser.animationlistdemo.R
import kotlin.math.*

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/21     18:46
 * 用途: 更新说明
 ***************************************
 */
class CustomScrollView1 @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    /**
     * 屏幕高度
     */
    private var screenHeight: Int = ScreenTools.getScreenDPI(context)

    private val aboveHeight = screenHeight - (resources?.getDimension(R.dimen.ksl_dp_85)?.toInt()?:0)

    /**
     * 下层控件
     */
    private var belowLayout: FrameLayout? = null

    /**
     * 上层控件
     */
    private var aboveLayout: AboveController1? = null

    /**
     * 上层布局居该控件顶部的距离
     */
    private val aboveMarginTopDistance = aboveHeight * 3 / 5f

    /**
     * 因为上层布局处在ScrollView中，所以上层布局需要设置初始位置，让上层布局盖住底层布局
     */
    private val initAboveTransY by lazy { (-getChildAt(0).measuredHeight + aboveHeight).toFloat() + aboveMarginTopDistance }

    private var scrollable = true

    private var isLink = false

    init {
        val linearLayout = LinearLayout(context, attrs, defStyleAttr)
        linearLayout.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayout.layoutParams = layoutParams

        addView(linearLayout)

        belowLayout = FrameLayout(context)
        val belowParams =
            MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        belowLayout?.layoutParams = belowParams

        aboveLayout = AboveController1(context)
        val aboveParams = MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            aboveHeight
        )
        aboveLayout?.layoutParams = aboveParams

        linearLayout.addView(belowLayout)
        linearLayout.addView(aboveLayout)

        post {
            ViewHelper.setTranslationY(aboveLayout, initAboveTransY)
            aboveLayout?.initAbove(marginTopDistance = aboveMarginTopDistance, initAboveTransY = initAboveTransY + scrollY)
        }

        setOnScrollChangeListener(object : OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (aboveLayout?.getState() == AboveController1.ABOVE_OPEN) {
                    return
                }
                if (scrollY < -initAboveTransY) {
                    isLink = false
                    aboveLayout?.translationY = initAboveTransY + scrollY
                    aboveLayout?.initAbove(marginTopDistance = aboveMarginTopDistance, initAboveTransY = initAboveTransY + scrollY)
                } else {
                    isLink = true
                    aboveLayout?.translationY = 0f
                }
            }
        })
    }

    fun isBelowScrollBottom(): Boolean = aboveLayout?.translationY == 0f

    fun isAboveScrollTop(): Boolean = scrollY == (ceil(abs(initAboveTransY)) + ceil(aboveMarginTopDistance)).toInt()

    fun isAboveBetweenTopAndBottom(): Boolean = abs(scrollY) > abs(initAboveTransY) && abs(scrollY) < abs(initAboveTransY) + aboveMarginTopDistance

    fun setScrollable(scrollable: Boolean) {
        this.scrollable = scrollable
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return scrollable && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return scrollable && super.onInterceptTouchEvent(ev)
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
}