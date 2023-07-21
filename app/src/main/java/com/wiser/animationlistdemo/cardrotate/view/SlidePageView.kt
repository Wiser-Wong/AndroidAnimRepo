package com.wiser.animationlistdemo.cardrotate.view

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.addListener
import com.wiser.animationlistdemo.cardrotate.OnCardPageChangeListener
import com.wiser.animationlistdemo.cardrotate.OnCardToggleListener
import com.wiser.animationlistdemo.cardrotate.PhotosModel
import java.util.*

/**
 * @author Wiser
 */
class SlidePageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    val defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {
    private val mInflater: LayoutInflater? by lazy { LayoutInflater.from(context) }
    private var views: Vector<View?>? = Vector()
    private var initViews: Vector<View?>? = Vector()
    private var adapter: SlidePageAdapter<*>? = null

    /**
     * 动画执行时间
     */
    private var duration = 300L

    /**
     * 收起动画时间
     */
    private var shrinkDuration = 600L

    /**
     * 未选中卡片缩放值
     */
    private var unSelectCardScale = 0.8f

    /**
     * 选中卡片缩放值
     */
    private var selectCardScale = 1f

    /**
     * 未选中卡片角度
     */
    private var unSelectCardAngle = 25f

    /**
     * 未选中卡片默认偏移Y值
     */
    private var unSelectCardDefaultTranslationY = 80f

    /**
     * 未选中卡片默认偏移X值
     */
    private var unSelectCardDefaultTranslationX = 230f

    var isRunningAnim = false

    /**
     * 是否关闭左右两侧卡片
     */
    private var isClose: Boolean = false

    /**
     * 当前显示坐标
     */
    private var currentItem = 0

    private var onCardToggleListener: OnCardToggleListener? = null
    private var onCardPageChangeListener: OnCardPageChangeListener? = null

    @JvmName("isRunningAnim1")
    fun isRunningAnim(): Boolean = isRunningAnim

    /**
     * 填充适配器
     */
    fun <T> setAdapter(adapter: SlidePageAdapter<T>?, isNotify: Boolean = true) {
        if (adapter == null) return
        this.adapter = adapter
        adapter.initAdapter(this, context, mInflater)
        for (i in 0 until adapter.counts) {
            val view = adapter.getItemView(this, i)
            views?.add(0, view)
            initViews?.add(0, view)
        }
        println("====viewsInit==>>$views")
        initCardView(isNotify)
    }

    /**
     * 初始化卡片控件
     */
    private fun initCardView(isNotify: Boolean = true) {
        removeAllViews()
        for (i in views?.indices!!) {
            val view = views?.get(i)
            addView(view)
            view?.apply {
                setCanTouchView(this, false)
                if (i == ((views?.size ?: 0) - 2)) {
                    rotation = -unSelectCardAngle
                    scaleX = unSelectCardScale
                    scaleY = unSelectCardScale
                    translationX = -unSelectCardDefaultTranslationX
                    translationY = unSelectCardDefaultTranslationY
                } else if (i == (views?.size ?: 0) - 3) {
                    rotation = unSelectCardAngle
                    scaleX = unSelectCardScale
                    scaleY = unSelectCardScale
                    translationX = unSelectCardDefaultTranslationX
                    translationY = unSelectCardDefaultTranslationY
                } else {
                    if (i == (views?.size ?: 0) - 1) {
                        setCanTouchView(this, true)
                        rotation = 0f
                        scaleX = selectCardScale
                        scaleY = selectCardScale
                        translationX = 0f
                        if (isNotify) {
                            onCardPageChangeListener?.onPageSelect(0)
                        }
                    } else {
                        rotation = 0f
                        translationX = 0f
                        alpha = 0f
                        scaleX = unSelectCardScale
                        scaleY = unSelectCardScale
                    }
                }
            }
        }
    }

    fun setOnCardPageChangeListener(onCardPageChangeListener: OnCardPageChangeListener?) {
        this.onCardPageChangeListener = onCardPageChangeListener
    }

