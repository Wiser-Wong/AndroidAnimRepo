package com.wiser.animationlistdemo.loginanim

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout


/**
 ***************************************
 * 项目名称:LoginPageAnim
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/3/3     15:43
 * 用途: 更新说明
 ***************************************
 */
class ClipCenterCircleFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val mPaintRectangle: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        return@lazy paint
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.apply {
            val mPath = Path()
            mPath.addCircle(width / 2f, height / 2f, height / 2f, Path.Direction.CW)
            clipPath(mPath, Region.Op.DIFFERENCE)
            val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
            drawRect(rectF, mPaintRectangle)
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
                MeasureSpec.UNSPECIFIED -> height
                MeasureSpec.EXACTLY -> heightSize
                else -> (height + paddingTop + paddingBottom).coerceAtMost(heightSize)
            }).toInt()
        val width: Int =
            (when (widthMode) {
                MeasureSpec.UNSPECIFIED -> width
                MeasureSpec.EXACTLY -> widthSize
                else -> (width + paddingLeft + paddingRight).coerceAtMost(widthSize)
            }).toInt()
        setMeasuredDimension(width, height)
    }

}