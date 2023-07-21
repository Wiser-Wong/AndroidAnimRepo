package com.wiser.animationlistdemo.list.ani

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/3/14 10:16
 * @author: jiaruihua
 * @desc :
 *
 */

const val SCALE_ANI_DURATIION = 300L
const val SCALE_ANI_DURATIION_300 = 300L
const val SCALE_ANI_DURATIION_500 = 300L
const val SCALE_ANI_DURATIION_200 = 300L
const val SCALE_ANI_DURATIION_100 = 100L


const val ALPHA_ANI_DURATION_300 = 300L
const val ALPHA_ANI_DURATION_800 = 800L
const val ALPHA_ANI_DURATION_500 = 500L


fun topAni(view: View, delay: Long = 0L) {

    val alpha = ValueAnimator.ofFloat(0f, 1f).apply {
        repeatCount = 0
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            view.alpha = it.animatedValue as Float
        }
    }

    val trans = ValueAnimator.ofFloat(view.resources.getDimension(R.dimen.ksl_dp_100), 0f).apply {
        repeatCount = 0
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            view.translationY = it.animatedValue as Float
        }
    }

    AnimatorSet().apply {
        duration = ALPHA_ANI_DURATION_800
        startDelay = delay
        playTogether(alpha, trans)
    }.start()
}


fun topAni1(view: View, delay: Long = 0L) {
    view.alpha = 0f
    view.visibility = View.VISIBLE
    val alpha = ValueAnimator.ofFloat(0f, 1f).apply {
        repeatCount = 0
        duration = ALPHA_ANI_DURATION_500
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            view.alpha = it.animatedValue as Float
        }
    }



    val animatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f)
    animatorScaleX.duration = ALPHA_ANI_DURATION_500
    animatorScaleX.interpolator = LinearInterpolator()

    val animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f)
    animatorScaleX.duration = ALPHA_ANI_DURATION_500
    animatorScaleX.interpolator = LinearInterpolator()

    val trans = ValueAnimator.ofFloat(view.resources.getDimension(R.dimen.ksl_dp_100), 0f).apply {
        repeatCount = 0
        duration = ALPHA_ANI_DURATION_500
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            view.translationY = it.animatedValue as Float
        }
    }

    AnimatorSet().apply {
        duration = ALPHA_ANI_DURATION_500
        startDelay = delay
        playTogether(alpha, trans, animatorScaleX, animatorScaleY)
    }.start()
}

fun topAniHide(view: View, delay: Long = 0L,complete:()->Unit) {

    val alpha = ValueAnimator.ofFloat(1f, 0f).apply {
        repeatCount = 0
        duration = ALPHA_ANI_DURATION_300
        interpolator = LinearInterpolator()
        addUpdateListener {
            view.alpha = it.animatedValue as Float
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
                complete.invoke()
                println("eeeeeeeeeeeeeeeeND-----alpha")
            }
        })
    }


    val trans = ValueAnimator.ofFloat(0f, 100f).apply {
        repeatCount = 0
        duration = ALPHA_ANI_DURATION_300
        interpolator = LinearInterpolator()
        addUpdateListener {
            view.translationY = it.animatedValue as Float
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                println("eeeeeeeeeeeeeeeeND-----trans")
            }
        })
    }

    AnimatorSet().apply {
        duration = ALPHA_ANI_DURATION_300
        startDelay = delay
        playTogether(alpha, trans)

        addListener {
            doOnEnd {

                println("eeeeeeeeeeeeeeeeND-----set")
            }
        }
    }.start()
}

fun alphaStyle1(view: View, delay: Long = 0L, listener: Animator.AnimatorListener?) {
    ValueAnimator.ofFloat(0f, 1f).run {
        duration = ALPHA_ANI_DURATION_300
        repeatCount = 0
        startDelay = delay
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            view.alpha = it.animatedValue as Float
        }

        start()
    }


}

/**
 * 控制周边小点
 * @param view View
 * @param listener AnimatorListener?
 */
fun scaleStyle1(view: View, delay: Long = 0L, listener: Animator.AnimatorListener?) {
    val scaleValue = ValueAnimator.ofFloat(0f, 0.1f, 0.5f, 0.8f, 1f)
    scaleValue.run {
        duration = SCALE_ANI_DURATIION_200
        repeatCount = 0
        startDelay = delay
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
//        LogUtil.e("ani-style1==${it.animatedValue as Float}")
            view.scaleX = it.animatedValue as Float
            view.scaleY = it.animatedValue as Float
        }
//        listener?.let {
//            addListener(it)
//        }

        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                scaleStyle4(view, null)
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }

        })
        start()

    }
}

fun scaleStyle4(view: View, listener: Animator.AnimatorListener?) {
    val scaleValue = ValueAnimator.ofFloat(1f, 0.8f, 0.1f, 0f)
    scaleValue.run {
        duration = SCALE_ANI_DURATIION
        repeatCount = 0
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
//            LogUtil.e("ani-style1==${it.animatedValue as Float}")
            view.scaleX = it.animatedValue as Float
            view.scaleY = it.animatedValue as Float
        }
        listener?.let {
            addListener(it)
        }
        start()

    }
}

fun scaleStyle2(view: View, listener: Animator.AnimatorListener?) {
    val scaleValue = ValueAnimator.ofFloat(1f, 1.3f)
    scaleValue.run {
        duration = SCALE_ANI_DURATIION_500
        repeatCount = 0
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
//            LogUtil.e("ani-style2==${it.animatedValue as Float}")
            view.scaleX = it.animatedValue as Float
            view.scaleY = it.animatedValue as Float
        }
        listener?.let {
            addListener(it)
        }
        start()

    }
}


fun scaleStyle3(view: View, listener: Animator.AnimatorListener?) {
    val scaleValue = ValueAnimator.ofFloat(1.3f, 1f)
    scaleValue.run {
        duration = SCALE_ANI_DURATIION_300
        repeatCount = 0
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
//            LogUtil.e("ani-style2==${it.animatedValue as Float}")
            view.scaleX = it.animatedValue as Float
            view.scaleY = it.animatedValue as Float
        }
        listener?.let {
            addListener(it)
        }
        start()

    }
}






