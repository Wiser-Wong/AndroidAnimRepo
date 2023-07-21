package com.wiser.animationlistdemo.toast

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private var radius = 30

    private var borderWidth = 10f

    private var loadingSize: Int = (radius * 2 + borderWidth).toInt()

    private val left: Float by lazy {
        width.coerceAtLeast(2 * radius) / 2f - (width / 2).coerceAtMost(
            radius
        )
    }

    private val top: Float by lazy {
        height.coerceAtLeast(2 * radius) / 2f - (height / 2).coerceAtMost(
            radius
        )
    }

    private val right: Float by lazy {
        width.coerceAtLeast(2 * radius) / 2f + (width / 2).coerceAtMost(
            radius
        )
    }

    private val bottom: Float by lazy {
        height.coerceAtLeast(2 * radius) / 2f + (height / 2).coerceAtMost(
            radius
        )
    }

    private val mPaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.RED
        paint.strokeWidth = borderWidth
        paint.style = Paint.Style.STROKE
        return@lazy paint
    }

    private val mRectF: RectF by lazy {
        return@lazy RectF(left, top, right, bottom)
    }

    private var startAngle = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var sweepAngle = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var animAngle = ValueAnimator.ofFloat(30f, 270f).setDuration(800)
    private var animAngle1 = ValueAnimator.ofFloat(-90f, 150f).setDuration(800)

    init {
        animAngle.addUpdateListener {
            startAngle = it.animatedValue as Float
            sweepAngle = it.animatedValue as Float
        }
        animAngle1.addUpdateListener {
            val value = it.animatedValue as Float
            startAngle = 270 + value
            sweepAngle = -180f + value
        }
        animAngle.doOnEnd {
            animAngle1.start()
        }
        animAngle1.doOnEnd {
            animAngle.start()
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startLoadingAnim()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopLoadingAnim()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLoading(canvas)
    }

    private fun drawLoading(canvas: Canvas?) {
        canvas?.apply {
//            drawCircle(width / 2f, height / 2f, radius.toFloat(), mPaint)
            drawArc(mRectF, startAngle, sweepAngle, false, mPaint)
        }
    }

    fun startLoadingAnim() {
        animAngle.start()
    }

    fun stopLoadingAnim() {
        animAngle.cancel()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val height =
            (when (heightMode) {
                MeasureSpec.UNSPECIFIED -> height.coerceAtLeast(loadingSize)
                MeasureSpec.EXACTLY -> heightSize
                else -> (height.coerceAtLeast(loadingSize) + paddingTop + paddingBottom).coerceAtMost(
                    heightSize
                )
            }).toInt()
        val width: Int =
            (when (widthMode) {
                MeasureSpec.UNSPECIFIED -> width.coerceAtLeast(loadingSize)
                MeasureSpec.EXACTLY -> widthSize
                else -> (width.coerceAtLeast(loadingSize) + paddingLeft + paddingRight).coerceAtMost(
                    widthSize
                )
            }).toInt()
        setMeasuredDimension(width, height)
    }
}