package com.wiser.animationlistdemo.audio

import android.animation.ObjectAnimator
import androidx.recyclerview.widget.RecyclerView

/**
 * @date: 2023/6/8 14:48
 * @author: jiaruihua
 * @desc :
 *
 */
class TranslateItemAnimator: RecyclerView.ItemAnimator() {
    override fun animateDisappearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo?
    ): Boolean {

        val animator = ObjectAnimator.ofFloat(viewHolder?.itemView, "translationX", viewHolder?.itemView?.width?.toFloat() ?: 0f, 0f)
        animator.duration = 500
        animator.startDelay = 100
        animator.start()
        return true
    }

    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo?,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        return false
    }

    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        return false
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        return false
    }

    override fun runPendingAnimations() {
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
    }

    override fun endAnimations() {
    }

    override fun isRunning(): Boolean {
        return false
    }
}