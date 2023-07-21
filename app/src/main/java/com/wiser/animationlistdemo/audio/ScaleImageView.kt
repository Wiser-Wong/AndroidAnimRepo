package com.wiser.animationlistdemo.audio

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView

/**
 * @date: 2023/6/8 18:05
 * @author: jiaruihua
 * @desc :
 *
 * 实现一个缩放的 imageview
 *
 */
class ScaleImageView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val ivMatrix: Matrix by lazy { Matrix() }
    private val startMatrixValues = FloatArray(9)
    private var startTime: Long = 0
    private var scaleFactor = 1f
    private var isAnimating = false

    init {

        postDelayed({startAnimation()},1000)
    }

    fun startAnimation() {
        if (isAnimating) return
        isAnimating = true

        // 这是动画持续的时间
        val animationDuration = 10000L

        // 获取ImageView的中心点
        val centerX = width / 2f
        val centerY = height / 2f

        // 获取原始图像的尺寸
        val drawableWidth = drawable?.intrinsicWidth ?: 0
        val drawableHeight = drawable?.intrinsicHeight ?: 0

        // 将 ImageView 的 drawable 转成 Bitmap
        val drawableBitmap = (drawable as? BitmapDrawable)?.bitmap ?: return

        //计算放大比例
        val scale = if (drawableWidth > width || drawableHeight > height) {
            scaleFactor = if (drawableWidth > drawableHeight) {
                width.toFloat() / drawableWidth.toFloat() * 1.5f
            } else {
                height.toFloat() / drawableHeight.toFloat() * 1.5f
            }
            scaleFactor
        } else {
            1.5f
        }

        // 设置矩阵放大并居中
        ivMatrix.setTranslate(centerX - drawableWidth / 2f, centerY - drawableHeight / 2f)
        ivMatrix.getValues(startMatrixValues)
        ivMatrix.postScale(scale, scale, centerX, centerY)
        setImageMatrix(ivMatrix)

        // 定义动画
        val anim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                ivMatrix.setValues(startMatrixValues)
                ivMatrix.postScale(
                    1 + (scaleFactor - 1) * interpolatedTime,
                    1 + (scaleFactor - 1) * interpolatedTime,
                    centerX,
                    centerY
                )
                setImageMatrix(ivMatrix)
            }

            override fun willChangeBounds(): Boolean {
                return false
            }
        }

        // 启动动画
        anim.duration = animationDuration
        startAnimation(anim)

        // 在动画结束后，缩小图片并恢复矩阵
        postDelayed({
            ivMatrix.setValues(startMatrixValues)
            setImageMatrix(ivMatrix)
            scaleFactor = 1f
            isAnimating = false
        }, animationDuration)
    }

}