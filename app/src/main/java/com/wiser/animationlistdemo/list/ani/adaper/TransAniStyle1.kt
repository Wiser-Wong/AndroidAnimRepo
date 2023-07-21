package com.wiser.animationlistdemo.list.ani.adaper

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.chad.library.adapter.base.animation.BaseAnimation
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/3/14 18:20
 * @author: jiaruihua
 * @desc :
 *
 */
class TransAniStyle1:BaseAnimation {
    override fun animators(view: View): Array<Animator> {
        val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        animator.duration = 300L
        animator.interpolator = LinearInterpolator()

        val animatorScaleX= ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f)
        animatorScaleX.duration = 300L
        animatorScaleX.interpolator = LinearInterpolator()

        val animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f)
        animatorScaleY.duration = 300L
        animatorScaleY.interpolator = LinearInterpolator()

        val animatorTrns = ObjectAnimator.ofFloat(view, "translationX", view.resources.getDimension(
           R.dimen.ksl_dp_100), 0f)
        animatorTrns.duration = 300L
        animatorTrns.interpolator = DecelerateInterpolator(2.0f)
        return arrayOf(animator,animatorTrns,animatorScaleX,animatorScaleY)
    }

}

class TransAniStyle3:BaseAnimation {
    override fun animators(view: View): Array<Animator> {
        val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        animator.duration = 300L
        animator.interpolator = LinearInterpolator()

        val animatorScaleX= ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f)
        animatorScaleX.duration = 300L
        animatorScaleX.interpolator = LinearInterpolator()

        val animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f)
        animatorScaleY.duration = 300L
        animatorScaleY.interpolator = LinearInterpolator()

        val animatorTrns = ObjectAnimator.ofFloat(view, "translationX", view.resources.getDimension(
            R.dimen.ksl_dp_100), 0f)
        animatorTrns.duration = 500L
        animatorTrns.interpolator = DecelerateInterpolator(2.0f)
        return arrayOf(animator,animatorTrns,animatorScaleX,animatorScaleY)
    }

}


class TransAniStyle2:BaseAnimation {
    override fun animators(view: View): Array<Animator> {
        val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        animator.duration = 300L
        animator.interpolator =DecelerateInterpolator()

        val animatorRotate = ObjectAnimator.ofFloat(view, "rotation", 355f, 360f)
        animatorRotate.duration = 200L
        animatorRotate.interpolator =LinearInterpolator()

        val animatorScaleX= ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f)
        animatorScaleX.duration = 300L
        animatorScaleX.interpolator = LinearInterpolator()

        val animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f)
        animatorScaleY.duration = 300L
        animatorScaleY.interpolator = LinearInterpolator()

        val animatorTrns = ObjectAnimator.ofFloat(view, "translationY", view.resources.getDimension(
        R.dimen.ksl_dp_200), 0f)
        animatorTrns.duration = 800L
        animatorTrns.interpolator = DecelerateInterpolator(2.0f)
        return arrayOf(animator,animatorTrns,animatorScaleX,animatorScaleY,animatorRotate)
    }

}