package com.wiser.animationlistdemo.audio

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.transition.*
import android.view.View
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.transition.addListener
import androidx.core.transition.doOnStart
import androidx.core.view.isVisible
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_HIGH_BOUNCY
import androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/4/3 18:37
 * @author: jiaruihua
 * @desc :
 *
 */


fun applyGaussianBlur(context: Context, inputBitmap: Bitmap, radius: Float): Bitmap {
    val rs = RenderScript.create(context)
    val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

    val inputAllocation = Allocation.createFromBitmap(rs, inputBitmap)
    val outputAllocation = Allocation.createTyped(rs, inputAllocation.type)

    blurScript.setRadius(radius)
    blurScript.setInput(inputAllocation)
    blurScript.forEach(outputAllocation)

    val outputBitmap =
        Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
    outputAllocation.copyTo(outputBitmap)

    rs.destroy()
    return outputBitmap
}


fun hideMiniPlayer(resources: Resources, view: View) {
    view.let { miniplayer ->


        val bottom = resources.displayMetrics.heightPixels.toFloat()
        val miniHeight = resources.getDimension(R.dimen.ksl_dp_170)
        val dy = bottom - miniHeight
        val obj = ObjectAnimator.ofFloat(miniplayer, "translationY", dy, bottom + 100).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addListener(
                onStart = {
                    miniplayer.visibility = View.VISIBLE
                }
            )
        }

        val objalpha = ObjectAnimator.ofFloat(miniplayer, "alpha", 1f, 0.3f)
        val objv = AnimatorSet()
        objv.playTogether(obj)
        objv.start()

    }
}

fun showMiniPlayer(resources: Resources, view: View) {
    view.let { miniplayer ->
        val bottom = resources.displayMetrics.heightPixels.toFloat()
        val miniHeight = resources.getDimension(R.dimen.ksl_dp_170)
        val dy = bottom - miniHeight

//        val objalpha = ObjectAnimator.ofFloat(miniplayer, "alpha", 0.3f, 1f).apply {
//            addListener(
//                onStart = {
//                    miniplayer.visibility = View.VISIBLE
//                }
//            , onEnd = {
//                    miniplayer.alpha = 1f
//                }
//            )
//        }
//        val objv = AnimatorSet()
//        objv.playTogether(objalpha)
//        objv.start()
        miniplayer.alpha = 1f
        miniplayer.visibility = View.VISIBLE
        val anim = SpringAnimation(miniplayer, DynamicAnimation.TRANSLATION_Y)
        anim.setStartValue(bottom)
        anim.animateToFinalPosition(dy)
        anim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        anim.spring.stiffness = 100f
        anim.start()

        anim.addEndListener { _, _, _, _ ->
        }

    }
}


fun transX(
    view: View,
    startValue: Float,
    finalPosition: Float,
    stiffness: Float = 200f,
    delay: Long = 0,
    duration: Long = 500

) {


    view.postDelayed({
        val anim = SpringAnimation(view, DynamicAnimation.TRANSLATION_X)
        anim.setStartValue(startValue)
        anim.animateToFinalPosition(finalPosition)
        anim.spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        anim.spring.stiffness = stiffness
        anim.start()
        anim.addEndListener { _, _, _, _ ->
        }

    }, delay)


}


fun changeColor(statColor:Int,endColor:Int,dtime:Long = 1000,end:(color:Int)->Unit
){
    ValueAnimator.ofArgb(statColor, endColor)
        .apply {
            duration = dtime
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                end.invoke(it.animatedValue as Int)
            }
        }.start()
}

fun scale(view: View, finalPosition: Float, stiffness: Float = 130f, onEnd: (() -> Unit)? = null) {
    SpringAnimation(view, DynamicAnimation.SCALE_X).apply {
        animateToFinalPosition(finalPosition)
        spring.stiffness = stiffness
        addEndListener { _, _, _, _ ->
            onEnd?.invoke()
        }
        start()
    }
    SpringAnimation(view, DynamicAnimation.SCALE_Y).apply {
        animateToFinalPosition(finalPosition)
        spring.stiffness = stiffness
        addEndListener { _, _, _, _ ->
        }
        start()
    }

}

