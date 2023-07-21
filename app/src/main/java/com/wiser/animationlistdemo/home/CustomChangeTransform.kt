package com.wiser.animationlistdemo.home

import android.animation.Animator
import android.graphics.RectF
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionValues
import android.view.ViewGroup

/**
 * @date: 2023/6/12 11:43
 * @author: jiaruihua
 * @desc :
 *
 */
class CustomChangeTransform : ChangeTransform(){


    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        val startBounds = startValues?.values?.get("android:changeImageTransform:bounds") as? RectF
        val endBounds =endValues?.values?.get("android:changeImageTransform:bounds") as? RectF


        println("CustomChangeTransform-------start=$startBounds---end=$endBounds")


        val ani = super.createAnimator(sceneRoot, startValues, endValues)

        ani?.duration = 5000

        return ani
    }
}