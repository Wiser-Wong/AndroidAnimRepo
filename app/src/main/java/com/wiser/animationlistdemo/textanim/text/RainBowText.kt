package com.wiser.animationlistdemo.textanim.text

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.util.AttributeSet
import kotlin.math.roundToInt

class RainBowText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    animTextView: AnimTextView? = null
) :
    BaseText(context, attrs, defStyleAttr, animTextView) {

    private var mTextWidth = 0f
    private var mLinearGradient: LinearGradient? = null
    private val mMatrix: Matrix? by lazy { Matrix() }
    private var mTranslate = 0f
    private val dx by lazy { (7 * Resources.getSystem().displayMetrics.density).roundToInt() }
    private var colors =
        intArrayOf(-0xd4de, -0x80de, -0x1200de, -0xdd00de, -0xdd0b01, -0xddc601, -0xabff09)

    fun setColors(colors: IntArray?) {
        this.colors = colors!!
    }

    override fun onPrepareAnimate() {
        mTextWidth = mPaint?.measureText(mText, 0, mText?.length ?: 0) ?: 0f
        mTextWidth = (100 * Resources.getSystem().displayMetrics.density).coerceAtLeast(mTextWidth)
        if (mTextWidth > 0) {
            mLinearGradient = LinearGradient(
                0f, 0f, mTextWidth, 0f,
                colors, null, Shader.TileMode.MIRROR
            )
            mPaint?.shader = mLinearGradient
        }
    }

    override fun startAnimator() {
        onTextAnimatorListener?.onStart(this)
        if (animTextView != null) {
            animTextView.invalidate()
        } else {
            invalidate()
        }
    }

    override fun drawFrame(canvas: Canvas?) {
        canvas?.apply {
            if (mMatrix != null && mLinearGradient != null) {
                mTranslate += dx
                mMatrix?.setTranslate(mTranslate, 0f)
                mLinearGradient?.setLocalMatrix(mMatrix)
                mPaint?.let {
                    drawText(
                        mText ?: "", 0, mText?.length ?: 0, startX, startY,
                        it
                    )
                }
                animTextView?.postInvalidateDelayed(100)
                onTextAnimatorListener?.onEnd(this@RainBowText)
            }
        }
    }

}