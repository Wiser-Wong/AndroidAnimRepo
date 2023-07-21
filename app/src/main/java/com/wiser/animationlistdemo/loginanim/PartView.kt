package com.wiser.animationlistdemo.loginanim

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.wiser.animationlistdemo.loginanim.NodAnim

/**
 ***************************************
 * 项目名称:LoginPageAnimator
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/3/7     11:00
 * 用途: 更新说明
 ***************************************
 */
class PartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var gravity = 0

    private var bitmap: Bitmap? = null
    private val bitmapBounds = Rect()

    private var clippingFactor = 0.5f

    private var bitmapPaint: Paint? = null

    private var visibleBounds: Rect? = null

    private var intVisibility = 0
    private var extVisibility = 0

    private var localFoldRotation = 0f

    constructor(context: Context, parent: NodAnim, gravity: Int): this(context, null, 0) {
        this.gravity = gravity
        val matchParent = FrameLayout.LayoutParams.MATCH_PARENT
        parent.addView(this, FrameLayout.LayoutParams(matchParent, matchParent))
        cameraDistance =
            (48 * resources.displayMetrics.densityDpi).toFloat()
        bitmapPaint = Paint()
        bitmapPaint?.isDither = true
        bitmapPaint?.isFilterBitmap = true
        setWillNotDraw(false)
    }

    init {

    }

    fun setCacheBitmap(bitmap: Bitmap?) {
        this.bitmap = bitmap
        calculateBitmapBounds()
    }

    fun setVisibleBounds(visibleBounds: Rect?) {
        this.visibleBounds = visibleBounds
        calculateBitmapBounds()
    }

    private fun calculateBitmapBounds() {
        if (bitmap == null) {
            bitmapBounds[0, 0, 0] = 0
        } else {
            val bh = bitmap?.height
            val bw = bitmap?.width
            val top = if (gravity == Gravity.TOP) 0 else ((bh?.times((1f - clippingFactor)) ?: 0f) - 0.5f).toInt()
            val bottom = if (gravity == Gravity.TOP) ((bh?.times(clippingFactor) ?: 0f) + 0.5f).toInt() else bh
            if (bottom != null) {
                bitmapBounds[0, top, bw!!] = bottom
            }
            if (visibleBounds != null) {
                if (!bitmapBounds.intersect(visibleBounds!!)) {
                    bitmapBounds[0, 0, 0] = 0 // No intersection
                }
            }
        }
        invalidate()
    }

    fun applyFoldRotation(rotation: Float) {
        var position = rotation
        while (position < 0f) {
            position += 360f
        }
        position %= 360f
        if (position > 180f) {
            position -= 360f // Now position is within (-180; 180]
        }
        var rotationX = 0f
        var isVisible = true
        if (gravity == Gravity.TOP) {
            if (position <= -90f || position == 180f) { // (-180; -90] || {180} - Will not show
                isVisible = false
            } else if (position < 0f) { // (-90; 0) - Applying rotation
                rotationX = position
            }
            // [0; 180) - Holding still
        } else {
            if (position >= 90f) { // [90; 180] - Will not show
                isVisible = false
            } else if (position > 0f) { // (0; 90) - Applying rotation
                rotationX = position
            }
            // (-180; 0] - Holding still
        }
        setRotationX(rotationX)
        intVisibility = if (isVisible) VISIBLE else INVISIBLE
        applyVisibility()
        localFoldRotation = position
        invalidate() // Needed to draw shadow overlay
    }

    fun applyRollingDistance(distance: Float, scaleY: Float) {
        // Applying translation
        translationY = (distance * scaleY + 0.5f).toInt().toFloat()

        // Computing clipping for top view (bottom clipping will be 1 - topClipping)
        val h = height / 2
        val topClipping = if (h == 0) 0.5f else 0.5f * (h - distance) / h
        clippingFactor = if (gravity == Gravity.TOP) topClipping else 1f - topClipping
        calculateBitmapBounds()
    }

    override fun setVisibility(visibility: Int) {
        extVisibility = visibility
        applyVisibility()
    }

    @SuppressLint("WrongConstant")
    private fun applyVisibility() {
        super.setVisibility(if (extVisibility == VISIBLE) intVisibility else extVisibility)
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap!!, bitmapBounds, bitmapBounds, bitmapPaint)
        }
    }
}