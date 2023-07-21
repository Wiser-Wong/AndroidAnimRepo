package com.wiser.animationlistdemo.loginanim

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import com.wiser.animationlistdemo.R

/**
 ***************************************
 * 项目名称:LoginPageAnim
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/3/3     15:43
 * 用途: 更新说明
 ***************************************
 */
class WinkAnim @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val winkDuration = 150L

    private var winkCount = 0

    private val mCamera: Camera = Camera()
    private val mBitmap: Bitmap by lazy {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bear_eye)
        getNewBitmap(
            bitmap, resources?.getDimension(R.dimen.ksl_dp_12)?.toInt() ?: bitmap.width,
            resources?.getDimension(R.dimen.ksl_dp_12)?.toInt() ?: bitmap.height
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

    private var animDegreeAngleY = ObjectAnimator.ofFloat(this, "degreeAngleY", 0f, 90f)
    private var animDegreeAngleReset = ObjectAnimator.ofFloat(this, "degreeAngleY", -90f, 0f)

    init {
        val displayMetrics = resources.displayMetrics
        val newZ = -displayMetrics.density * 6
        mCamera.setLocation(0f, 0f, newZ)
        animDegreeAngleY.duration = winkDuration
        animDegreeAngleReset.duration = winkDuration
        animDegreeAngleY.doOnEnd {
            winkCount++
            animDegreeAngleReset.start()
        }
        animDegreeAngleReset.doOnEnd {
            if (winkCount == 2) {
                winkCount = 0
                animDegreeAngleY.startDelay = 1000
            } else {
                animDegreeAngleY.startDelay = 0
            }
            animDegreeAngleY.start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animDegreeAngleY.start()
    }

    override fun onDetachedFromWindow() {
        animDegreeAngleReset.cancel()
        animDegreeAngleY.cancel()
        super.onDetachedFromWindow()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        drawWink(canvas)
    }

    private fun drawWink(canvas: Canvas?) {
        canvas?.apply {
            val bWidth = mBitmap.width
            val bHeight = mBitmap.height
            val centerX = width / 2f
            val centerY = height / 2f
            val x = centerX - bWidth / 2f
            val y = centerY - bHeight / 2f

            //静态
            save()
            translate(centerX, centerY)
            mCamera.save()
            mCamera.rotateX(degreeAngleY)
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

    private fun getNewBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }
}