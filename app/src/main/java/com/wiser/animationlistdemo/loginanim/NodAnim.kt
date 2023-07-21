package com.wiser.animationlistdemo.loginanim

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd

/**
 ***************************************
 * 项目名称:LoginPageAnim
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/3/3     15:43
 * 用途: 更新说明
 ***************************************
 */
class NodAnim @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val nodDuration = 2000L

    private val mCanvas by lazy { Canvas() }
    private val mCamera: Camera = Camera()
    private val mBitmap: Bitmap by lazy {
        Bitmap.createBitmap(
            measuredWidth + 50, measuredHeight + 50,
            Bitmap.Config.ARGB_8888
        )
    }

    private val mPaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        return@lazy paint
    }

    private var degreeAngleY = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var animDegreeAngleY = ObjectAnimator.ofFloat(this, "degreeAngleY", 0f, -15f)
    private var animDegreeAngleReset = ObjectAnimator.ofFloat(this, "degreeAngleY", -15f, 0f)

    init {
        val displayMetrics = resources.displayMetrics
        val newZ = -displayMetrics.density * 6
        mCamera.setLocation(0f, 0f, newZ)
        animDegreeAngleY.duration = nodDuration
        animDegreeAngleReset.duration = nodDuration
        animDegreeAngleY.doOnEnd {
            animDegreeAngleReset.start()
        }
        animDegreeAngleReset.doOnEnd {
            animDegreeAngleY.startDelay = 1000
            animDegreeAngleY.start()
        }
        setWillNotDraw(false)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        animDegreeAngleY.start()
    }

    override fun onDetachedFromWindow() {
        animDegreeAngleReset.cancel()
        animDegreeAngleY.cancel()
        super.onDetachedFromWindow()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
//        super.dispatchDraw(mCanvas)
//        drawWink(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mCanvas.setBitmap(mBitmap)
    }

    private fun drawWink(canvas: Canvas?) {
        canvas?.apply {
            val bWidth = mBitmap.width
            val bHeight = mBitmap.height + 300
            val centerX = width / 2f
            val centerY = height / 2f + 150
            val x = centerX - bWidth / 2f
            val y = centerY - bHeight / 2f

            //静态
            save()
            translate(centerX, centerY)
            mCamera.save()
            clipRect(-bWidth.toFloat(), 0f, bWidth.toFloat(), bHeight.toFloat())
            mCamera.applyToCanvas(canvas)
            mCamera.restore()
            translate(-centerX, -centerY)
            drawBitmap(mBitmap, x, y, mPaint)
            restore()

            //3D旋转
            save()
            translate(centerX, centerY)
            mCamera.save()
            mCamera.rotateX(degreeAngleY)
            mCamera.applyToCanvas(canvas)
            mCamera.restore()
            translate(-centerX, -centerY)
            clipRect(x, 0f, x + bWidth, centerY)
            drawBitmap(mBitmap, x, y, mPaint)
            restore()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val height =
            (when (heightMode) {
                MeasureSpec.UNSPECIFIED -> mBitmap.height
                MeasureSpec.EXACTLY -> heightSize
                else -> (mBitmap.height + paddingTop + paddingBottom).coerceAtMost(heightSize)
            }).toInt()
        val width: Int =
            (when (widthMode) {
                MeasureSpec.UNSPECIFIED -> mBitmap.width
                MeasureSpec.EXACTLY -> widthSize
                else -> (mBitmap.width + paddingLeft + paddingRight).coerceAtMost(widthSize)
            }).toInt()
        setMeasuredDimension(width, height)
    }

}