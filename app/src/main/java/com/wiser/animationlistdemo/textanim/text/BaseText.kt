package com.wiser.animationlistdemo.textanim.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.wiser.animationlistdemo.textanim.utils.CharacterUtils

abstract class BaseText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val animTextView: AnimTextView? = null
) : AppCompatTextView(context, attrs, defStyleAttr), IText {

    val mPaint: Paint? by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = animTextView?.currentTextColor ?: currentTextColor
        paint.style = Paint.Style.FILL
        paint.typeface = animTextView?.typeface
        return@lazy paint
    }

    val mOldPaint: Paint? by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = animTextView?.currentTextColor ?: currentTextColor
        paint.style = Paint.Style.FILL
        paint.typeface = animTextView?.typeface
        return@lazy paint
    }

    var mText: CharSequence? = null

    var mOldText: CharSequence? = null

    val mTextSize by lazy { animTextView?.textSize ?: textSize }

    /**
     * the gap between characters
     */
    var gaps = FloatArray(100)

    var oldGaps = FloatArray(100)

    var differentList: MutableList<CharacterDiffResult> = ArrayList()

    var oldStartX = 0f

    var startX = 0f

    var startY = 0f

    var onTextAnimatorListener: OnTextAnimatorListener? = null

    init {
        animTextView?.postDelayed({ prepareAnimate() }, 50)
        mText = animTextView?.text
        mOldText = animTextView?.text
    }

    fun setTextAnimatorListener(onTextAnimatorListener: OnTextAnimatorListener?) {
        this.onTextAnimatorListener = onTextAnimatorListener
    }

    override fun setTextAnim(text: CharSequence?) {
        animTextView?.text = text
        mOldText = mText
        mText = text
        prepareAnimate()
        onPrepareAnimate()
        startAnimator()
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint?.color = animTextView?.currentTextColor ?: currentTextColor
        mOldPaint?.color = animTextView?.currentTextColor ?: currentTextColor
        drawFrame(canvas)
    }

    private fun prepareAnimate() {
        mPaint?.textSize = animTextView?.textSize ?: mTextSize
        for (i in 0 until (mText?.length ?: 0)) {
            gaps[i] = mPaint?.measureText(mText?.get(i)?.toString()) ?: 0f
        }
        mOldPaint?.textSize = mTextSize
        for (i in 0 until (mOldText?.length ?: 0)) {
            oldGaps[i] = mOldPaint?.measureText(mOldText?.get(i).toString()) ?: 0f
        }
        oldStartX =
            ((animTextView?.measuredWidth ?: measuredWidth) - (animTextView?.compoundPaddingLeft
                ?: compoundPaddingLeft) - (animTextView?.paddingLeft
                ?: paddingLeft) - (mOldPaint?.measureText(mOldText.toString())
                ?: 0f)) / 2f
        startX =
            ((animTextView?.measuredWidth ?: measuredWidth) - (animTextView?.compoundPaddingLeft
                ?: compoundPaddingLeft) - (animTextView?.paddingLeft
                ?: paddingLeft) - (mPaint?.measureText(mText.toString())
                ?: 0f)) / 2f
        startY = animTextView?.baseline?.toFloat() ?: 0f
        differentList.clear()
        differentList.addAll(CharacterUtils.diff(mOldText ?: "", mText ?: ""))
    }
}