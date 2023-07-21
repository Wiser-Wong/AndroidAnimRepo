package com.wiser.animationlistdemo.audio

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.delay

/**
 * @date: 2023/6/8 18:05
 * @author: jiaruihua
 * @desc :
 *
 */
class ScrollImageView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {


    private val matrix = Matrix()
    private var currentsX = 0f
    private var currentY = 0f

    private var bitmapWidth = 0
    private var bitmapHeight = 0

    private var width = 0
    private var height = 0

    var ratioY = 0f
    var dx = 0f


    init {
        startAni()

    }


    fun startAni() {
        postDelayed({
            transRight()
        }, 1000)


    }

    var tranAni: ValueAnimator? = null

    var rLastX = 0F
    var lLastX = 0F
    val DURATION = 15000L
    private fun transRight() {
        tranAni = ValueAnimator.ofFloat(0f, dx).apply {
            duration = DURATION
            addUpdateListener {
                println("-------(it.animatedValue as Float)=${(it.animatedValue as Float)}-rLastX=$rLastX--currentX=$currentsX--")
                currentsX = (it.animatedValue as Float) - rLastX
                rLastX = it.animatedValue as Float
                invalidate()
            }
            doOnEnd {
                rLastX = 0f
                lLastX = dx
                transLeft()
            }
            start()
        }
    }

    private fun transLeft() {
        tranAni = ValueAnimator.ofFloat(dx, 0f).apply {
            duration = DURATION
            addUpdateListener {
                currentsX = (it.animatedValue as Float) - lLastX
                lLastX = it.animatedValue as Float
                invalidate()
            }
            doOnEnd {
                lLastX = 0f
                rLastX = 0f
                transRight()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        destory()
    }

    fun destory() {
        tranAni?.cancel()
        tranAni = null
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        imageMatrix.postTranslate(-currentsX, 0f)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取ImageView的实际宽度和高度
        width = measuredWidth
        height = measuredHeight
        //限制宽度不大于ImageView的宽度
        if (drawable != null) {
            if (bitmapWidth == 0) {
                bitmapWidth = drawable.intrinsicWidth
                bitmapHeight = drawable.intrinsicHeight
                ratioY = height / bitmapHeight.toFloat()
                dx = bitmapWidth * (ratioY) - width
                matrix.postScale(ratioY, ratioY)

                imageMatrix = matrix
                val src = RectF(0F, 0f, width.toFloat(), height.toFloat())
                val dst = RectF(0f, 0f, width.toFloat(), height.toFloat())
                matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER)
                matrix.postTranslate(0F, 0f)
            }


        }
    }

}