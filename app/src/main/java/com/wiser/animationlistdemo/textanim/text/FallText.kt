package com.wiser.animationlistdemo.textanim.text

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.addListener
import com.wiser.animationlistdemo.textanim.utils.CharacterUtils
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class FallText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    animTextView: AnimTextView? = null
) :
    BaseText(context, attrs, defStyleAttr, animTextView) {

    private var progress = 0f

    private var charTime = 400f

    private var mostCount = 20

    private var mTextHeight = 0f

    private val interpolator: OvershootInterpolator? by lazy { OvershootInterpolator() }

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
        mTextHeight = bounds.height().toFloat()
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
                    val percent: Float =
                        progress / (charTime + charTime / mostCount * ((mText?.length ?: 0) - 1))
                    mOldPaint?.textSize = mTextSize
                    val move = CharacterUtils.needMove(i, differentList)
                    if (move != -1) {
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
                        mOldPaint?.alpha = 255
                        val centerX = oldOffset + oldGaps[i] / 2
                        val width = mOldPaint?.measureText(mOldText?.get(i)?.toString() ?: "") ?: 0f
                        var p = percent * 1.4f
                        p = if (p > 1) 1f else p
                        p = interpolator?.getInterpolation(p) ?: 0f
                        var angle = (1 - p) * Math.PI
                        if (i % 2 == 0) {
                            angle = p * Math.PI + Math.PI
                        }
                        val disX = centerX + (width / 2 * cos(angle)).toFloat()
                        val disY = startY + (width / 2 * sin(angle)).toFloat()
//                        mOldPaint?.style = Paint.Style.STROKE
                        val path = Path()
                        path.moveTo(disX, disY)
                        // (m+x)/2=a ,x=2a-m
                        // (n+y)/2=b ,y=2b-n
                        path.lineTo(2 * centerX - disX, 2 * startY - disY)
                        if (percent <= 0.7) {
                            mOldPaint?.let {
                                canvas.drawTextOnPath(
                                    mOldText?.get(i)?.toString() ?: "",
                                    path,
                                    0f,
                                    0f,
                                    it
                                )
                            }
                        } else {
                            val p2 = ((percent - 0.7) / 0.3f).toFloat()
                            mOldPaint?.alpha = ((1 - p2) * 255).toInt()
                            val y = (p2 * mTextHeight)
                            val path2 = Path()
                            path2.moveTo(disX, disY + y)
                            path2.lineTo(2 * centerX - disX, 2 * startY - disY + y)
                            mOldPaint?.let {
                                canvas.drawTextOnPath(
                                    mOldText?.get(i)?.toString() ?: "",
                                    path2,
                                    0f,
                                    0f,
                                    it
                                )
                            }
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
                        var size = mTextSize * 1f / charTime * (progress - charTime * i / mostCount)
                        size = if (size > mTextSize) mTextSize else size
                        size = if (size < 0) 0f else size
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