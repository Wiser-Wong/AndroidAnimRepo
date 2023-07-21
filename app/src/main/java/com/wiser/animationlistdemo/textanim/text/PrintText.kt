package com.wiser.animationlistdemo.textanim.text

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class PrintText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    animTextView: AnimTextView? = null
) :
    BaseText(context, attrs, defStyleAttr, animTextView) {

    private var currentLength = 0

    override fun onPrepareAnimate() {
    }

    override fun startAnimator() {
        currentLength = 0
        onTextAnimatorListener?.onStart(this)
        if (animTextView != null) {
            animTextView.invalidate()
        } else {
            invalidate()
        }
    }

    override fun drawFrame(canvas: Canvas?) {
        canvas?.apply {
            mPaint?.let { drawText(mText ?: "", 0, currentLength, startX, startY, it) }
            if (currentLength < (mText?.length ?: 0)) {
                currentLength++
                animTextView?.postInvalidateDelayed(100)
            } else {
                onTextAnimatorListener?.onEnd(this@PrintText)
            }
        }
    }

}