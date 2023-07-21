package com.wiser.animationlistdemo.audio

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.wiser.animationlistdemo.R
import java.util.Collections.max
import kotlin.math.roundToInt

class StarrySkyView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    // 画笔
    private val paint = Paint()

    // 星星的数量
    private var starCount = 18

    // 星星的最小和最大尺寸
    private var minStarSize = spToPx(2)
    private var maxStarSize = spToPx(8)

    // 星星图片
    private lateinit var starBitmap: Bitmap

    // 是否开启闪烁动画
    private var enableBlinking = true

    // 闪烁动画的时间周期和一周期内的动画时长占比
    private var blinkDuration = 500L
    private var blinkRatio = 0.3f

    // 当前闪烁动画的时间
    private var currentTime = 0L

    // 星星数组
    private lateinit var stars: Array<Star>

    init {
//        // 从XML属性中获取参数
//        attrs?.let {
//            val typedArray = context.obtainStyledAttributes(it, R.styleable.StarrySkyView)
//            starCount = typedArray.getInteger(R.styleable.StarrySkyView_starCount, starCount)
//            minStarSize = typedArray.getDimensionPixelSize(R.styleable.StarrySkyView_minStarSize, minStarSize)
//            maxStarSize = typedArray.getDimensionPixelSize(R.styleable.StarrySkyView_maxStarSize, maxStarSize)
//            starBitmap = (ContextCompat.getDrawable(context, typedArray.getResourceId(R.styleable.StarrySkyView_starDrawable, 0)) as BitmapDrawable).bitmap
//            enableBlinking = typedArray.getBoolean(R.styleable.StarrySkyView_enableBlinking, enableBlinking)
//            blinkDuration = typedArray.getInteger(R.styleable.StarrySkyView_blinkDuration, blinkDuration.toInt()).toLong()
//            blinkRatio = typedArray.getFloat(R.styleable.StarrySkyView_blinkRatio, blinkRatio)
//            typedArray.recycle()
//        }

        starBitmap = BitmapFactory.decodeResource(resources,R.drawable.star)

        // 初始化星星数组
        stars = Array(starCount) { Star() }

        // 初始化画笔
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制背景
//        canvas.drawColor(Color.BLACK)

        // 绘制星星
        for (star in stars) {
            // 计算星星的透明度和尺寸
            val alpha = (star.opacity * 255).toInt()
            val size = star.size.toFloat()

            // 设置画笔的透明度
            paint.alpha = alpha

            // 计算星星的坐标
            val x = star.x
            val y = star.y

            // 绘制星星
            canvas.drawBitmap(starBitmap, Rect(0, 0, starBitmap.width, starBitmap.height), RectF(x - size / 2, y - size / 2, x + size / 2, y + size / 2), paint)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // 重新生成星星数组
        stars = Array(starCount) { Star() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // 开始动画
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        // 停止动画
        stopAnimation()
    }

    /**
     * 开始动画
     */
    fun startAnimation() {
        if (enableBlinking) {
            postInvalidateOnAnimation()
        }
    }

    /**
     * 停止动画
     */
    fun stopAnimation() {
        removeCallbacks(null)
    }

    /**
     * 触摸屏幕时修改星星数量
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
//        if (event.action == MotionEvent.ACTION_DOWN) {
//            starCount = Math.max(1,Math.min(starCount + 10, 200))
//            stars = Array(starCount) { Star() }
//            postInvalidate()
//        }
        return false
    }

    /**
     * 将sp转为px
     */
    private fun spToPx(sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics).toInt()
    }

    /**
     * 表示一个星星的类
     */
    inner class Star {

        // x坐标和y坐标
        var x: Float = 0f
        var y: Float = 0f

        // 大小和透明度
        var size: Int = 0
        var opacity: Float = 0f

        // 初始化星星
        init {
            // 随机生成坐标
            x = (0..width).random().toFloat()
            y = (0..height).random().toFloat()

            // 随机生成大小和透明度
            size = (minStarSize..maxStarSize).random()
            opacity = (50..100).random() / 100f
        }

        /**
         * 模拟闪烁动画
         */
        fun blink() {
            val blinkTime = blinkDuration * blinkRatio
            val cycleTime = blinkDuration - blinkTime

            if (currentTime % blinkDuration < blinkTime) {
                opacity = 1f
            } else {
                opacity = 0f
            }

            if (currentTime % blinkDuration == 0L) {
                opacity = 1f
            }
        }

        /**
         * 重新生成星星的属性
         */
        fun recreate() {
            x = (0..width).random().toFloat()
            y = (0..height).random().toFloat()
            size = (minStarSize..maxStarSize).random()
            opacity = (50..100).random() / 100f
        }

        /**
         * 更新星星的状态
         */
        fun update() {
            blink()

            if (currentTime % blinkDuration == 0L) {
                recreate()
            }
        }

    }

    /**
     * 随机生成指定范围内的整数
     */
    private fun IntRange.random() = (Math.random() * (endInclusive - start) + start).roundToInt()

    /**
     * 在下一帧绘制
     */
    override fun postInvalidateOnAnimation() {
        postInvalidate()

        currentTime += 16

        if (enableBlinking) {
            postOnAnimationDelayed({ postInvalidateOnAnimation() }, 16)
        }
    }

}