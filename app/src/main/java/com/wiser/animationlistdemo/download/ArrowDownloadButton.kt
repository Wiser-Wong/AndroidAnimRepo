package com.wiser.animationlistdemo.download

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View

open class ArrowDownloadButton @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var onDownloadCompletedListener: OnDownloadCompletedListener? = null
    private var xx = 550f
    private var yy = 550f
    private var radius = RADIUS
    private var maxWaveHeight = MAX_WAVE_HEIGHT
    private var minWaveHeight = MIN_WAVE_HEIGHT
    private var textY = TEXT_Y
    private var step = STEP
    private var elasticityStep = ELASTICITY_STEP
    private var ropeStepX = ROPE_STEP_X
    private var ropeStepY = ROPE_STEP_Y
    private var ropeHeadStepY = ROPE_HEAD_STEP_Y
    private var jumpStep = JUMP_STEP
    private var downStep = DOWN_STEP
    private var triStep = TRI_STEP
    private var hookStepY = HOOK_STEP_Y
    private var littleStep = LITTLE_STEP
    private var smallRadius = SMALL_RADIUS
    private var textSize = TEXT_SIZE
    private var arcWidth = ARC_WIDTH
    private var arrowWidth = ARROW_WIDTH
    private var triWidth = TRI_WIDTH
    private var loadingWidth = LOADING_WIDTH
    private var arrowPaint: Paint? = null
    private var arcPaint: Paint? = null
    private var smallPaint: Paint? = null
    private var triPaint: Paint? = null
    private var loadingPaint: Paint? = null
    private var textPaint: Paint? = null
    private var arrowPath: Path? = null
    private var triPath: Path? = null
    private var textPath: Path? = null
    private var oval: RectF? = null
    private var a: Point? = null
    private var b: Point? = null
    private var c: Point? = null
    private var d: Point? = null
    private var e: Point? = null
    private var jumpPoint: Point? = null
    private val triPoints: MutableList<Point> = ArrayList()
    private var isFirst = true
    private var isAnimating = false
    private var bezier = false
    private var isLoading = false
    private var isCompleted = false
    private var isEnd = false
    private var count = 0
    private var length = 0f
    private var currentTime = 0
    private var waveHeight = MIN_WAVE_HEIGHT
    private var progress = 0f
    private var hookCount = 0
    var lengthX = 3 * radius / 4
    var lengthY = 3 * radius / 4
    fun getProgress(): Float {
        return progress
    }

    fun setProgress(progress: Float) {
        if (progress > 100) {
            this.progress = 100f
        } else {
            this.progress = progress
        }
        if (progress == 100f) {
            isLoading = false
            isCompleted = true
        }
    }

    init {
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false))
    }

    private fun measure(measureSpec: Int, isWidth: Boolean): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        val padding = if (isWidth) paddingLeft + paddingRight else paddingTop + paddingBottom
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = if (isWidth) suggestedMinimumWidth else suggestedMinimumHeight
            result += padding
            if (mode == MeasureSpec.AT_MOST) {
                result = if (isWidth) {
                    Math.max(result, size)
                } else {
                    Math.min(result, size)
                }
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        if (isFirst) {
            init()
            isFirst = false
        }
        arcPaint?.let { canvas.drawCircle(xx, yy, radius, it) }
        drawArrow(canvas)
        if (isAnimating) {
            animating()
        }
        if (isLoading) {
            loading(canvas)
        }
        if (isCompleted) {
            afterCompleted(canvas)
        }
    }

    fun setOnDownloadCompletedListener(onDownloadCompletedListener: OnDownloadCompletedListener?) {
        this.onDownloadCompletedListener = onDownloadCompletedListener
    }

    private fun init() {
        val temp = if (height > width) (width / 2).toFloat() else height / 2.toFloat()
        radius = temp - temp * OFFSET / RADIUS - temp * ELASTICITY_STEP / RADIUS - 6
        xx = (paddingLeft + width / 2).toFloat()
        yy = (paddingTop + height / 2).toFloat()
        maxWaveHeight = convert(MAX_WAVE_HEIGHT)
        minWaveHeight = convert(MIN_WAVE_HEIGHT)
        textY = convert(TEXT_Y)
        step = convert(STEP)
        elasticityStep = convert(ELASTICITY_STEP)
        ropeStepX = convert(ROPE_STEP_X)
        ropeStepY = convert(ROPE_STEP_Y)
        ropeHeadStepY = convert(ROPE_HEAD_STEP_Y)
        jumpStep = convert(JUMP_STEP)
        downStep = convert(DOWN_STEP)
        triStep = convert(TRI_STEP)
        hookStepY = convert(HOOK_STEP_Y)
        littleStep = convert(LITTLE_STEP)
        smallRadius = convert(SMALL_RADIUS)
        textSize = convert(TEXT_SIZE)
        arcWidth = convert(ARC_WIDTH)
        arrowWidth = convert(ARROW_WIDTH)
        triWidth = convert(TRI_WIDTH)
        loadingWidth = convert(LOADING_WIDTH)
        lengthX = 3 * radius / 4
        lengthY = 3 * radius / 4
        arrowPath = Path()
        triPath = Path()
        textPath = Path()
        oval = RectF()
        oval?.left = xx - radius
        oval?.top = yy - radius
        oval?.right = xx + radius
        oval?.bottom = yy + radius
        length = radius / 2
        initializePaints()
        initializePoints()
    }

    /**
     * 开始动画在loading之前
     */
    fun startAnimating() {
        isAnimating = true
        invalidate()
    }

    /**
     * 重置
     */
    fun reset() {
        isAnimating = false
        isLoading = false
        bezier = false
        isCompleted = false
        isEnd = false
        length = radius / 2
        count = 0
        hookCount = 0
        jumpPoint?.x = -1f
        progress = 0f
        lengthX = 3 * radius / 4
        lengthY = 3 * radius / 4
        a?.y = yy + length
        b?.y = yy - length
        e?.y = yy + length
        c?.x = xx - length / 2
        c?.y = yy + length / 2
        d?.x = xx + length / 2
        d?.y = yy + length / 2
        invalidate()
    }

    /**
     * 动画
     */
    private fun animating() {
        if (count < 19) {
            length = length * 3 / 4
            a?.y = yy + length
            b?.y = yy - length
            if ((count + 1) % 3 == 0 && count < 9) {
                e?.y = (e?.y ?: 0f) + step
                c?.y = (c?.y ?: 0f) + step
                d?.y = (d?.y ?: 0f) + step
            }
            if (count in 9..11) {
                jumpPoint?.x = xx
                jumpPoint?.y = yy - jumpStep * (count - 8)
                c?.x = (c?.x ?: 0f) - ropeStepX
                c?.y = (c?.y ?: 0f) - ropeHeadStepY
                d?.x = (d?.x ?: 0f) + ropeStepX
                d?.y = (d?.y ?: 0f) - ropeHeadStepY
                e?.y = (e?.y ?: 0f) - ropeStepY
            }
            if (count > 11) {
                bezier = true
                if (count == 12) {
                    jumpPoint?.y = (jumpPoint?.y ?: 0f) - jumpStep * 2
                } else {
                    jumpPoint?.y = (jumpPoint?.y ?: 0f) + downStep
                    if (count < 16) {
                        val time1 = 15 - count
                        e?.y = yy + time1 * elasticityStep
                    }
                }
            }
            count++
            postInvalidateDelayed(DURATION.toLong())
        } else {
            isAnimating = false
            bezier = false
            if (progress != 100f) {
                isLoading = true
            } else {
                isLoading = false
                isCompleted = true
            }
        }
    }

    /**
     * under loading
     *
     * @param canvas
     */
    private fun loading(canvas: Canvas) {
        var currentPoint = triPoints[0]
        var nextPoint: Point
        for (i in 0 until TRI_POINT_NUMBER) {
            val p = triPoints[i]
            p.x = xx - 3 * radius / 4 + triStep * i
            p.y = yy + calculateTri(TIME_STEP * i, currentTime.toFloat())
        }
        for (i in 1 until TRI_POINT_NUMBER) {
            nextPoint = triPoints[i]
            triPath?.reset()
            triPath?.moveTo(currentPoint.x, currentPoint.y)
            triPath?.lineTo(nextPoint.x, nextPoint.y)
            canvas.drawCircle(nextPoint.x, nextPoint.y, smallRadius, smallPaint!!)
            canvas.drawPath(triPath!!, triPaint!!)
            currentPoint = nextPoint
        }
        textPath?.moveTo(xx - textSize, yy + textY)
        textPath?.lineTo(xx + textSize, yy + textY)
        canvas.drawTextOnPath(progress.toInt().toString() + "%", textPath!!, 0f, 0f, textPaint!!)
        currentTime = (currentTime + TIME_STEP).toInt()
        val sweepAngle = progress / PROGRESS * ANGLE
        canvas.drawArc(oval!!, 270f, 0 - sweepAngle, false, loadingPaint!!)
        postInvalidateDelayed(DURATION.toLong())
    }

    /**
     * 1. 画箭头
     * 2. 动画完成小球跳动
     *
     * @param canvas
     */
    private fun drawArrow(canvas: Canvas) {
        if (jumpPoint?.x != -1f) {
            canvas.drawCircle(jumpPoint?.x ?: 0f, jumpPoint?.y ?: 0f, smallRadius, smallPaint!!)
        }
        if (bezier) {
            arrowPath?.reset()
            arrowPath?.moveTo(c?.x ?: 0f, c?.y ?: 0f)
            arrowPath?.quadTo(e?.x ?: 0f, e?.y ?: 0f, d?.x ?: 0f, d?.y ?: 0f)
            canvas.drawPath(arrowPath!!, arrowPaint!!)
        } else if (isLoading) {
        } else if (isCompleted) {
        } else if (isEnd) {
            canvas.drawCircle(xx, yy, radius, loadingPaint!!)
            drawArrowOrHook(canvas)
        } else {
            arrowPath?.reset()
            arrowPath?.moveTo(a?.x ?: 0f, a?.y ?: 0f)
            arrowPath?.lineTo(b?.x ?: 0f, b?.y ?: 0f)
            canvas.drawPath(arrowPath!!, arrowPaint!!)
            canvas.drawCircle(a?.x ?: 0f, a?.y ?: 0f, smallRadius, smallPaint!!)
            canvas.drawCircle(b?.x ?: 0f, b?.y ?: 0f, smallRadius, smallPaint!!)
            drawArrowOrHook(canvas)
        }
    }

    /**
     * 画箭头或者对钩
     */
    private fun drawArrowOrHook(canvas: Canvas) {
        arrowPath?.reset()
        arrowPath?.moveTo(e?.x ?: 0f, e?.y ?: 0f)
        arrowPath?.lineTo(c?.x ?: 0f, c?.y ?: 0f)
        canvas.drawPath(arrowPath!!, arrowPaint!!)
        arrowPath?.reset()
        arrowPath?.moveTo(e?.x ?: 0f, e?.y ?: 0f)
        arrowPath?.lineTo(d?.x ?: 0f, d?.y ?: 0f)
        canvas.drawPath(arrowPath!!, arrowPaint!!)
        canvas.drawCircle(c?.x ?: 0f, c?.y ?: 0f, smallRadius, smallPaint!!)
        canvas.drawCircle(d?.x ?: 0f, d?.y ?: 0f, smallRadius, smallPaint!!)
        canvas.drawCircle(e?.x ?: 0f, e?.y ?: 0f, smallRadius, smallPaint!!)
    }

    /**
     * 动画之后执行loading
     */
    private fun afterCompleted(canvas: Canvas) {
        canvas.drawCircle(xx, yy, radius, loadingPaint!!)
        if (hookCount.toFloat() == HOOK_COUNT - 1) {
            e?.y = (e?.y ?: 0f) + littleStep
            c?.x = (c?.x ?: 0f) - littleStep
            d?.x = (d?.x ?: 0f) + littleStep
            d?.y = (d?.y ?: 0f) - littleStep
            isCompleted = false
            isEnd = true
        } else {
            e?.x = xx
            e?.y = yy + hookStepY * (hookCount + 1)
            lengthX = lengthX * 3 / 4
            c?.x = xx - lengthX * 3 / 4
            c?.y = yy
            d?.x = xx + lengthY - radius / 8f * (hookCount + 1)
            d?.y = yy - hookStepY * (hookCount + 1)
            hookCount++
        }
        drawArrowOrHook(canvas)
        postInvalidateDelayed(COMPLETE_DURATION.toLong())
        onDownloadCompletedListener?.onCompleted()
    }

    private fun initializePaints() {
        arcPaint = Paint()
        arcPaint?.isAntiAlias = true
        arcPaint?.style = Paint.Style.STROKE
        arcPaint?.strokeWidth = arcWidth
        arcPaint?.color = BLUE_ONE
        arrowPaint = Paint()
        arrowPaint?.isAntiAlias = true
        arrowPaint?.style = Paint.Style.STROKE
        arrowPaint?.strokeWidth = arrowWidth
        arrowPaint?.color = WHILE
        smallPaint = Paint()
        smallPaint?.isAntiAlias = true
        smallPaint?.style = Paint.Style.FILL
        smallPaint?.color = WHILE
        triPaint = Paint()
        triPaint?.isAntiAlias = true
        triPaint?.style = Paint.Style.STROKE
        triPaint?.strokeWidth = triWidth
        triPaint?.color = WHILE
        loadingPaint = Paint()
        loadingPaint?.isAntiAlias = true
        loadingPaint?.style = Paint.Style.STROKE
        loadingPaint?.strokeWidth = loadingWidth
        loadingPaint?.color = WHILE
        textPaint = Paint()
        textPaint?.isAntiAlias = true
        textPaint?.style = Paint.Style.FILL
        textPaint?.strokeWidth = 1f
        textPaint?.color = WHILE
        textPaint?.textSize = textSize
    }

    private fun initializePoints() {
        a = Point(xx, yy + radius / 2)
        b = Point(xx, yy - radius / 2)
        c = Point(xx - radius / 4, yy + radius / 4)
        d = Point(xx + radius / 4, yy + radius / 4)
        e = Point(xx, yy + radius / 2)
        jumpPoint = Point()
        for (i in 0 until TRI_POINT_NUMBER) {
            val point = Point()
            point.x = xx - 3 * radius / 4 + triStep * i
            point.y = yy + calculateTri(TIME_STEP * i, 0f)
            triPoints.add(point)
        }
    }

    /**
     * 画波浪
     *
     * @param originalTime original time
     * @param currentTime  current time
     */
    private fun calculateTri(originalTime: Float, currentTime: Float): Float {
        waveHeight = if (progress < PROGRESS / 3) {
            MIN_WAVE_HEIGHT
        } else if (progress < PROGRESS * 2 / 3) {
            maxWaveHeight
        } else {
            minWaveHeight
        }
        return (waveHeight * Math.sin(Math.PI / 80 * (originalTime + currentTime))).toFloat()
    }

    private fun convert(original: Float): Float {
        return radius * original / RADIUS
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putFloat(X_I, xx)
        bundle.putFloat(Y_I, yy)
        bundle.putFloat(RADIUS_I, radius)
        bundle.putFloat(MAX_WAVE_HEIGHT_I, maxWaveHeight)
        bundle.putFloat(MIN_WAVE_HEIGHT_I, minWaveHeight)
        bundle.putFloat(TEXT_Y_I, textY)
        bundle.putFloat(STEP_I, step)
        bundle.putFloat(ELASTICITY_STEP_I, elasticityStep)
        bundle.putFloat(ROPE_STEP_X_I, ropeStepX)
        bundle.putFloat(ROPE_STEP_Y_I, ropeStepY)
        bundle.putFloat(ROPE_HEAD_STEP_Y_I, ropeHeadStepY)
        bundle.putFloat(JUMP_STEP_I, jumpStep)
        bundle.putFloat(DOWN_STEP_I, downStep)
        bundle.putFloat(TRI_STEP_I, triStep)
        bundle.putFloat(HOOK_STEP_Y_I, hookStepY)
        bundle.putFloat(LITTLE_STEP_I, littleStep)
        bundle.putFloat(SMALL_RADIUS_I, smallRadius)
        bundle.putFloat(TEXT_SIZE_I, textSize)
        bundle.putFloat(ARC_WIDTH_I, arcWidth)
        bundle.putFloat(ARROW_WIDTH_I, arrowWidth)
        bundle.putFloat(TRI_WIDTH_I, triWidth)
        bundle.putFloat(LOADING_WIDTH_I, loadingWidth)
        bundle.putBoolean(IS_FIRST_I, isFirst)
        bundle.putBoolean(IS_ANIMATING_I, isAnimating)
        bundle.putBoolean(BEZIER_I, bezier)
        bundle.putBoolean(IS_LOADING_I, isLoading)
        bundle.putBoolean(IS_COMPLETED_I, isCompleted)
        bundle.putBoolean(IS_END_I, isEnd)
        bundle.putInt(COUNT_I, count)
        bundle.putFloat(LENGTH_I, length)
        bundle.putInt(CURRENT_TIME_I, currentTime)
        bundle.putFloat(WAVE_HEIGHT_I, waveHeight)
        bundle.putFloat(PROGRESS_I, progress)
        bundle.putInt(HOOK_COUNT_I, hookCount)
        bundle.putFloat(LENGTH_X_I, lengthX)
        bundle.putFloat(LENGTH_Y_I, lengthY)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val bundle = state
            xx = bundle.getFloat(X_I)
            yy = bundle.getFloat(Y_I)
            radius = bundle.getFloat(RADIUS_I)
            maxWaveHeight = bundle.getFloat(MAX_WAVE_HEIGHT_I)
            minWaveHeight = bundle.getFloat(MIN_WAVE_HEIGHT_I)
            textY = bundle.getFloat(TEXT_Y_I)
            step = bundle.getFloat(STEP_I)
            elasticityStep = bundle.getFloat(ELASTICITY_STEP_I)
            ropeStepX = bundle.getFloat(ROPE_STEP_X_I)
            ropeStepY = bundle.getFloat(ROPE_STEP_Y_I)
            ropeHeadStepY = bundle.getFloat(ROPE_HEAD_STEP_Y_I)
            jumpStep = bundle.getFloat(JUMP_STEP_I)
            downStep = bundle.getFloat(DOWN_STEP_I)
            triStep = bundle.getFloat(TRI_STEP_I)
            hookStepY = bundle.getFloat(HOOK_STEP_Y_I)
            littleStep = bundle.getFloat(LITTLE_STEP_I)
            smallRadius = bundle.getFloat(SMALL_RADIUS_I)
            textSize = bundle.getFloat(TEXT_SIZE_I)
            arcWidth = bundle.getFloat(ARC_WIDTH_I)
            arrowWidth = bundle.getFloat(ARROW_WIDTH_I)
            triWidth = bundle.getFloat(TRI_WIDTH_I)
            loadingWidth = bundle.getFloat(LOADING_WIDTH_I)
            isFirst = bundle.getBoolean(IS_FIRST_I)
            isAnimating = bundle.getBoolean(IS_ANIMATING_I)
            bezier = bundle.getBoolean(BEZIER_I)
            isLoading = bundle.getBoolean(IS_LOADING_I)
            isCompleted = bundle.getBoolean(IS_COMPLETED_I)
            isEnd = bundle.getBoolean(IS_END_I)
            count = bundle.getInt(COUNT_I)
            length = bundle.getFloat(LENGTH_I)
            currentTime = bundle.getInt(CURRENT_TIME_I)
            waveHeight = bundle.getFloat(WAVE_HEIGHT_I)
            progress = bundle.getFloat(PROGRESS_I)
            hookCount = bundle.getInt(HOOK_COUNT_I)
            lengthX = bundle.getFloat(LENGTH_X_I)
            lengthY = bundle.getFloat(LENGTH_Y_I)
        }
        super.onRestoreInstanceState(state)
    }

    internal class Point {
        var x: Float
        var y: Float

        constructor(x: Float, y: Float) {
            this.x = x
            this.y = y
        }

        constructor() {
            x = -1f
            y = -1f
        }
    }

    companion object {
        private val BLUE_ONE = Color.rgb(46, 164, 242)
        private val WHILE = Color.rgb(255, 255, 255)
        private const val RADIUS = 180f
        private const val TRI_POINT_NUMBER = 17
        private const val MAX_WAVE_HEIGHT = 10f
        private const val MIN_WAVE_HEIGHT = 5f
        private const val PROGRESS = 100
        private const val ANGLE = 360
        private const val TEXT_Y = 67.5f
        private const val OFFSET = 10f
        private const val SMALL_RADIUS = 5f
        private const val TEXT_SIZE = 40f
        private const val ARC_WIDTH = 20f
        private const val ARROW_WIDTH = 10f
        private const val TRI_WIDTH = 10f
        private const val LOADING_WIDTH = 10f
        private const val STEP = 2f
        private const val ELASTICITY_STEP = 10f
        private const val ROPE_STEP_X = 30f
        private const val ROPE_STEP_Y = 32f
        private const val ROPE_HEAD_STEP_Y = 17f
        private const val JUMP_STEP = 45f
        private const val DOWN_STEP = 7.5f
        private const val TRI_STEP = 16.875f
        private const val TIME_STEP = 20f
        private const val HOOK_STEP_Y = 15f
        private const val HOOK_COUNT = 4f
        private const val LITTLE_STEP = 8f
        private const val DURATION = 20
        private const val COMPLETE_DURATION = 20

        /**
         * start instance
         */
        private const val INSTANCE_STATE = "instance_state"

        /**
         *
         */
        private const val X_I = "x"
        private const val Y_I = "y"
        private const val RADIUS_I = "radius"
        private const val MAX_WAVE_HEIGHT_I = "max_wave_height"
        private const val MIN_WAVE_HEIGHT_I = "min_wave_height"
        private const val TEXT_Y_I = "text_y"
        private const val STEP_I = "step"
        private const val ELASTICITY_STEP_I = "elasticity_step"
        private const val ROPE_STEP_X_I = "rope_step_x"
        private const val ROPE_STEP_Y_I = "rope_step_y"
        private const val ROPE_HEAD_STEP_Y_I = "rope_head_step_y"
        private const val JUMP_STEP_I = "jump_step"
        private const val DOWN_STEP_I = "down_step"
        private const val TRI_STEP_I = "tri_step"
        private const val HOOK_STEP_Y_I = "hook_step"
        private const val LITTLE_STEP_I = "little_step"
        private const val SMALL_RADIUS_I = "small_radius"
        private const val TEXT_SIZE_I = "text_size"
        private const val ARC_WIDTH_I = "arc_width"
        private const val ARROW_WIDTH_I = "arrow_width"
        private const val TRI_WIDTH_I = "tri_width"
        private const val LOADING_WIDTH_I = "loading_width"
        private const val IS_FIRST_I = "is_first"
        private const val IS_ANIMATING_I = "is_animating"
        private const val BEZIER_I = "bezier"
        private const val IS_LOADING_I = "is_loading"
        private const val IS_COMPLETED_I = "is_completed"
        private const val IS_END_I = "is_end"
        private const val COUNT_I = "count"
        private const val LENGTH_I = "length"
        private const val CURRENT_TIME_I = "current_time"
        private const val WAVE_HEIGHT_I = "wave_height"
        private const val PROGRESS_I = "progress"
        private const val HOOK_COUNT_I = "hook_count"
        private const val LENGTH_X_I = "length_x"
        private const val LENGTH_Y_I = "length_y"
    }
}

interface OnDownloadCompletedListener {
    fun onCompleted()
}