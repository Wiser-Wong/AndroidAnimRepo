package com.wiser.animationlistdemo.home

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

/**
 * @date: 2023/6/7 10:44
 * @author: jiaruihua
 * @desc : 缩放 x ,y
 *
 */
class CardTransition : Transition() {
    /**
     * 宽
     */
    val TRANSITION_WIDTH = "com.wiser.animationlistdemo.home:changeWidth:width"

    /**
     * 高
     */
    val TRANSITION_HEIGHT = "com.wiser.animationlistdemo.home:changeHeight:height"

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        transitionValues.values[TRANSITION_WIDTH] = transitionValues.view.width
        transitionValues.values[TRANSITION_HEIGHT] = transitionValues.view.height
    }


    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {

        if (startValues == null || endValues == null) return null

        val view = endValues.view

        val startWith = startValues.values[TRANSITION_WIDTH]
        val endWidth = endValues.values[TRANSITION_WIDTH]
        val startHeight = startValues.values[TRANSITION_HEIGHT]
        val endHeight = endValues.values[TRANSITION_HEIGHT]
        var ani: AnimatorSet
        var aniW: ValueAnimator?=null
        var aniH: ValueAnimator?=null
        if (startWith is Int && endWidth is Int) {
            aniW = ValueAnimator.ofFloat(startWith.toFloat(), endWidth.toFloat()).apply {
                duration = 300
                addUpdateListener { animation->
                    view.scaleX = (animation.animatedValue as Float)/startWith
                }
            }

        }

        if (startHeight is Int && endHeight is Int) {
            aniH = ValueAnimator.ofFloat(startHeight.toFloat(), endHeight.toFloat()).apply {
                duration = 300
                addUpdateListener { animation->
                    view.scaleX = (animation.animatedValue as Float)/startHeight
                }
            }

        }

        if (aniW!=null&&aniH!=null){
            ani = AnimatorSet().apply { playTogether(aniW,aniH) }
            return ani
        }

        return super.createAnimator(sceneRoot, startValues, endValues)
    }


}