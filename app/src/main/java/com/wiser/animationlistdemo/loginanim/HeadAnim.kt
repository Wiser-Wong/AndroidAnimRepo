package com.wiser.animationlistdemo.loginanim

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
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
class HeadAnim @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val headDuration = 800L

    private val angle = 25f

    private val mCamera: Camera = Camera()
    private val mBitmap: Bitmap by lazy {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bear_head)
        getNewBitmap(
            bitmap, resources?.getDimension(R.dimen.ksl_dp_150)?.toInt() ?: bitmap.width,
            resources?.getDimension(R.dimen.ksl_dp_150)?.toInt() ?: bitmap.height
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

    private var animDegreeAngleY = ObjectAnimator.ofFloat(this, "degreeAngleY", 0f, -angle)
    private var resetAnimDegreeAngleY = ObjectAnimator.ofFloat(this, "degreeAngleY", -angle, 0f)

    init {
        val displayMetrics = resources.displayMetrics
        val newZ = -displayMetrics.density * 6
        mCamera.setLocation(0f, 0f, newZ)
        animDegreeAngleY.duration = headDuration
        resetAnimDegreeAngleY.duration = headDuration
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        animDegreeAngleY.cancel()
        resetAnimDegreeAngleY.cancel()
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
            clipRect(-centerX, -centerY, centerX, 0f)
            mCamera.restore()
            translate(-centerX, -centerY)
            drawBitmap(mBitmap, x, y, mPaint)
            restore()

            //3D旋转
            save()
            translate(centerX, centerY)
            mCamera.save()
            mCamera.applyToCanvas(canvas)
            clipRect(-centerX, 0f, centerX, centerY)
            mCamera.restore()
            translate(-centerX, -centerY)
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

    fun startHeadAnim() {
        animDegreeAngleY.start()
    }

    fun resetHeadAnim() {
        resetAnimDegreeAngleY.start()
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