    fun setOnCardToggleListener(onCardToggleListener: OnCardToggleListener?) {
        this.onCardToggleListener = onCardToggleListener
    }

    fun setCanTouchView(view: View?, isCanTouch: Boolean) {
        if (view is SlideLayout) {
            view.setCanTouch(isCanTouch)
            view.setPageView(this)
        }
    }

    fun getTopView(): View? = if ((views?.size ?: 0) > 0) views?.get((views?.size?:0) - 1) else null

    @JvmName("isClose1")
    fun isClose(): Boolean = isClose

    fun getCloseDuration(): Long = shrinkDuration

    /**
     * 设置左右两侧卡片的动画
     */
    fun setLeftRightCardAnim(isAnim: Boolean = true) {
        if (isClose) return
        if (isRunningAnim && isAnim) return
        val leftView = if ((views?.size ?: 0) > 1) views?.get((views?.size ?: 0) - 2) else null
        val rightView = if ((views?.size ?: 0) > 2) views?.get((views?.size ?: 0) - 3) else null
        val otherView = if ((views?.size ?: 0) > 3) views?.get((views?.size ?: 0) - 4) else null
        setCanTouchView(rightView, false)
        setCanTouchView(otherView, false)

        leftView?.apply {
            isRunningAnim = true
            alpha = 1f
            val set1 = AnimatorSet()
            set1.duration = if (isAnim) duration else 0
            val animatorTransX1 = ValueAnimator.ofFloat(-unSelectCardDefaultTranslationX, 0f)
            animatorTransX1.addUpdateListener { valueAnimator ->
                translationX = (valueAnimator.animatedValue as Float)
            }
            val animatorTransY1 = ValueAnimator.ofFloat(unSelectCardDefaultTranslationY, 0f)
            animatorTransY1.addUpdateListener { valueAnimator ->
                translationY = (valueAnimator.animatedValue as Float)
            }
            val animatorScale1 = ValueAnimator.ofFloat(unSelectCardScale, selectCardScale)
            animatorScale1.addUpdateListener { valueAnimator ->
                scaleX = (valueAnimator.animatedValue as Float)
                scaleY = (valueAnimator.animatedValue as Float)
            }
            val animatorRotate1 = ValueAnimator.ofFloat(-unSelectCardAngle, 0f)
            animatorRotate1.addUpdateListener { valueAnimator ->
                rotation = (valueAnimator.animatedValue as Float)
            }
            set1.playTogether(animatorTransX1, animatorTransY1, animatorScale1, animatorRotate1)
            set1.start()
            set1.addListener(onEnd = {
                isRunningAnim = false
                setCanTouchView(this, true)
            })
        }

        rightView?.apply {
            isRunningAnim = true
            alpha = 1f
            val set2 = AnimatorSet()
            set2.duration = if (isAnim) duration else 0
            val animatorTransX2 = ValueAnimator.ofFloat(
                unSelectCardDefaultTranslationX,
                -unSelectCardDefaultTranslationX
            )
            animatorTransX2.addUpdateListener { valueAnimator ->
                translationX = (valueAnimator.animatedValue as Float)
            }
            val animatorRotate2 = ValueAnimator.ofFloat(unSelectCardAngle, -unSelectCardAngle)
            animatorRotate2.addUpdateListener { valueAnimator ->
                rotation = (valueAnimator.animatedValue as Float)
            }
            set2.playTogether(animatorTransX2, animatorRotate2)
            set2.start()
            set2.addListener(onEnd = {
                isRunningAnim = false
            })
        }

        otherView?.apply {
            isRunningAnim = true
            val set3 = AnimatorSet()
            set3.duration = if (isAnim) duration else 0
            val animatorTransX3 = ValueAnimator.ofFloat(0f, unSelectCardDefaultTranslationX)
            animatorTransX3.addUpdateListener { valueAnimator ->
                translationX = (valueAnimator.animatedValue as Float)
            }
            val animatorTransY3 = ValueAnimator.ofFloat(0f, unSelectCardDefaultTranslationY)
            animatorTransY3.addUpdateListener { valueAnimator ->
                translationY = (valueAnimator.animatedValue as Float)
            }
            val animatorRotate3 = ValueAnimator.ofFloat(0f, unSelectCardAngle)
            animatorRotate3.addUpdateListener { valueAnimator ->
                rotation = (valueAnimator.animatedValue as Float)
            }
            val animatorAlpha3 = ValueAnimator.ofFloat(0f, 1f)
            animatorAlpha3.addUpdateListener { valueAnimator ->
                alpha = (valueAnimator.animatedValue as Float)
            }
            set3.playTogether(animatorTransX3, animatorTransY3, animatorRotate3, animatorAlpha3)
            set3.start()
            set3.addListener(onEnd = {
                isRunningAnim = false
            })
        }
    }

