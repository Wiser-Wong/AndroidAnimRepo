package com.wiser.animationlistdemo.cardrotate.view

import android.animation.TypeEvaluator
import android.graphics.Point

/**
 * @author Wiser
 */
class BesselTypeEvaluator : TypeEvaluator<Point> {

    override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point {
        val x = (startValue.x + (endValue.x - startValue.x) * fraction).toInt()
        val y = (startValue.y + (endValue.y - startValue.y) * fraction).toInt()
        return Point(x, y)
    }
}