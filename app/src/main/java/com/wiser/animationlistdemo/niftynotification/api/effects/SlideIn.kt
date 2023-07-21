package com.wiser.animationlistdemo.niftynotification.api.effects

import android.view.View
import com.nineoldandroids.animation.Animator
import com.nineoldandroids.animation.ObjectAnimator

class SlideIn : BaseEffect() {
    override fun setInAnimation(view: View?) {
        animatorSet.playTogether(
            *arrayOf<Animator>(
                ObjectAnimator.ofFloat(
                    view,
                    "translationX",
                    *floatArrayOf((-view!!.width).toFloat(), -10.0f, -20.0f, -5.0f, -10.0f, 0.0f)
                ).setDuration(
                    mDuration
                )
            )
        )
    }

    override fun setOutAnimation(view: View?) {
        animatorSet.playTogether(
            *arrayOf<Animator>(
                ObjectAnimator.ofFloat(
                    view,
                    "translationX",
                    *floatArrayOf(0.0f, -10.0f, view!!.width.toFloat())
                ).setDuration(
                    mDuration
                )
            )
        )
    }

    override fun getAnimDuration(duration: Long): Long {
        return duration
    }
}