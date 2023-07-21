package com.wiser.animationlistdemo.niftynotification.api.effects

import android.view.View
import com.nineoldandroids.animation.Animator
import com.nineoldandroids.animation.ObjectAnimator
import com.nineoldandroids.view.ViewHelper

class Flip : BaseEffect() {
    var e = mDuration
    var s = mDuration
    override fun setInAnimation(view: View?) {
        ViewHelper.setPivotX(view, (view!!.width / 2).toFloat())
        ViewHelper.setPivotY(view, 0.0f)
        animatorSet.playTogether(
            *arrayOf<Animator>(
                ObjectAnimator.ofFloat(view, "rotationX", *floatArrayOf(-90.0f, 0.0f)).setDuration(
                    s
                ), ObjectAnimator.ofFloat(view, "alpha", *floatArrayOf(0.0f, 1.0f)).setDuration(
                    s * 3 / 2
                )
            )
        )
    }

    override fun setOutAnimation(view: View?) {
        ViewHelper.setPivotX(view, (view!!.width / 2).toFloat())
        ViewHelper.setPivotY(view, 0.0f)
        animatorSet.playTogether(
            *arrayOf<Animator>(
                ObjectAnimator.ofFloat(view, "rotationX", *floatArrayOf(0.0f, -90.0f)).setDuration(
                    e
                ), ObjectAnimator.ofFloat(view, "alpha", *floatArrayOf(1.0f, 0.0f)).setDuration(
                    e * 3 / 2
                )
            )
        )
    }

    override fun getAnimDuration(duration: Long): Long {
        return duration
    }
}