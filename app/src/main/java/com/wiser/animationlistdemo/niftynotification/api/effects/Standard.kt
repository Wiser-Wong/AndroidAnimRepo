package com.wiser.animationlistdemo.niftynotification.api.effects

import android.view.View
import com.nineoldandroids.animation.Animator
import com.nineoldandroids.animation.ObjectAnimator

class Standard : BaseEffect() {
    override fun setInAnimation(view: View?) {
        animatorSet.playTogether(
            *arrayOf<Animator>(
                ObjectAnimator.ofFloat(
                    view,
                    "translationY",
                    *floatArrayOf((-view!!.height).toFloat(), 0.0f)
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
                    "translationY",
                    *floatArrayOf(0.0f, (-view!!.height).toFloat())
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