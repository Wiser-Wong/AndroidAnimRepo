package com.wiser.animationlistdemo.textanim.text

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import com.wiser.animationlistdemo.textanim.utils.CharacterUtils
import java.util.*
import kotlin.math.sqrt

class EvaporateText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    animTextView: AnimTextView? = null
) :
    BaseText(context, attrs, defStyleAttr, animTextView) {

    private var progress = 0f

    private var charTime = 400f

    private var mostCount = 20

    private var mTextHeight = 0

    /**
     * 启动动画
     */
    override fun startAnimator() {
        var n: Int = mText?.length ?: 0
        n = if (n <= 0) 1 else n

        val duration = (charTime + charTime / mostCount * (n - 1)).toLong()

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
        val bounds = Rect()
        mPaint?.getTextBounds(mText.toString(), 0, mText?.length ?: 0, bounds)
        mTextHeight = bounds.height()
    }

    override fun drawFrame(canvas: Canvas?) {
        canvas?.apply {
            var offset = startX
            var oldOffset = oldStartX

            val maxLength: Int = (mText?.length ?: 0).coerceAtLeast(mOldText?.length ?: 0)

            for (i in 0 until maxLength) {

                // draw old text
                if (i < (mOldText?.length ?: 0)) {
                    //
                    val pp: Float =
                        progress / (charTime + charTime / mostCount * ((mText?.length ?: 0) - 1))
                    mOldPaint?.textSize = mTextSize
                    val move = CharacterUtils.needMove(i, differentList)
                    if (move != -1) {
                        mOldPaint?.alpha = 255
                        var p = pp * 2f
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
                        mOldPaint?.alpha = ((1 - pp) * 255).toInt()
                        val y = startY - pp * mTextHeight
                        val width = mOldPaint?.measureText(mOldText?.get(i)?.toString() ?: "") ?: 0f
                        mOldPaint?.let {
                            drawText(
                                mOldText?.get(i)?.toString() ?: "",
                                0,
                                1,
                                oldOffset + (oldGaps[i] - width) / 2,
                                y,
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
                        alpha = if (alpha > 255) 255 else alpha
                        alpha = if (alpha < 0) 0 else alpha
                        mPaint?.alpha = alpha
                        mPaint?.textSize = mTextSize
                        val pp: Float =
                            progress / (charTime + charTime / mostCount * ((mText?.length
                                ?: 0) - 1))
                        val y = mTextHeight + startY - pp * mTextHeight
                        val width = mPaint?.measureText(mText?.get(i)?.toString() ?: "") ?: 0f
                        mPaint?.let {
                            drawText(
                                mText?.get(i)?.toString() ?: "",
                                0,
                                1,
                                offset + (gaps[i] - width) / 2,
                                y,
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