package com.wiser.animationlistdemo.textanim

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bullfrog.particle.Particles
import com.bullfrog.particle.animation.ParticleAnimation
import com.bullfrog.particle.particle.configuration.Shape
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.textanim.text.AnimTextView
import com.wiser.animationlistdemo.textanim.text.AnvilText
import com.wiser.animationlistdemo.textanim.text.BaseText
import com.wiser.animationlistdemo.textanim.text.OnTextAnimatorListener
import kotlinx.android.synthetic.main.activity_text_anim.*

class TextAnimActivity: AppCompatActivity() {

    private var sentences = arrayOf(
        "口袋神探",
        "摩登大自然",
        "小猴玛尼",
        "凯叔红楼梦",
        "麦小米的100个烦恼",
        "长安喵探妙狸花",
        "神奇图书馆"
    )

    private var mCounter = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_anim)

        tv_anim_text?.setTextAnimatorListener(object: OnTextAnimatorListener {
            override fun onStart(textView: BaseText?) {
                if (textView is AnvilText) {
                    Particles.with(this@TextAnimActivity, fl_anim_text) // container 是粒子动画的宿主父 ViewGroup
                        .colorFromDrawable(R.drawable.ic_close)// 从 button 中采样颜色
                        .particleNum(200)// 一共 200 个粒子
                        .anchor(tv_anim_text)// 把 button 作为动画的锚点
                        .shape(Shape.PENTACLE)// 粒子形状是圆形
                        .radius(2, 6)// 粒子随机半径 2~6
                        .anim(ParticleAnimation.FIREWORK)// 使用爆炸动画
                        .start()
                }
            }

            override fun onEnd(textView: BaseText?) {

            }

        })

        tv_anim_text?.setTextAnim("凯叔三国演义")
    }

    fun clickAnvil(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.ANVIL)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickSparkle(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.SPARKLE)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickPixelate(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.PIXELATE)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickFall(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.FALL)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickScale(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.SCALE)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickEvaporate(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.EVAPORATE)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickBurn(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.BURN)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickRainBow(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.RAINBOW)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickPrint(view: View?) {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.PRINT)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }
}