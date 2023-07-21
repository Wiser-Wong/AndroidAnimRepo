package com.wiser.animationlistdemo.loginanim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.wiser.animationlistdemo.R
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.android.synthetic.main.layout_login_bear.*

class LoginPageActivity : AppCompatActivity() {

    private val normalSpace: Float by lazy { 18f }

    private val dis1 by lazy { resources.getDimension(R.dimen.ksl_dp_68) }
    private val dis2 by lazy { resources.getDimension(R.dimen.ksl_dp_52) }
    private val animDuration = 1000L
    private var isAnimRunning = false
    private var isOpenEye = false
    private var isCoverEye = false

    //旋转 x方向移动 y方向移动
    //左手臂向上旋转
    private val leftArmUpAnim: AnimatorSet by lazy {
        val rotate = ObjectAnimator.ofFloat(iv_hand_left, "rotation", -110f, 0f).apply {
            duration = animDuration
        }
        //手臂向右移动
        val tx = PropertyValuesHolder.ofFloat("translationX", dis1)
        //手臂向上移动
        val ty = PropertyValuesHolder.ofFloat("translationY", -dis2)
        val translate = ObjectAnimator.ofPropertyValuesHolder(iv_hand_left, tx, ty)

        AnimatorSet().apply {
            playTogether(rotate, translate)
            duration = animDuration
        }
    }

    //左手臂向下旋转
    private val leftArmDownAnim: AnimatorSet by lazy {
        val rotate = ObjectAnimator.ofFloat(iv_hand_left, "rotation", -110f).apply {
            duration = animDuration
        }
        //手臂向左移动
        val tx = PropertyValuesHolder.ofFloat("translationX", 0f)
        //手臂向下移动
        val ty = PropertyValuesHolder.ofFloat("translationY", 0f)
        val translate = ObjectAnimator.ofPropertyValuesHolder(iv_hand_left, tx, ty)

        AnimatorSet().apply {
            playTogether(rotate, translate)
            duration = animDuration
        }
    }

    //右手臂向上旋转
    private val rightArmUpAnim: AnimatorSet by lazy {
        val rotate = ObjectAnimator.ofFloat(iv_hand_right, "rotation", 0f).apply {
            duration = animDuration
        }
        //手臂向左移动
        val tx = PropertyValuesHolder.ofFloat("translationX", -dis1)
        //手臂向上移动
        val ty = PropertyValuesHolder.ofFloat("translationY", -dis2)
        val translate = ObjectAnimator.ofPropertyValuesHolder(iv_hand_right, tx, ty)

        AnimatorSet().apply {
            playTogether(rotate, translate)
            duration = animDuration
        }
    }

    //右手臂向下旋转
    private val rightArmDownAnim: AnimatorSet by lazy {
        val rotate = ObjectAnimator.ofFloat(iv_hand_right, "rotation", 110f).apply {
            duration = animDuration
        }
        //手臂向右移动
        val tx = PropertyValuesHolder.ofFloat("translationX", 0f)
        //手臂向下移动
        val ty = PropertyValuesHolder.ofFloat("translationY", 0f)
        val translate = ObjectAnimator.ofPropertyValuesHolder(iv_hand_right, tx, ty)

        AnimatorSet().apply {
            playTogether(rotate, translate)
            duration = animDuration
        }
    }

    //其他脸部器官动画
    private fun startOtherFaceOrganAnim() {
        val initEndX = if (x != 0f) x else -normalSpace
        val initEndY = normalSpace

        val transEyebrowLeftX =
            ObjectAnimator.ofFloat(iv_eyebrow_left, "translationX", 0f, initEndX)
        val transEyebrowLeftY =
            ObjectAnimator.ofFloat(iv_eyebrow_left, "translationY", 0f, initEndY)
        val transEyebrowRightX =
            ObjectAnimator.ofFloat(iv_eyebrow_right, "translationX", 0f, initEndX)
        val transEyebrowRightY =
            ObjectAnimator.ofFloat(iv_eyebrow_right, "translationY", 0f, initEndY)
        val transEyeLeftX = ObjectAnimator.ofFloat(iv_left_eye, "translationX", 0f, initEndX)
        val transEyeLeftY = ObjectAnimator.ofFloat(iv_left_eye, "translationY", 0f, initEndY)
        val transEyeRightX = ObjectAnimator.ofFloat(iv_right_eye, "translationX", 0f, initEndX)
        val transEyeRightY = ObjectAnimator.ofFloat(iv_right_eye, "translationY", 0f, initEndY)
        val transMouthX = ObjectAnimator.ofFloat(iv_mouth, "translationX", 0f, initEndX)
        val transMouthY = ObjectAnimator.ofFloat(iv_mouth, "translationY", 0f, initEndY)

        AnimatorSet().apply {
            playTogether(
                transEyebrowLeftX,
                transEyebrowLeftY,
                transEyebrowRightX,
                transEyebrowRightY,
                transEyeLeftX,
                transEyeLeftY,
                transEyeRightX,
                transEyeRightY,
                transMouthX,
                transMouthY,
            )
            duration = 800
        }.start()
    }

