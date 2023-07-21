package com.wiser.animationlistdemo.textanim.text

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.core.animation.addListener
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.textanim.utils.CharacterUtils
import java.util.*
import kotlin.math.sqrt

class SparkleText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    animTextView: AnimTextView? = null
) :
    BaseText(context, attrs, defStyleAttr, animTextView) {

    private val backPaint: Paint? by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = (animTextView?.background as? ColorDrawable)?.color ?: Color.WHITE
        paint.style = Paint.Style.FILL
        return@lazy paint
    }

    private val sparkBitmap: Bitmap? by lazy {
        BitmapFactory.decodeResource(
            resources,
            R.drawable.sparkle
        )
    }

    private var progress = 0f

    private var charTime = 400f

    private var mostCount = 20

    private var upDistance = 0f

    /**
     * 启动动画
     */
    override fun startAnimator() {
        var n: Int = mText?.length ?: 0
        n = if (n <= 0) 1 else n

        val duration = (charTime + charTime / mostCount * (n - 1)).toLong()

        val valueAnimator = ValueAnimator.ofFloat(0f, duration.toFloat()).setDuration(duration)
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
        upDistance = bounds.height().toFloat()
    }

    override fun drawFrame(canvas: Canvas?) {
        canvas?.apply {
            var offset = startX
            var oldOffset = oldStartX

            val maxLength: Int = Math.max(mText?.length ?: 0, mOldText?.length ?: 0)
            val percent: Float =
                progress / (charTime + charTime / mostCount * ((mText?.length ?: 0) - 1))

            mPaint?.alpha = 255
            mPaint?.textSize = mTextSize

            for (i in 0 until maxLength) {
                // draw new text
                if (i < (mText?.length ?: 0)) {
                    if (!CharacterUtils.stayHere(i, differentList)) {
                        val width = mPaint?.measureText(mText?.get(i)?.toString())
                        mPaint?.let {
                            canvas.drawText(
                                mText?.get(i)?.toString() ?: "", 0, 1, offset, startY,
                                it
                            )
                        }
                        if (percent < 1) {
                            drawSparkle(
                                canvas,
                                offset,
                                startY - (1 - percent) * upDistance,
                                width ?: 0f
                            )
                        }
                        backPaint?.let {
                            canvas.drawRect(
                                offset,
                                startY * 1.2f - (1 - percent) * (upDistance + startY * 0.2f),
                                offset + gaps[i],
                                startY * 1.2f,
                                it
                            )
                        }
                    }
                    offset += gaps[i]
                }
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
                            canvas.drawText(
                                mOldText?.get(i)?.toString() ?: "", 0, 1, distX, startY,
                                it
                            )
                        }
                    } else {
                        var p = pp * 3.5f
                        p = if (p > 1) 1f else p
                        mOldPaint?.alpha = (255 * (1 - p)).toInt()
                        mOldPaint?.let {
                            canvas.drawText(
                                mOldText?.get(i)?.toString() ?: "", 0, 1, oldOffset, startY,
                                it
                            )
                        }
                    }
                    oldOffset += oldGaps[i]
                }
            }
        }
    }


    private fun drawSparkle(canvas: Canvas, offset: Float, startY: Float, width: Float) {
        val random = Random()
        for (i in 0..7) {
            getRandomSpark(random)?.let {
                canvas.drawBitmap(
                    it,
                    (offset + random.nextDouble() * width).toFloat(),
                    (startY - random
                        .nextGaussian() * sqrt(upDistance.toDouble())).toFloat(),
                    mPaint
                )
            }
        }
    }

    private fun getRandomSpark(random: Random): Bitmap? {
        val dstWidth = random.nextInt(25) + 1
        return sparkBitmap?.let { Bitmap.createScaledBitmap(it, dstWidth, dstWidth, false) }
    }
}