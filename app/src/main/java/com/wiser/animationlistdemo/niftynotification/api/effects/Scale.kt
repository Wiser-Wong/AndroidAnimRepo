package com.wiser.animationlistdemo.niftynotification.api.effects

import android.view.View
import com.nineoldandroids.animation.Animator
import com.nineoldandroids.animation.ObjectAnimator

class Scale : BaseEffect() {
    override fun setInAnimation(view: View?) {
        animatorSet.playTogether(
            *arrayOf<Animator>(
                ObjectAnimator.ofFloat(
                    view,
                    "translationY",
                    *floatArrayOf((-view!!.height / 2).toFloat(), 0.0f)
                ).setDuration(
                    mDuration
                ),
                ObjectAnimator.ofFloat(view, "scaleX", *floatArrayOf(0.3f, 0.5f, 1.0f)).setDuration(
                    mDuration
                ),
                ObjectAnimator.ofFloat(view, "scaleY", *floatArrayOf(0.3f, 0.5f, 1.0f, 1.1f, 1.0f))
                    .setDuration(
                        mDuration
                    ),
                ObjectAnimator.ofFloat(view, "alpha", *floatArrayOf(0.0f, 1.0f)).setDuration(
                    mDuration * 3 / 2
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
                    *floatArrayOf(0.0f, (-view!!.height / 2).toFloat())
                ).setDuration(
                    mDuration
                ),
                ObjectAnimator.ofFloat(view, "scaleX", *floatArrayOf(1.0f, 0.5f, 0.0f)).setDuration(
                    mDuration
                ),
                ObjectAnimator.ofFloat(view, "scaleY", *floatArrayOf(1.0f, 0.5f, 0.0f)).setDuration(
                    mDuration
                ),
                ObjectAnimator.ofFloat(view, "alpha", *floatArrayOf(1.0f, 0.0f)).setDuration(
                    mDuration * 3 / 2
                )
            )
        )
    }

    override fun getAnimDuration(duration: Long): Long {
        return duration
    }
}