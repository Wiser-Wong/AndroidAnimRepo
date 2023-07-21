package com.wiser.animationlistdemo.ticket

import android.animation.Animator
import android.view.MotionEvent
import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener

/**
 * @date: 2023/5/16 10:44
 * @author: jiaruihua
 * @desc :
 *
 */
class SnapHelper(val stackView: StackView) : View.OnTouchListener {

    private var mDownX = 0f
    private var mDownY = 0f
    private var mInitialX = 0f
    private var mInitialY = 0f
    private var mPointerId = 0
    private var mObservedView: View? = null


    val DEFAULT_ANIMATION_DURATION = 300
    val DEFAULT_STACK_SIZE = 3
    val DEFAULT_STACK_ROTATION = 8
    val DEFAULT_SWIPE_ROTATION = 30f
    val DEFAULT_SWIPE_OPACITY = 1f
    val DEFAULT_SCALE_FACTOR = 1f

    var dragDx = 0f

    private var mLitenForTouchEvents = false
    private var  mRotateDegrees = DEFAULT_STACK_ROTATION
    private var  mAnimationDuration = DEFAULT_ANIMATION_DURATION


    fun registerObservedView(view: View?, initialX: Float, initialY: Float) {
        if (view == null) return
        mObservedView = view
        mObservedView?.setOnTouchListener(this)
        mInitialX = initialX
        mInitialY = initialY
        mLitenForTouchEvents = true
    }

    fun unregisterObservedView() {
        if (mObservedView != null) {
            mObservedView?.setOnTouchListener(null)
        }
        mObservedView = null
        mLitenForTouchEvents = false
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        mObservedView?.let { targetView ->

            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!mLitenForTouchEvents) return false
                    v?.parent?.requestDisallowInterceptTouchEvent(true)

                    mPointerId = event.getPointerId(0)
                    mDownX = event.getX(mPointerId)
                    mDownY = event.getY(mPointerId)

                    return true
                }
                MotionEvent.ACTION_MOVE -> {

                    val pointerIndex = event.findPointerIndex(mPointerId)
                    if (pointerIndex < 0) return false

                    val dx = event.getX(pointerIndex) - mDownX
                    val dy = event.getY(pointerIndex) - mDownY

                    val newX = targetView.x + dx
                    val newY = targetView.y+dy

                    targetView.x = newX
//                    targetView.y = newY

                     dragDx = newX-mInitialX


//                    val viewCenterHorizontal = targetView.x
//                    val parentFirstThird = stackView.width/2f*sensitivity-viewCenterHorizontal
                    val dis = moveDis(targetView)
                    val swipProgress = dragDx / dis
                    stackView.onSwipeProgress(swipProgress)

                    if (dragDx<dis){
                        println("SnapHelper-----dragDx=$dragDx-------Progress=$swipProgress---------left")
                    }else{
                        println("SnapHelper-----dragDx=$dragDx-------Progress=$swipProgress-----------right")
                    }


                    if (mRotateDegrees>0){
                        val rotation = mRotateDegrees*swipProgress
                        targetView.rotation = rotation.toFloat()
                    }

                    return true

                }
                MotionEvent.ACTION_UP -> {

                    v?.parent?.requestDisallowInterceptTouchEvent(false)
                    stackView.onSwipeEnd()
                    checkViewPosition()

                    return true

                }

                else->{}
            }


        }


        return false
    }

    /**
     * 敏感系数
     */
    private val sensitivity = 0.7f

    private fun checkViewPosition() {

        mObservedView?.let {targetView ->
            val viewCenterHorizontal = targetView.x
            val dis = moveDis(targetView)


            if (dragDx<dis){
                println("SnapHelper--------mInitialX=$mInitialX----swipToLeft")

                swipToLeft(mAnimationDuration/2)
            }

            if (dragDx>dis){
                println("SnapHelper--------mInitialX=$mInitialX------swipToRight")

                swipToRight(mAnimationDuration/2)
            }
        }


    }

    private fun moveDis(targetView: View) = targetView.width / 3f * sensitivity

    private fun swipToLeft(time: Int) {
        if (!mLitenForTouchEvents) return
        mLitenForTouchEvents = false
        mObservedView?.let {
            it.animate().cancel()
            it.animate().x(mInitialX).rotation(0f)
                .apply {
                    duration = time.toLong()
                } .setListener(object :Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        stackView.onSwipeProgress(0f)
                        mLitenForTouchEvents = true

                    }

                    override fun onAnimationCancel(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                    }

                })

        }
    }


    private fun swipToRight(time: Int) {
        if (!mLitenForTouchEvents) return
        mLitenForTouchEvents = false

        mObservedView?.let {
            it.animate().cancel()
            it.animate().x(stackView.width.toFloat())
                .apply {
                    duration = time.toLong()
                } .setListener(object :Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        mLitenForTouchEvents = true
                        stackView.onNextShow()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                    }

                })

        }
    }

}