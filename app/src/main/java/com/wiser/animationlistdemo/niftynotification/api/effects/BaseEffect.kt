package com.wiser.animationlistdemo.niftynotification.api.effects

import android.view.View
import com.nineoldandroids.animation.AnimatorSet
import com.nineoldandroids.view.ViewHelper
import com.wiser.animationlistdemo.niftynotification.api.Configuration

abstract class BaseEffect {
    val animatorSet = AnimatorSet()
    var mDuration = DURATION.toLong()
    abstract fun getAnimDuration(j: Long): Long
    abstract fun setInAnimation(view: View?)
    abstract fun setOutAnimation(view: View?)
    fun `in`(view: View) {
        reset(view)
        setInAnimation(view)
        animatorSet.start()
    }

    fun out(view: View) {
        reset(view)
        setOutAnimation(view)
        animatorSet.start()
    }

    fun reset(view: View) {
        ViewHelper.setPivotX(view, view.width.toFloat() / 2.0f)
        ViewHelper.setPivotY(view, view.height.toFloat() / 2.0f)
    }

    fun setDuration(duration: Long): BaseEffect {
        mDuration = duration
        return this
    }

    val duration: Long
        get() = getAnimDuration(mDuration)

    companion object {
        const val DURATION = Configuration.ANIM_DURATION
    }
}