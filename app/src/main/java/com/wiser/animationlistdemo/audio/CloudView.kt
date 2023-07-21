package com.wiser.animationlistdemo.audio

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CloudView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val cloudWidth = 100f // 云朵宽度
    private val cloudHeight = 80f // 云朵高度
    private val cloudOffset = cloudWidth / 3 // 云朵偏移量
    private val cloudRadius = cloudHeight / 2 // 云朵曲线半径

    private var cloudPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private var width = 0
    private var height = 0
    private var cx = 0f // 云朵中心点x坐标
    private var cy = 0f // 云朵中心点y坐标

    private var animator: ValueAnimator? = null
    private var animDuration = 3000L // 动画时长
    private var animOffset = 0f // 动画偏移量

    init {
        startAnimation()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = MeasureSpec.getSize(widthMeasureSpec)
        height = MeasureSpec.getSize(heightMeasureSpec)
        cx = -cloudWidth / 2 - cloudOffset
        cy = height / 2f
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCloud(canvas)
    }

    private fun drawCloud(canvas: Canvas) {
        val path = Path()
        val angle = PI.toFloat() / 4 // 云朵曲线角度
        val dx = cloudRadius * sin(angle) // 横向偏移量
        val dy = cloudRadius * (1 - cos(angle)) // 垂直偏移量

        path.moveTo(cx, cy)
        path.cubicTo(
            cx - dx, cy - dy,
            cx + cloudWidth / 2 - cloudRadius, cy - cloudHeight / 2,
            cx + cloudWidth / 2, cy - cloudHeight / 2
        )
        path.cubicTo(
            cx + cloudWidth / 2 + cloudRadius, cy - cloudHeight / 2,
            cx + cloudWidth + dx, cy - dy,
            cx + cloudWidth + cloudOffset, cy
        )
        path.cubicTo(
            cx + cloudWidth + dx, cy + dy,
            cx + cloudWidth / 2 + cloudRadius, cy + cloudHeight / 2,
            cx + cloudWidth / 2, cy + cloudHeight / 2
        )
        path.cubicTo(
            cx + cloudWidth / 2 - cloudRadius, cy + cloudHeight / 2,
            cx - dx, cy + dy,
            cx, cy
        )

        canvas.drawPath(path, cloudPaint)
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, cloudWidth + cloudOffset)
            .apply {
                repeatCount = ValueAnimator.INFINITE
                duration = animDuration
                addUpdateListener { valueAnimator ->
                    animOffset = valueAnimator.animatedValue as Float
                    invalidate()
                }
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationRepeat(animation: Animator) {
                        cx = -cloudWidth / 2 - cloudOffset
                    }
                })
                start()
            }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.let {
            it.pause()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator?.let {
            it.resume()
        }
    }
}

