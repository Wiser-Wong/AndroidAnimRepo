package com.wiser.animationlistdemo.search.listener

import android.animation.Animator

abstract class AnimationListener : Animator.AnimatorListener {

    override fun onAnimationRepeat(animation: Animator) = Unit

    override fun onAnimationStart(animation: Animator) = Unit

    override fun onAnimationCancel(animation: Animator) = Unit

    abstract override fun onAnimationEnd(animation: Animator)

}