    private fun resetOtherFaceOrganAnim() {
        if (!isLowerHead) return
        val transEyebrowLeftX = ObjectAnimator.ofFloat(
            iv_eyebrow_left,
            "translationX",
            iv_eyebrow_left.translationX,
            0f
        )
        val transEyebrowLeftY = ObjectAnimator.ofFloat(
            iv_eyebrow_left,
            "translationY",
            iv_eyebrow_left.translationY,
            0f
        )
        val transEyebrowRightX = ObjectAnimator.ofFloat(
            iv_eyebrow_right,
            "translationX",
            iv_eyebrow_right.translationX,
            0f
        )
        val transEyebrowRightY = ObjectAnimator.ofFloat(
            iv_eyebrow_right,
            "translationY",
            iv_eyebrow_right.translationY,
            0f
        )
        val transEyeLeftX = ObjectAnimator.ofFloat(
            iv_left_eye, "translationX",
            iv_left_eye.translationX, 0f
        )
        val transEyeLeftY = ObjectAnimator.ofFloat(
            iv_left_eye, "translationY",
            iv_left_eye.translationY, 0f
        )
        val transEyeRightX =
            ObjectAnimator.ofFloat(iv_right_eye, "translationX", iv_right_eye.translationX, 0f)
        val transEyeRightY =
            ObjectAnimator.ofFloat(iv_right_eye, "translationY", iv_right_eye.translationY, 0f)
        val transMouthX =
            ObjectAnimator.ofFloat(iv_mouth, "translationX", iv_mouth.translationX, 0f)
        val transMouthY =
            ObjectAnimator.ofFloat(iv_mouth, "translationY", iv_mouth.translationY, 0f)

        AnimatorSet().apply {
            playTogether(
                transEyebrowLeftX,
                transEyebrowLeftY,
                transEyebrowRightX,
                transEyebrowRightY,
                transEyeLeftX,
                transEyeLeftY,
                transEyeRightX,
                transEyeRightY,
                transMouthX,
                transMouthY,
            )
            duration = 800
        }.start()
        ip_head.resetHeadAnim()
        isLowerHead = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

//        findViewById<FoldableItemLayout>(R.id.fl_head)?.setFoldRotation(100f)

        et_email?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isEmpty() == true) {
                    startHeadAnim()
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                setEyeTranslation(et_email?.paint?.measureText(p0.toString()) ?: 0f)
                if (p0?.isEmpty() == true) {
                    resetOtherFaceOrganAnim()
                }
            }

        })

        et_email.setOnClickListener {
            if (et_email.length() == 0) {
                startHeadAnim()
            }
        }

        et_email?.setOnFocusChangeListener { view, b ->
            if (b) {
                startHeadAnim()
            }
        }

        et_password?.setOnFocusChangeListener { view, b ->
            if (b) {
                resetOtherFaceOrganAnim()
                coverEye()
            } else {
                openEye()
            }
        }

    }

    private fun startHeadAnim() {
        if (isLowerHead) return
        isLowerHead = true
        ip_head.startHeadAnim()
        startOtherFaceOrganAnim()
    }

    // 眼睛移动距离
    private var x = 0f

    private var isLowerHead: Boolean = false

    /**
     * 设置眼睛移动
     */
    private fun setEyeTranslation(distance: Float) {
        val width: Float =
            (et_email?.width ?: 0).toFloat() - 2 * (et_email?.paddingLeft ?: 0).toFloat()
        var d = distance
        if (d > width) {
            d = width
        }
        println("========>>distance====>$distance")
        println("========>>width====>$width")
        x = ((normalSpace / (width / 2)) * d - normalSpace)
        //越界处理
        if (x < -normalSpace) {
            x = -normalSpace
        }

        val rangeLeftLargeOpen = width / 2 - 150
        val rangeLeftOpen = rangeLeftLargeOpen - 150

        when (d) {
            in 0f..rangeLeftOpen -> {
                iv_mouth.setImageResource(R.drawable.bear_mouth_close)
            }
            in rangeLeftOpen..rangeLeftLargeOpen -> {
                iv_mouth.setImageResource(R.drawable.bear_mouth_open)
            }
            else -> {
                iv_mouth.setImageResource(R.drawable.bear_mouth_large_open)
            }
        }

        val maxY = normalSpace + 15f
        var y = ((maxY - normalSpace) * d) / (width / 2f) + normalSpace
        if (y > maxY) {
            if (y - maxY > normalSpace) {
                y = normalSpace
            } else {
                y = (maxY - normalSpace) - (y - maxY) + normalSpace
            }
        }
        println("========>>y====>$y")

        iv_left_eye?.translationX = x
        iv_right_eye?.translationX = x
        iv_eyebrow_left?.translationX = x
        iv_eyebrow_right?.translationX = x
        iv_mouth?.translationX = x
        iv_left_eye.translationY = y
        iv_right_eye.translationY = y
        iv_eyebrow_left.translationY = y
        iv_eyebrow_right.translationY = y
        iv_mouth?.translationY = y
    }

    /**
    遮住眼睛
    两只手掌同时向下移动  ->两只手同时旋转
    AnimatorSet
    实现创建一次有两种方法
    懒加载
    定义一个变量  定义一个方法  在方法中判断这个变量是否有值
     */
    private fun coverEye() {
        isCoverEye = true
        if (isAnimRunning) {
            return
        }
        isAnimRunning = true
        AnimatorSet().apply {
            play(leftArmUpAnim)
                .with(rightArmUpAnim)
            start()
            doOnEnd {
                isAnimRunning = false
                isCoverEye = false
                if (isOpenEye) {
                    openEye()
                }
            }
        }
    }

    /**
    打开眼睛
    两只手同时旋转  ->两个手掌同时向上移动
     */
    private fun openEye() {
        isOpenEye = true
        if (isAnimRunning) {
            return
        }
        isAnimRunning = true
        AnimatorSet().apply {
            play(leftArmDownAnim)
                .with(rightArmDownAnim)
            start()
            doOnEnd {
                isAnimRunning = false
                isOpenEye = false
                if (isCoverEye) {
                    coverEye()
                }
            }
        }
    }
}