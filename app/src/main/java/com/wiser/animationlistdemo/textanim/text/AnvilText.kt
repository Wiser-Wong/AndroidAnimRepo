package com.wiser.animationlistdemo.textanim.text

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.BounceInterpolator
import androidx.core.animation.addListener
import com.wiser.animationlistdemo.textanim.utils.CharacterUtils

class AnvilText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    animTextView: AnimTextView? = null
) :
    BaseText(context, attrs, defStyleAttr, animTextView) {

    private val bitmapPaint: Paint? by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        return@lazy paint
    }

    private val mMatrix: Matrix? by lazy { Matrix() }

    private val smokes by lazy { arrayOfNulls<Bitmap>(50) }

    private val ANIMA_DURATION = 800f

    private var mTextHeight = 0

    private var mTextWidth = 0

    private var progress = 0f

    private var dstWidth = 0f

    init {
        // 加载 drawable
        try {
            for (j in 0..49) {
                val drawable: String = if (j < 10) {
                    "wenzi000$j"
                } else {
                    "wenzi00$j"
                }
                val imgId = resources.getIdentifier(
                    drawable,
                    "drawable",
                    context.packageName
                )
                smokes[j] = BitmapFactory.decodeResource(resources, imgId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 启动动画
     */
    override fun startAnimator() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            .setDuration(ANIMA_DURATION.toLong())
        valueAnimator.addUpdateListener { animation ->
            progress = animation.animatedValue as Float
            if (animTextView != null)
                animTextView.invalidate()
            else invalidate()
        }
        valueAnimator.addListener(onStart = {
            onTextAnimatorListener?.onStart(this)
        }, onEnd = {
            onTextAnimatorListener?.onEnd(this)
        })
        valueAnimator.start()
        if (smokes.isNotEmpty()) {
            mMatrix?.reset()
            dstWidth = mTextWidth * 1.2f
            if (dstWidth < 604f) dstWidth = 604f
            mMatrix?.postScale(dstWidth / smokes[0]?.width?.toFloat()!!, 1f)
        }
    }

    override fun onPrepareAnimate() {
        val bounds = Rect()
        mPaint?.getTextBounds(mText.toString(), 0, mText?.length ?: 0, bounds)
        mTextHeight = bounds.height()
        mTextWidth = bounds.width()
    }

    override fun drawFrame(canvas: Canvas?) {
        canvas?.apply {
            var offset: Float = startX
            var oldOffset: Float = oldStartX
            val maxLength: Int = (mText?.length ?: 0).coerceAtLeast(mOldText?.length ?: 0)
            val percent = progress
            var showSmoke = false
            for (i in 0 until maxLength) {

                // draw old text
                if (i < (mOldText?.length ?: 0)) {
                    mOldPaint?.textSize = mTextSize
                    val move: Int = CharacterUtils.needMove(i, differentList)
                    if (move != -1) {
                        mOldPaint?.alpha = 255
                        var p = percent * 2f
                        p = if (p > 1) 1f else p
                        val distX: Float =
                            CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps)
                        mOldPaint?.let {
                            drawText(
                                mOldText?.get(i)?.toString() ?: "", 0, 1, distX, startY,
                                it
                            )
                        }
                    } else {
                        var p = percent * 2f
                        p = if (p > 1) 1f else p
                        mOldPaint?.alpha = ((1 - p) * 255).toInt()
                        mOldPaint?.let {
                            drawText(
                                mOldText?.get(i)?.toString() ?: "", 0, 1, oldOffset, startY,
                                it
                            )
                        }
                    }
                    oldOffset += oldGaps[i]
                }

                // draw new text
                if (i < (mText?.length ?: 0)) {
                    if (!CharacterUtils.stayHere(i, differentList)) {
                        showSmoke = true
                        val interpolation = BounceInterpolator().getInterpolation(percent)
                        mPaint?.alpha = 255
                        mPaint?.textSize = mTextSize
                        val y: Float = startY - (1 - interpolation) * mTextHeight * 2
                        val width: Float = mPaint?.measureText(mText?.get(i)?.toString()) ?: 0f
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
            if (percent > 0.3 && percent < 1) {
                if (showSmoke) {
                    drawSmokes(this, percent)
                }
            }
        }
    }


    private fun drawSmokes(canvas: Canvas?, percent: Float) {
        canvas?.apply {
            var b = smokes[0]
            try {
                var index = (50 * percent).toInt()
                if (index < 0) index = 0
                if (index >= 50) index = 49
                b = smokes[index]
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (b != null) {
                val dx: Float =
                    if (((animTextView?.width ?: width) - dstWidth) / 2 > 0) ((animTextView?.width
                        ?: width) - dstWidth) / 2 else 0f
                translate(dx, ((animTextView?.height ?: height) - b.height) / 2f)
                drawBitmap(b, mMatrix!!, bitmapPaint)
            }
        }
    }
}