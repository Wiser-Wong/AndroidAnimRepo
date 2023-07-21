package com.wiser.animationlistdemo.niftynotification.api.effects

import android.view.View
import com.nineoldandroids.animation.Animator
import com.nineoldandroids.animation.ObjectAnimator
import com.nineoldandroids.view.ViewHelper

class ThumbSlider : BaseEffect() {
    var e = (mDuration - 200) / 2
    var iconView: View? = null
    var m: Long = 200
    var msgView: View? = null
    var s = (mDuration - 200) / 2
    override fun setInAnimation(view: View?) {
        iconView = view!!.findViewById(16908294)
        if (iconView != null) {
            msgView = view.findViewById(16908299)
            ViewHelper.setAlpha(msgView, 0.0f)
            ViewHelper.setPivotX(msgView, 0.0f)
            ViewHelper.setPivotY(msgView, 0.0f)
            val msgScaleAnim = ObjectAnimator.ofFloat(
                msgView,
                "scaleX",
                *floatArrayOf(0.0f, 0.5f, 1.0f, 1.1f, 1.0f)
            ).setDuration(
                s * 2
            )
            val msgAlphaAnim =
                ObjectAnimator.ofFloat(msgView, "alpha", *floatArrayOf(1.0f)).setDuration(
                    s * 2
                )
            msgScaleAnim.startDelay = s + m
            msgAlphaAnim.startDelay = s + m
            animatorSet.playTogether(
                *arrayOf<Animator>(
                    ObjectAnimator.ofFloat(
                        iconView, "scaleX", *floatArrayOf(0.0f, 0.5f, 1.0f, 0.9f, 1.0f, 1.2f, 1.0f)
                    ).setDuration(
                        s
                    ),
                    ObjectAnimator.ofFloat(
                        iconView,
                        "scaleY",
                        *floatArrayOf(0.0f, 0.5f, 1.0f, 1.2f, 1.0f, 0.9f, 1.0f)
                    ).setDuration(
                        s
                    ),
                    msgScaleAnim,
                    msgAlphaAnim
                )
            )
        }
    }

    override fun setOutAnimation(view: View?) {
        iconView = view!!.findViewById(16908294)
        if (iconView != null) {
            msgView = view.findViewById(16908299)
            val iconScaleXAnim = ObjectAnimator.ofFloat(
                iconView,
                "scaleX",
                *floatArrayOf(1.0f, 1.2f, 1.0f, 0.9f, 1.0f, 0.5f, 0.0f)
            ).setDuration(
                e * 2
            )
            val iconScaleYAnim = ObjectAnimator.ofFloat(
                iconView,
                "scaleY",
                *floatArrayOf(1.0f, 0.9f, 1.0f, 1.2f, 1.0f, 0.5f, 0.0f)
            ).setDuration(
                e * 2
            )
            val iconAlphaAnim =
                ObjectAnimator.ofFloat(iconView, "alpha", *floatArrayOf(1.0f, 0.0f)).setDuration(
                    e * 2
                )
            iconScaleXAnim.startDelay = e + m
            iconScaleYAnim.startDelay = e + m
            iconAlphaAnim.startDelay = e + m
            animatorSet.playTogether(
                *arrayOf<Animator>(
                    ObjectAnimator.ofFloat(
                        msgView, "scaleX", *floatArrayOf(1.0f, 1.1f, 1.0f, 0.5f, 0.0f)
                    ).setDuration(e), iconScaleXAnim, iconScaleYAnim, iconAlphaAnim
                )
            )
        }
    }

    override fun getAnimDuration(duration: Long): Long {
        return 2 * duration + 200
    }
}