    /**
     * 设置其他收缩动画
     */
    fun setCloseOtherAnim() {
        if (isRunningAnim) return
        this.isClose = true
        onCardToggleListener?.onCardToggleStart(true)
        val leftView = if ((views?.size ?: 0) > 1) views?.get((views?.size ?: 0) - 2) else null
        val rightView = if ((views?.size ?: 0) > 2) views?.get((views?.size ?: 0) - 3) else null
        setCanTouchView(leftView, false)
        setCanTouchView(rightView, false)

        leftView?.apply {
            isRunningAnim = true
            alpha = 1f
            val set1 = AnimatorSet()
            set1.duration = shrinkDuration
            val animatorTransX1 = ValueAnimator.ofFloat(-unSelectCardDefaultTranslationX, 0f)
            animatorTransX1.addUpdateListener { valueAnimator ->
                translationX = (valueAnimator.animatedValue as Float)
            }
            val animatorTransY1 = ValueAnimator.ofFloat(unSelectCardDefaultTranslationY, 0f)
            animatorTransY1.addUpdateListener { valueAnimator ->
                translationY = (valueAnimator.animatedValue as Float)
            }
            val animatorScale1 = ValueAnimator.ofFloat(unSelectCardScale, selectCardScale)
            animatorScale1.addUpdateListener { valueAnimator ->
                scaleX = (valueAnimator.animatedValue as Float)
                scaleY = (valueAnimator.animatedValue as Float)
            }
            val animatorRotate1 = ValueAnimator.ofFloat(-unSelectCardAngle, 0f)
            animatorRotate1.addUpdateListener { valueAnimator ->
                rotation = (valueAnimator.animatedValue as Float)
            }
            set1.playTogether(animatorTransX1, animatorTransY1, animatorScale1, animatorRotate1)
            set1.start()
            set1.addListener(onEnd = {
                isRunningAnim = false
                onCardToggleListener?.onCardToggle(true)
            })
        }

        rightView?.apply {
            isRunningAnim = true
            alpha = 1f
            val set2 = AnimatorSet()
            set2.duration = shrinkDuration
            val animatorTransX2 = ValueAnimator.ofFloat(unSelectCardDefaultTranslationX, 0f)
            animatorTransX2.addUpdateListener { valueAnimator ->
                translationX = (valueAnimator.animatedValue as Float)
            }
            val animatorTransY2 = ValueAnimator.ofFloat(unSelectCardDefaultTranslationY, 0f)
            animatorTransY2.addUpdateListener { valueAnimator ->
                translationY = (valueAnimator.animatedValue as Float)
            }
            val animatorScale2 = ValueAnimator.ofFloat(unSelectCardScale, selectCardScale)
            animatorScale2.addUpdateListener { valueAnimator ->
                scaleX = (valueAnimator.animatedValue as Float)
                scaleY = (valueAnimator.animatedValue as Float)
            }
            val animatorRotate2 = ValueAnimator.ofFloat(unSelectCardAngle, 0f)
            animatorRotate2.addUpdateListener { valueAnimator ->
                rotation = (valueAnimator.animatedValue as Float)
            }
            set2.playTogether(animatorTransX2, animatorTransY2, animatorScale2, animatorRotate2)
            set2.start()
            set2.addListener(onEnd = {
                isRunningAnim = false
                onCardToggleListener?.onCardToggle(false)
            })
        }
    }

