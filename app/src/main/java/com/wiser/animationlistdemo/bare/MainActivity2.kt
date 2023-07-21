package com.example.bare

import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Slide
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.wiser.animationlistdemo.R


class MainActivity2 : AppCompatActivity() {
    private var isfirstlookOut: Boolean = true
    lateinit var rootImage: View
    lateinit var buildContainerTransform: ChangeBounds
    private val mRootImage: ImageView by lazy { findViewById<ImageView>(R.id.root_image) }
    private val mGrass: ImageView by lazy { findViewById<ImageView>(R.id.grass) }
    private val mLeftFirstLook: ImageView by lazy { findViewById<ImageView>(R.id.left_first_look) }
    private val mRightLook: ImageView by lazy { findViewById<ImageView>(R.id.left_right_look) }
    private val mCheckCover: View by lazy { findViewById<View>(R.id.check_cover) }
    private val mTvTocal: TextView by lazy { findViewById<TextView>(R.id.tv_tocal) }
    private val mCalculator: RecyclerView by lazy { findViewById<RecyclerView>(R.id.calculator) }
    private val mAniRightLeft: ImageView by lazy { findViewById<ImageView>(R.id.ani_right_left) }
    private val mAniRightRight: ImageView by lazy { findViewById<ImageView>(R.id.ani_right_right) }
    private val mAniWrongLeft: ImageView by lazy { findViewById<ImageView>(R.id.ani_wrong_left) }
    private val mAniWrongRight: ImageView by lazy { findViewById<ImageView>(R.id.ani_wrong_right) }
    private val adapter:CalculateAdapter by lazy { CalculateAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.allowEnterTransitionOverlap = false
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        val inflate = LayoutInflater.from(this).inflate(R.layout.parent_loackactivity_main2, null, false)
        rootImage = inflate.findViewById<View>(R.id.root_image)
        buildContainerTransform = buildContainerTransform()
        window.sharedElementEnterTransition = buildContainerTransform

        val slide = Slide()
        slide.addTarget(rootImage)
        window.sharedElementReturnTransition = slide
        window.sharedElementsUseOverlay = false
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {

            }
        })

        setContentView(inflate)

        onEnterAni()
        configRecyclerView()

    }

    /**
     * Config recycler view.
     */
    fun configRecyclerView(){
        mCalculator.layoutManager = GridLayoutManager(this,2)
        mCalculator.adapter = adapter
    }

    private fun buildContainerTransform() =
        ChangeBounds().apply {
            addTarget(rootImage)
            duration = 300
            interpolator = FastOutSlowInInterpolator()
//            fadeMode = MaterialContainerTransform.FADE_MODE_IN
        }

    override fun onBackPressed() {
//        super.onBackPressed()

//        window.sharedElementEnterTransition = null
        ActivityCompat.finishAfterTransition(this)
        finish()
        overridePendingTransition(0, R.anim.bottom_slide_out)
    }

    /**
     * 进入动画
     * On enter ani.
     */
    fun onEnterAni() {
        //草坪动画
        val fromBot = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            1f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
        fromBot.apply {
            setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mGrass.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        //左右ip动画
                        onFirstLook()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                }
            )
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }
        mGrass.startAnimation(fromBot)

        //计算
        onGenerateCalculatorAni()

    }

    fun onGenerateCalculatorAni() {
        val str = "3+4x9=..."
        val animator = ValueAnimator.ofInt(0,str.length)
        animator.duration = 1000
        animator.addUpdateListener {
            mTvTocal.text = str.subSequence(0, it.animatedValue as Int).toString()
        }
        animator.start()

        val set = hashSetOf<Int>((39 + Math.random()*-40).toInt(),(39 + Math.random()*-40).toInt(),(39 + Math.random()*-40).toInt(),(39 + Math.random()*40).toInt(),(39 + Math.random()*40).toInt(),39)
        val newList = arrayListOf<Int>()
        set.forEach {
            newList.add(it)
        }
        adapter.setData(newList)
    }

    fun onFirstLook() {
        val fromLeft = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            -1f,
            Animation.RELATIVE_TO_SELF,
            -0.3f,
            Animation.RELATIVE_TO_SELF,
            -0.1f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
            .apply {
                duration = 500
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mLeftFirstLook.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }
        val fromRight = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.45f,
            Animation.RELATIVE_TO_SELF,
            -0.2f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
            .apply {
                duration = 500
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mRightLook.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {

                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }

        mLeftFirstLook.startAnimation(fromLeft)
        mRightLook.postDelayed({
            mRightLook.startAnimation(fromRight)
        }, 100)


    }

    fun firstLookOut() {
        if (!isfirstlookOut)return
        isfirstlookOut = false
        val outLeft = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            -0.3f,
            Animation.RELATIVE_TO_SELF,
            -2f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            -0.10f
        )
            .apply {
                duration = 500
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mLeftFirstLook.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }
        val outRight = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0.45f,
            Animation.RELATIVE_TO_SELF,
            1.5f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            -0.2f
        )
            .apply {
                duration = 500
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mRightLook.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }

        mLeftFirstLook.startAnimation(outLeft)
        mRightLook.postDelayed({
            mRightLook.startAnimation(outRight)
        }, 100)
    }

    fun onCheckAni(corect:Boolean) {
        firstLookOut()
        if (corect){
            onCheckRight()
        } else {
            onCheckError()
        }
    }

    fun onCheckRight() {
        val toBottom = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_PARENT,
            0f,
            Animation.RELATIVE_TO_PARENT,
            1.0f
        )
            .apply {
                duration = 1000
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mAniRightLeft.visibility = View.VISIBLE
                        mAniRightRight.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        mAniRightLeft.visibility = View.INVISIBLE
                        mAniRightRight.visibility = View.INVISIBLE
                        onProcessOverAni()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }


        val fromBottom = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_PARENT,
            1f,
            Animation.RELATIVE_TO_PARENT,
            0.0f
        )
            .apply {
                interpolator = OvershootInterpolator(1f)
                duration = 1000
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mAniRightLeft.visibility = View.VISIBLE
                        mAniRightRight.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        mAniRightRight.postDelayed({
                            mAniRightLeft.startAnimation(toBottom)
                            mAniRightRight.startAnimation(toBottom)
                        }, 1000)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }
        mAniRightLeft.startAnimation(fromBottom)
        mAniRightRight.startAnimation(fromBottom)


    }

    fun onCheckError() {
        val toBottom = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_PARENT,
            0f,
            Animation.RELATIVE_TO_PARENT,
            1.0f
        )
            .apply {
                duration = 1000
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mAniWrongLeft.visibility = View.VISIBLE
                        mAniWrongRight.visibility = View.VISIBLE
                        mCheckCover.visibility = View.INVISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        mAniWrongLeft.visibility = View.INVISIBLE
                        mAniWrongRight.visibility = View.INVISIBLE
                        onGenerateCalculatorAni()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }


        val fromBottom = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_PARENT,
            1f,
            Animation.RELATIVE_TO_PARENT,
            0.0f
        )
            .apply {
                interpolator = LinearInterpolator()
                duration = 1000
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        mAniWrongLeft.visibility = View.VISIBLE
                        mAniWrongRight.visibility = View.VISIBLE
                        mCheckCover.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        mAniRightRight.postDelayed({
                            mAniWrongLeft.startAnimation(toBottom)
                            mAniWrongRight.startAnimation(toBottom)
                        }, 1000)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }
        mAniWrongLeft.startAnimation(fromBottom)
        mAniWrongRight.startAnimation(fromBottom)
    }


    fun onProcessOverAni() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(com.google.android.material.R.anim.design_bottom_sheet_slide_in,0)
            .replace(  R.id.score_fragment,ScoreFragment())
            .commitNowAllowingStateLoss()
    }

}