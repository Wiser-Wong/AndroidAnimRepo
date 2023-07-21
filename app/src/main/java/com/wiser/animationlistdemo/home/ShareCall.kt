package com.wiser.animationlistdemo.home

import android.app.SharedElementCallback
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Parcelable
import android.view.View
import com.nineoldandroids.animation.ObjectAnimator

/**
 * @date: 2023/6/7 14:43
 * @author: jiaruihua
 * @desc :
 *
 */
class ShareCall:SharedElementCallback() {
    override fun onSharedElementStart(
        sharedElementNames: MutableList<String>?,
        sharedElements: MutableList<View>?,
        sharedElementSnapshots: MutableList<View>?
    ) {
        super.onSharedElementStart(
            sharedElementNames,
            sharedElements,
            sharedElementSnapshots
        )

        println("c--------------------------------00")
    }

    override fun onSharedElementEnd(
        sharedElementNames: MutableList<String>?,
        sharedElements: MutableList<View>?,
        sharedElementSnapshots: MutableList<View>?
    ) {
        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
        println("c--------------------------------11")

        sharedElements?.forEach {
            it.rotation = 0f
        }
    }

    override fun onCaptureSharedElementSnapshot(
        sharedElement: View?,
        viewToGlobalMatrix: Matrix?,
        screenBounds: RectF?
    ): Parcelable {
        println("c--------------------------------22")
        return super.onCaptureSharedElementSnapshot(
            sharedElement,
            viewToGlobalMatrix,
            screenBounds
        )


    }

    override fun onCreateSnapshotView(context: Context?, snapshot: Parcelable?): View {
        println("c--------------------------------33")
        return super.onCreateSnapshotView(context, snapshot)
    }

    override fun onMapSharedElements(
        names: MutableList<String>?,
        sharedElements: MutableMap<String, View>?
    ) {
        super.onMapSharedElements(names, sharedElements)
        println("c--------------------------------44")
    }

    override fun onSharedElementsArrived(
        sharedElementNames: MutableList<String>?,
        sharedElements: MutableList<View>?,
        listener: OnSharedElementsReadyListener?
    ) {
        super.onSharedElementsArrived(sharedElementNames, sharedElements, listener)

//        sharedElements?.forEach {
//            println("c-----------------------it=${it.id}--${it.transitionName}--it.rotation=${it.rotation}---------55")
//            ObjectAnimator.ofFloat(it,"rotation",it.rotation,0f).apply {
//                duration = 3000
//                start()
//            }
//        }
    }

    override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
        super.onRejectSharedElements(rejectedSharedElements)
        println("c--------------------------------66")
    }

}