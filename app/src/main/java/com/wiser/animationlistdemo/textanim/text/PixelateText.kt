package com.wiser.animationlistdemo.textanim.text

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener


class PixelateText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    animTextView: AnimTextView? = null
) :
    BaseText(context, attrs, defStyleAttr, animTextView) {

    private val maxBlurRadius = 25f
    // 0-25
    private var blurRadius = maxBlurRadius

    private var progress = 0f

    private var charTime = 1000f

    private var mostCount = 20

    private val mMatrix: Matrix by lazy { Matrix() }

    private val metrics: DisplayMetrics = DisplayMetrics()

    private var isEnd: Boolean = true

    private val bitmap: Bitmap? by lazy {
        Bitmap.createBitmap(
            animTextView?.measuredWidth ?: measuredWidth,
            animTextView?.measuredHeight ?: measuredHeight,
            Bitmap.Config.ARGB_4444
        )
    }

    private val pixCanvas: Canvas? by lazy { bitmap?.let { Canvas(it) } }

    private val pixPaint: Paint? by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = (animTextView?.background as? ColorDrawable)?.color ?: Color.BLACK
        paint.style = Paint.Style.FILL
        return@lazy paint
    }

    init {
        (context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay?.getMetrics(
            metrics
        )
    }

    override fun onPrepareAnimate() {
    }

    /**
     * 启动动画
     */
    override fun startAnimator() {
        isEnd = false
        var n = mText?.length ?: 0
        n = if (n <= 0) 1 else n

        val duration = (charTime + charTime / mostCount * (n - 1)).toLong()

        val valueAnimator = ValueAnimator.ofFloat(0f, duration.toFloat()).setDuration(duration)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            progress = animation.animatedValue as Float
            val present = progress / duration
            if (present > 0) {
                blurRadius = maxBlurRadius - (progress / duration) * maxBlurRadius
                if (blurRadius == 0f) {
                    blurRadius = 0.01f
                }
            }
            println("========>>${progress / duration}")
            println("========>>blurRadius===>>${blurRadius}")
            if (animTextView != null) {
                animTextView.invalidate()
            } else {
                invalidate()
            }
        }
        valueAnimator.addListener(onStart = {
            onTextAnimatorListener?.onStart(this)
        }, onEnd = {
            isEnd = true
            onTextAnimatorListener?.onEnd(this)
        })
        valueAnimator.start()
    }

    override fun drawFrame(canvas: Canvas?) {
        canvas?.apply {
            val offset = startX
            val oldOffset = oldStartX

            val pp = progress / (charTime + charTime / mostCount * (mText!!.length - 1))

            var alpha = (255 * pp).toInt()
            var oldAlpha = (255 * (1 - pp)).toInt()

            alpha = if (alpha > 255) 255 else alpha
            alpha = if (alpha < 0) 0 else alpha

            oldAlpha = if (oldAlpha > 255) 255 else oldAlpha
            oldAlpha = if (oldAlpha < 0) 0 else oldAlpha

            mOldPaint?.alpha = oldAlpha
            mPaint?.alpha = alpha

            pixCanvas?.drawColor((animTextView?.background as? ColorDrawable)?.color ?: Color.BLACK)
            mOldPaint?.textSize = mTextSize
            mOldPaint?.let {
                pixCanvas?.drawText(
                    mOldText ?: "", 0, mOldText?.length ?: 0, oldOffset, startY,
                    it
                )
            }
            mPaint?.textSize = mTextSize
            mPaint?.let {
                pixCanvas?.drawText(
                    mText ?: "",
                    0,
                    mText?.length ?: 0,
                    offset,
                    startY,
                    it
                )
            }

            if (isEnd) {
                bitmap?.let { it -> canvas.drawBitmap(it, mMatrix, pixPaint) }
            } else {
                bitmap?.let { blurBitmap(context, it, blurRadius)?.let { it1 -> canvas.drawBitmap(it1, mMatrix, pixPaint) } }
            }
        }
    }

    /**
     * @param context 上下文对象
     * @param srcBitmap   需要模糊的图片
     * @return 模糊处理后的Bitmap
     */
    private fun blurBitmap(context: Context?, srcBitmap: Bitmap, blurRadius: Float): Bitmap? {
        // 计算图片缩小后的长宽
        val width = srcBitmap.width
        val height = srcBitmap.height
        // 将缩小后的图片做为预渲染的图片
        val inputBitmap = Bitmap.createScaledBitmap(srcBitmap, width, height, false)
        // 创建一张渲染后的输出图片
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        // 创建RenderScript内核对象
        val rs = RenderScript.create(context)
        // 创建一个模糊效果的RenderScript的工具对象
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius)
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn)
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut)
        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap)
        rs.destroy()
        return outputBitmap
    }
}