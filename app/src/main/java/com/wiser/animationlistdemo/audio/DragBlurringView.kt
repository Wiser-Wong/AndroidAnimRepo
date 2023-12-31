package com.wiser.animationlistdemo.audio


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.hoko.blur.HokoBlur
import com.hoko.blur.anno.Mode
import com.hoko.blur.anno.Scheme

import com.hoko.blur.processor.BlurProcessor



class DragBlurringView : View {
    companion object {
        private const val DOWNSAMPLE_FACTOR = 5
    }

    private var oldX: Float = 0f
    private var oldY: Float = 0f

    lateinit var blurredView: View
    private var toBlurBitmap: Bitmap? = null
    private var blurringCanvas: Canvas? = null
    private var processor: BlurProcessor = HokoBlur.with(context)
        .scheme(HokoBlur.SCHEME_NATIVE)
        .mode(HokoBlur.MODE_GAUSSIAN)
        .radius(5)
        .sampleFactor(1.0f)
        .processor()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (prepare()) {
            if (blurredView.background is ColorDrawable) {
                toBlurBitmap?.eraseColor((blurredView.background as ColorDrawable).color)
            } else {
                toBlurBitmap?.eraseColor(Color.TRANSPARENT)
            }

            blurredView.draw(blurringCanvas)
            val blurredBitmap = toBlurBitmap?.let {
                processor.blur(it)
            }

            with(canvas) {
                save()
                translate(blurredView.x - x, blurredView.y - y)
                scale(DOWNSAMPLE_FACTOR.toFloat(), DOWNSAMPLE_FACTOR.toFloat())
                blurredBitmap?.let {
                    drawBitmap(blurredBitmap, 0f, 0f, null)
                }
                restore()
            }
        }

    }

    private fun prepare(): Boolean {
        blurringCanvas ?: createBlurCanvas().also {
            blurringCanvas = it
        }
        return true
    }

    private fun createBlurCanvas(): Canvas {
        val width = blurredView.width
        val height = blurredView.height
        val scaledWidth = width / DOWNSAMPLE_FACTOR
        val scaleHeight = height / DOWNSAMPLE_FACTOR
        val b = toBlurBitmap ?: Bitmap.createBitmap(scaledWidth, scaleHeight, Bitmap.Config.ARGB_8888).also {
            toBlurBitmap = it
        }
        return Canvas(b).apply {
            scale(1.0f / DOWNSAMPLE_FACTOR, 1.0f / DOWNSAMPLE_FACTOR)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                oldX = event.rawX
                oldY = event.rawY
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - oldX
                val dy = event.rawY - oldY
                offsetLeftAndRight(dx.toInt())
                offsetTopAndBottom(dy.toInt())
                oldX = event.rawX
                oldY = event.rawY
                invalidate()
            }
        }

        return super.onTouchEvent(event)
    }



}