    /**
     * 设置其他展开动画
     */
    fun setOpenOtherAnim() {
        if (isRunningAnim) return
        onCardToggleListener?.onCardToggleStart(false)
        this.isClose = false
        val leftView = if ((views?.size ?: 0) > 1) views?.get((views?.size ?: 0) - 2) else null
        val rightView = if ((views?.size ?: 0) > 2) views?.get((views?.size ?: 0) - 3) else null
        setCanTouchView(leftView, false)
        setCanTouchView(rightView, false)

        leftView?.apply {
            isRunningAnim = true
            alpha = 1f
            val set1 = AnimatorSet()
            set1.duration = shrinkDuration
            val animatorTransX1 = ValueAnimator.ofFloat(0f, -unSelectCardDefaultTranslationX)
            animatorTransX1.addUpdateListener { valueAnimator ->
                translationX = (valueAnimator.animatedValue as Float)
            }
            val animatorTransY1 = ValueAnimator.ofFloat(0f, unSelectCardDefaultTranslationY)
            animatorTransY1.addUpdateListener { valueAnimator ->
                translationY = (valueAnimator.animatedValue as Float)
            }
            val animatorScale1 = ValueAnimator.ofFloat(selectCardScale, unSelectCardScale)
            animatorScale1.addUpdateListener { valueAnimator ->
                scaleX = (valueAnimator.animatedValue as Float)
                scaleY = (valueAnimator.animatedValue as Float)
            }
            val animatorRotate1 = ValueAnimator.ofFloat(0f, -unSelectCardAngle)
            animatorRotate1.addUpdateListener { valueAnimator ->
                rotation = (valueAnimator.animatedValue as Float)
            }
            set1.playTogether(animatorTransX1, animatorTransY1, animatorScale1, animatorRotate1)
            set1.start()
            set1.addListener(onEnd = {
                isRunningAnim = false
            })
        }

        rightView?.apply {
            isRunningAnim = true
            alpha = 1f
            val set2 = AnimatorSet()
            set2.duration = shrinkDuration
            val animatorTransX2 = ValueAnimator.ofFloat(0f, unSelectCardDefaultTranslationX)
            animatorTransX2.addUpdateListener { valueAnimator ->
                translationX = (valueAnimator.animatedValue as Float)
            }
            val animatorTransY2 = ValueAnimator.ofFloat(0f, unSelectCardDefaultTranslationY)
            animatorTransY2.addUpdateListener { valueAnimator ->
                translationY = (valueAnimator.animatedValue as Float)
            }
            val animatorScale2 = ValueAnimator.ofFloat(selectCardScale, unSelectCardScale)
            animatorScale2.addUpdateListener { valueAnimator ->
                scaleX = (valueAnimator.animatedValue as Float)
                scaleY = (valueAnimator.animatedValue as Float)
            }
            val animatorRotate2 = ValueAnimator.ofFloat(0f, unSelectCardAngle)
            animatorRotate2.addUpdateListener { valueAnimator ->
                rotation = (valueAnimator.animatedValue as Float)
            }
            set2.playTogether(animatorTransX2, animatorTransY2, animatorScale2, animatorRotate2)
            set2.start()
            set2.addListener(onEnd = {
                isRunningAnim = false
            })
        }

    }

    /**
     * 移除顶部View
     */
    fun removeTopView(view: View?) {
        views?.apply {
            if (currentItem == size - 1) {
                currentItem = 0
            } else {
                currentItem += 1
            }
            remove(view)
            add(0, view)
        }
        post {
            view?.apply {
                removeView(this)
                addView(this, 0)
                setCanTouchView(this, false)
                rotation = 0f
                translationX = 0f
                translationY = 0f
                alpha = 0f
                scaleX = unSelectCardScale
                scaleY = unSelectCardScale
                onCardPageChangeListener?.onPageSelect(currentItem)
            }
        }
    }

