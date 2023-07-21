package com.wiser.animationlistdemo.textanim.text

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import com.wiser.animationlistdemo.textanim.utils.CharacterUtils
import java.util.*

class ScaleText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    animTextView: AnimTextView? = null
) :
    BaseText(context, attrs, defStyleAttr, animTextView) {

    private var progress = 0f

    private var charTime = 400f

    private var mostCount = 20

    private var duration: Long = 0

    /**
     * 启动动画
     */
    override fun startAnimator() {
        var n: Int = mText?.length ?: 0
        n = if (n <= 0) 1 else n
        duration = (charTime + charTime / mostCount * (n - 1)).toLong()

        val valueAnimator = ValueAnimator.ofFloat(0f, duration.toFloat()).setDuration(duration)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            progress = animation.animatedValue as Float
            if (animTextView != null) {
                animTextView.invalidate()
            } else {
                invalidate()
            }
        }
        valueAnimator.addListener(onStart = {
            onTextAnimatorListener?.onStart(this)
        }, onEnd = {
            onTextAnimatorListener?.onEnd(this)
        })
        valueAnimator.start()
    }

    override fun onPrepareAnimate() {
    }

    override fun drawFrame(canvas: Canvas?) {
        canvas?.apply {
            var offset = startX
            var oldOffset = oldStartX

            val maxLength: Int = (mText?.length ?: 0).coerceAtLeast(mOldText?.length ?: 0)

            for (i in 0 until maxLength) {

                // draw old text
                if (i < (mOldText?.length ?: 0)) {
                    val percent = progress / duration
                    val move = CharacterUtils.needMove(i, differentList)
                    if (move != -1) {
                        mOldPaint?.textSize = mTextSize
                        mOldPaint?.alpha = 255
                        var p = percent * 2f
                        p = if (p > 1) 1f else p
                        val distX =
                            CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps)
                        mOldPaint?.let {
                            drawText(
                                mOldText?.get(i)?.toString() ?: "", 0, 1, distX, startY,
                                it
                            )
                        }
                    } else {
                        mOldPaint?.alpha = ((1 - percent) * 255).toInt()
                        mOldPaint?.textSize = mTextSize * (1 - percent)
                        val width = mOldPaint?.measureText(mOldText?.get(i)?.toString() ?: "") ?: 0f
                        mOldPaint?.let {
                            drawText(
                                mOldText?.get(i)?.toString() ?: "",
                                0,
                                1,
                                oldOffset + (oldGaps[i] - width) / 2,
                                startY,
                                it
                            )
                        }
                    }
                    oldOffset += oldGaps[i]
                }

                // draw new text
                if (i < (mText?.length ?: 0)) {
                    if (!CharacterUtils.stayHere(i, differentList)) {
                        var alpha =
                            (255f / charTime * (progress - charTime * i / mostCount)).toInt()
                        if (alpha > 255) alpha = 255
                        if (alpha < 0) alpha = 0
                        var size = mTextSize * 1f / charTime * (progress - charTime * i / mostCount)
                        if (size > mTextSize) size = mTextSize
                        if (size < 0) size = 0f
                        mPaint?.alpha = alpha
                        mPaint?.textSize = size
                        val width = mPaint?.measureText(mText?.get(i)?.toString() ?: "") ?: 0f
                        mPaint?.let {
                            drawText(
                                mText?.get(i)?.toString() ?: "",
                                0,
                                1,
                                offset + (gaps[i] - width) / 2,
                                startY,
                                it
                            )
                        }
                    }
                    offset += gaps[i]
                }
            }
        }
    }

}