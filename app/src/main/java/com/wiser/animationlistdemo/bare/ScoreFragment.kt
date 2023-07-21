package com.example.bare

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import com.github.lzyzsd.circleprogress.ArcProgress
import com.wiser.animationlistdemo.R

/**
 *
 */
class ScoreFragment:Fragment() {
    private val imClose: ImageView by lazy { requireView().findViewById<ImageView>(R.id.imClose) }
    private val l1: ImageView by lazy { requireView().findViewById<ImageView>(R.id.l1) }
    private val l2: ImageView by lazy { requireView().findViewById<ImageView>(R.id.l2) }
    private val l3: ImageView by lazy { requireView().findViewById<ImageView>(R.id.l3) }
    private val l4: ImageView by lazy { requireView().findViewById<ImageView>(R.id.l4) }
    private val bg1: FrameLayout by lazy { requireView().findViewById<FrameLayout>(R.id.bg1) }
    private val arcProgress: ArcProgress by lazy { requireView().findViewById<ArcProgress>(R.id.arc_progress) }
    private val duck: ImageView by lazy { requireView().findViewById<ImageView>(R.id.duck) }
    private val cat: ImageView by lazy { requireView().findViewById<ImageView>(R.id.cat) }
    private val list: LinearLayout by lazy { requireView().findViewById<LinearLayout>(R.id.list) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.score_fragment,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imClose.setOnClickListener {
            requireActivity().finish()
        }
        //1.向上推出列表
        pushUpList()
        //2.渐显头部分数区域
        showHeader{
            //3.宠物弹出
            popCats{
                //4.跑分
                runScore{
                    //5.黄鸭子跳起
                    duckFly()
                }
            }
        }



        //6.黄鸭子落下 星星放大显示出来
        //7.
    }

    private fun duckFly() {
        val tans = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            -1.5f
        )
        val rotateAnimation = RotateAnimation(0f,-15f)
        rotateAnimation.apply {
            duration = 800
            fillAfter = true

        }
        tans.apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
            fillAfter = true
            setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

            })
        }
        val  set =AnimationSet(false)
        set.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    duckFall()
                    starLighting()
                },1000)

            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        set.addAnimation(tans)
        set.addAnimation(rotateAnimation)
        set.fillAfter = true
        duck.startAnimation(set)
    }

    private fun starLighting(){
        val scale = ScaleAnimation(0f,1f,0f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f,)
        scale.apply {
            duration = 800
            setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {
                    l1.visibility = View.VISIBLE
                    l2.visibility = View.VISIBLE
                    l3.visibility = View.VISIBLE
                    l4.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }

        l1.startAnimation(scale)
        l2.startAnimation(scale)
        l3.startAnimation(scale)
        l4.startAnimation(scale)

    }

    private fun duckFall() {
        val tans = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            -1.5f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
        val rotateAnimation = RotateAnimation(-15f,0f)
        rotateAnimation.apply {
            duration = 800
        }
        tans.apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()

            setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

            })
        }
        val  set =AnimationSet(false)
        set.addAnimation(tans)
        set.addAnimation(rotateAnimation)
        duck.startAnimation(set)
    }

    private fun runScore(onEnd:()->Unit) {
        val valueAnimator = ValueAnimator.ofInt(0,89)
        valueAnimator.apply {
            addUpdateListener {
                val i = it.animatedValue as Int
                arcProgress.progress = i
            }
            addListener(
                onEnd = {
                    onEnd.invoke()
                }
            )
            duration= 1000
            start()
        }
    }

    private fun popCats(onEnd:()->Unit) {
        val tans = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            1f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
        tans.apply {
            duration = 400
            interpolator = OvershootInterpolator()
            setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {
                    cat.visibility = View.VISIBLE
                    duck.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                    onEnd.invoke()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }

        cat.startAnimation(tans)
        duck.startAnimation(tans)
    }

    private fun showHeader(onEnd:()->Unit) {
        val alphaAnimation = AlphaAnimation(0f,1f)
        alphaAnimation.apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    onEnd.invoke()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }

        bg1.startAnimation(alphaAnimation)
    }

    private fun pushUpList() {
        val tans = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            1f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
        tans.apply {
            duration = 600
            interpolator = AccelerateDecelerateInterpolator()
        }

        list.startAnimation(tans)
    }
}