    private fun notifyViews() {
        views?.clear()
        for (i in 0 until (adapter?.counts ?: 0)) {
            val view = adapter?.getItemView(this, i)
            views?.add(0, view)
        }
        val appendViewsLeft: Vector<View?> = Vector()
        val appendViewsRight: Vector<View?> = Vector()
        views?.forEachIndexed { index, view ->
            val size = views?.size ?: 0
            if (index > (size - 1 - currentItem)) {
                appendViewsRight.add(view)
            }
            if (index < (size - 1 - currentItem)) {
                appendViewsLeft.add(view)
            }
        }
        views?.removeAll(appendViewsLeft)
        views?.removeAll(appendViewsRight)
        views?.addAll(0, appendViewsLeft)
        views?.addAll(0, appendViewsRight)
    }

    private fun newViews() {
        views?.clear()
        initViews?.let { views?.addAll(it) }
        val appendViewsLeft: Vector<View?> = Vector()
        val appendViewsRight: Vector<View?> = Vector()
        views?.forEachIndexed { index, view ->
            val size = views?.size ?: 0
            if (index > (size - 1 - currentItem)) {
                appendViewsRight.add(view)
            }
            if (index < (size - 1 - currentItem)) {
                appendViewsLeft.add(view)
            }
        }
        views?.removeAll(appendViewsLeft)
        views?.removeAll(appendViewsRight)
        views?.addAll(0, appendViewsLeft)
        views?.addAll(0, appendViewsRight)
    }

    fun setCurrentItem(position: Int) {
        if (position >= (views?.size ?: 0) || position < 0) {
            return
        }
        this.currentItem = position
        removeAllViews()
        newViews()
        for (i in views?.indices!!) {
            val view = views?.get(i)
            addView(view)
            view?.apply {
                setCanTouchView(this, true)
                alpha = 1f
                if (i == ((views?.size ?: 0) - 2)) {
                    scaleX = unSelectCardScale
                    scaleY = unSelectCardScale
                } else if (i == (views?.size ?: 0) - 3) {
                    scaleX = unSelectCardScale
                    scaleY = unSelectCardScale
                } else {
                    if (i == (views?.size ?: 0) - 1) {
                        rotation = 0f
                        scaleX = selectCardScale
                        scaleY = selectCardScale
                        translationX = 0f
                    } else {
                        rotation = 0f
                        translationX = 0f
                        alpha = 0f
                        scaleX = unSelectCardScale
                        scaleY = unSelectCardScale
                    }
                }
            }
        }
        onCardPageChangeListener?.onPageSelect(currentItem)
    }

    fun getViews(): Vector<View?>? = views

    fun getItemCounts(): Int = views?.size ?: 0

    /**
     * 更新适配器
     */
    fun notifyDataAdapter() {
        postDelayed({
            removeAllViews()
            notifyViews()
            for (i in views?.indices!!) {
                val view = views?.get(i)
                addView(view)
                view?.apply {
                    setCanTouchView(this, false)
                    alpha = 1f
                    if (i == ((views?.size ?: 0) - 2)) {
                        rotation = -unSelectCardAngle
                        scaleX = unSelectCardScale
                        scaleY = unSelectCardScale
                        translationX = -unSelectCardDefaultTranslationX
                        translationY = unSelectCardDefaultTranslationY
                    } else if (i == (views?.size ?: 0) - 3) {
                        rotation = unSelectCardAngle
                        scaleX = unSelectCardScale
                        scaleY = unSelectCardScale
                        translationX = unSelectCardDefaultTranslationX
                        translationY = unSelectCardDefaultTranslationY
                    } else {
                        if (i == (views?.size ?: 0) - 1) {
                            setCanTouchView(this, true)
                            rotation = 0f
                            scaleX = selectCardScale
                            scaleY = selectCardScale
                            translationX = 0f
                        } else {
                            rotation = 0f
                            translationX = 0f
                            alpha = 0f
                            scaleX = unSelectCardScale
                            scaleY = unSelectCardScale
                        }
                    }
                }
            }
        }, 100)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        views?.clear()
        views = null
        adapter?.detach()
        adapter = null
    }
}