package com.wiser.animationlistdemo.home

import android.animation.Animator
import android.animation.ObjectAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * @date: 2023/5/12 10:57
 * @author: jiaruihua
 * @desc :
 *
 */
class RotationTransition : Transition(){

    companion object {
        private const val ROTATION_PROPERTY = "rotating_transition:rotation"
    }

    override fun getTransitionProperties(): Array<String> {
        return arrayOf(ROTATION_PROPERTY)
    }
    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        if (transitionValues.view !is ImageView) return
        println("RotationTransition-----sss---${transitionValues.view.rotation}---")
        transitionValues.values[ROTATION_PROPERTY] = transitionValues.view.rotation
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        println("RotationTransition-----111---startValues=${startValues}")
        println("RotationTransition-----2---endValues=${endValues}")

        // Only create animator if start and end values exist, and have different rotation values.
//        if (startValues == null || endValues == null ||
//            startValues.values[ROTATION_PROPERTY] == endValues.values[ROTATION_PROPERTY]
//        ) {
//            return null
//        }
        if (startValues == null || endValues == null) {
            return null
        }

        val view = endValues.view ?: startValues.view
        var startRotation = startValues.values[ROTATION_PROPERTY] as Float
        var endRotation = endValues.values[ROTATION_PROPERTY] as Float

        endRotation = startRotation+300

        println("RotationTransition--------startRotation=$startRotation-----endRotation=$endRotation")
        // Set the start rotation so the animation starts from the correct value.
        view.rotation = startRotation

        // Create an ObjectAnimator to animate the rotation.
        return ObjectAnimator.ofFloat(view, View.ROTATION, startRotation, endRotation)
            .apply {
                duration = 3000
            }
    }
}