fun scale2(view: View, finalPosition: Float, stiffness: Float = 130f, onEnd: (() -> Unit)? = null) {
    SpringAnimation(view, DynamicAnimation.SCALE_X).apply {
        animateToFinalPosition(finalPosition)
        spring.stiffness = stiffness
        spring.dampingRatio = DAMPING_RATIO_MEDIUM_BOUNCY
        addEndListener { _, _, _, _ ->
            onEnd?.invoke()
        }
        start()
    }
    SpringAnimation(view, DynamicAnimation.SCALE_Y).apply {
        animateToFinalPosition(finalPosition)
        spring.stiffness = stiffness
        spring.dampingRatio = DAMPING_RATIO_HIGH_BOUNCY
        addEndListener { _, _, _, _ ->
        }
        start()
    }

}


fun rotationY(view: View, finalPosition: Float, onEnd: (() -> Unit)? = null) {
    SpringAnimation(view, DynamicAnimation.ROTATION_Y).apply {
        animateToFinalPosition(finalPosition)
        spring.stiffness = 130f
        addEndListener { _, _, _, _ ->
            onEnd?.invoke()
        }
        start()
    }

}

fun rotationCenter(
    view: View,
    startValue: Float,
    finalPosition: Float,
    stiffness: Float = 100f,
    onEnd: (() -> Unit)? = null
) {
    SpringAnimation(view, DynamicAnimation.ROTATION).apply {
        setStartValue(startValue)
        animateToFinalPosition(finalPosition)
        spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        spring.stiffness = stiffness
        addEndListener { _, _, _, _ ->
            onEnd?.invoke()
        }
        start()
    }
}

fun trans(
    view: View,
    startValue: Float,
    finalPosition: Float,
    stiffness: Float = 200f,
    delay: Long = 0,
    duration: Long = 500,
    onEnd: (() -> Unit)? = null
) {


    view.postDelayed({
        val anim = SpringAnimation(view, DynamicAnimation.TRANSLATION_Y)
        anim.setStartValue(startValue)
        anim.animateToFinalPosition(finalPosition)
        anim.spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        anim.spring.stiffness = stiffness

        anim.addEndListener { _, _, _, _ ->
            onEnd?.invoke()
        }
        anim.start()
    }, delay)


}



fun transANI(window: Window, isOpenIn: Boolean = true, isOpenExit: Boolean = true) {
    val transSet = TransitionSet().apply {
        addTransition(ChangeBounds())
        addTransition(ChangeClipBounds())
        addTransition(ChangeTransform().apply {
            duration = 300
            addListener {
                doOnStart {
                    it.targets.forEach {
                        println("it---------------onstart")
                        rotationCenter(it, it.rotation, it.rotation + 30)

                    }
                }
            }

        })
        addTransition(ChangeImageTransform())

        addListener {
            doOnStart {
                println("it---------------2222222onstart")
            }
        }
    }


    if (isOpenIn) {
        window.sharedElementEnterTransition = transSet

    }

    if (isOpenExit) {
        window.sharedElementExitTransition = transSet

    }
}

fun transY(
    view: View,
    startValue: Float,
    finalPosition: Float,
    stiffness: Float = 200f,
    delay: Long = 0,
    onStart: (() -> Unit)? = null,
    onEnd: (() -> Unit)? = null
) {


    view.postDelayed({

        view.visibility = View.VISIBLE
        val anim = SpringAnimation(view, DynamicAnimation.TRANSLATION_Y)
        anim.setStartValue(startValue)
        anim.animateToFinalPosition(finalPosition)
        anim.spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        anim.spring.stiffness = stiffness

        anim.addEndListener { _, _, _, _ ->
            onEnd?.invoke()
        }
        onStart?.invoke()
        anim.start()
    }, delay)


}

fun hideView(view: View, duration: Long = 500, onEnd: (() -> Unit)? = null) {
    view.run {
        val obj = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        obj.duration = duration
        obj.doOnEnd { onEnd?.invoke() }
        obj.start()
    }
}

fun showView(view: View, duration: Long = 500) {
    view.run {
        val obj = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        obj.duration = duration
        obj.start()
    }
}

