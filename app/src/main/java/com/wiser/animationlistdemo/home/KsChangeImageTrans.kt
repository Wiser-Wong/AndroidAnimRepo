package com.wiser.animationlistdemo.home

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Matrix
import android.graphics.Rect
import android.transition.ChangeImageTransform
import android.transition.TransitionValues
import android.util.AttributeSet
import android.util.Property
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType

/**
 * @date: 2023/6/12 15:56
 * @author: jiaruihua
 * @desc :
 *
 */
class KsChangeImageTrans : ChangeImageTransform() {

    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {

        println("KsChangeImageTrans------st=${startValues?.view?.rotation}---en=${endValues?.view?.rotation}")

        return super.createAnimator(sceneRoot, startValues, endValues)

    }

}