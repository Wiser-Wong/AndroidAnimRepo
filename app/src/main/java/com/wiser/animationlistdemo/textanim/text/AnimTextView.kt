package com.wiser.animationlistdemo.textanim.text

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class AnimTextView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var animType: AnimType = AnimType.SCALE

    private var iText: BaseText? = null

    private var onTextAnimatorListener: OnTextAnimatorListener? = null

    init {
        iText = ScaleText(context, animTextView = this)
    }

    fun setAnimType(animType: AnimType) {
        this.animType = animType
        iText = when (animType) {
            AnimType.ANVIL -> {
                if (iText == null || iText !is AnvilText) {
                    AnvilText(context, animTextView = this)
                } else {
                    iText
                }
            }
            AnimType.SPARKLE -> {
                if (iText == null || iText !is SparkleText) {
                    SparkleText(context, animTextView = this)
                } else {
                    iText
                }
            }
            AnimType.PIXELATE -> {
                if (iText == null || iText !is PixelateText) {
                    PixelateText(context, animTextView = this)
                } else {
                    iText
                }
            }
            AnimType.FALL -> {
                if (iText == null || iText !is FallText) {
                    FallText(context, animTextView = this)
                } else {
                    iText
                }
            }
            AnimType.SCALE -> {
                if (iText == null || iText !is ScaleText) {
                    ScaleText(context, animTextView = this)
                } else {
                    iText
                }
            }
            AnimType.EVAPORATE -> {
                if (iText == null || iText !is EvaporateText) {
                    EvaporateText(context, animTextView = this)
                } else {
                    iText
                }
            }
            AnimType.BURN -> {
                if (iText == null || iText !is BurnText) {
                    BurnText(context, animTextView = this)
                } else {
                    iText
                }
            }
            AnimType.RAINBOW -> {
                if (iText == null || iText !is RainBowText) {
                    RainBowText(context, animTextView = this)
                } else {
                    iText
                }
            }
            AnimType.PRINT -> {
                if (iText == null || iText !is PrintText) {
                    PrintText(context, animTextView = this)
                } else {
                    iText
                }
            }
        }
        iText?.setTextAnimatorListener(onTextAnimatorListener)
    }

    override fun onDraw(canvas: Canvas?) {
        iText?.draw(canvas)
    }

    fun setTextAnimatorListener(onTextAnimatorListener: OnTextAnimatorListener?) {
        this.onTextAnimatorListener = onTextAnimatorListener
        iText?.setTextAnimatorListener(onTextAnimatorListener)
    }

    fun setTextAnim(text: CharSequence?) {
        iText?.setTextAnim(text)
    }

    fun getAnimType(): AnimType = animType

    enum class AnimType {
        ANVIL, SPARKLE, PIXELATE, FALL, SCALE, EVAPORATE, BURN, RAINBOW, PRINT
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onTextAnimatorListener = null
    }
}