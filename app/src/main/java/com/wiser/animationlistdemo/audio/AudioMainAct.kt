package com.wiser.animationlistdemo.audio

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.databinding.ActivityAudioBinding
import com.wiser.animationlistdemo.home.CardDetail2
import com.wiser.animationlistdemo.home.CardDetail3
import com.wiser.animationlistdemo.home.CardDetail4
import com.wiser.animationlistdemo.home.DataSource
import com.wiser.animationlistdemo.list.ani.adaper.TransAniStyle1
import com.wiser.animationlistdemo.list.ani.adaper.TransAniStyle3
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * @date: 2023/4/3 18:23
 * @author: jiaruihua
 * @desc :
 *
 */
class AudioMainAct : AppCompatActivity() {

    val binding by lazy { ActivityAudioBinding.inflate(layoutInflater) }

    val screenHeight by lazy { resources.displayMetrics.heightPixels.toFloat() }
    val screenWith by lazy { resources.displayMetrics.widthPixels.toFloat() }

    var isPlay = false

    private val hAdapter = HAudioAdaper(onItemClick = {
//        toggleAudio()
    }) { v2, _, _, position ->

        when(position){

            1->{
                val cop = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@AudioMainAct,
                    Pair(v2, "player_cover")
                )
                val data = DataSource.getAudioResource().get(position)
                startActivity(Intent(this@AudioMainAct, FullScreenAudio::class.java).apply {
                    putExtra("src", data.src)
                }, cop.toBundle())
            }
            0->{toggleAudio()}
            else->{
                val cop = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@AudioMainAct,
                    Pair(v2, "player_cover")
                )
                val data = DataSource.getAudioResource().get(position)
                startActivity(Intent(this@AudioMainAct, NormalAudio::class.java).apply {
                    putExtra("src", data.src)
                }, cop.toBundle())
            }
        }

    }.apply {
        adapterAnimation = TransAniStyle3()
        animationEnable = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(binding.root)
        binding.tvSubTitle.isSelected = true
        hideHomePage(false)


        binding.moon.skyStar
        val resoureces = DataSource.getImageResource()

        resoureces.get(0).let {
            binding.iv1.setImageResource(it.src)
        }

        resoureces.get(1).let {
            binding.iv2.setImageResource(it.src)
        }
        resoureces.get(2).let {
            binding.iv3.setImageResource(it.src)
        }
        resoureces.get(3).let {
            binding.iv4.setImageResource(it.src)
        }
        binding.iv1.setOnClickListener {

            skip(binding.iv1,0)


        }
        binding.iv2.setOnClickListener {      skip(binding.iv2,1) }
        binding.iv3.setOnClickListener {       skip(binding.iv3,2)}
        binding.iv4.setOnClickListener {       skip(binding.iv4,3)}

        binding.miniContainer.icon3.setOnClickListener { toggleAudio() }
        binding.miniContainer.icon4.setOnClickListener { toggleAudio() }

        binding.miniContainer.root.setOnClickListener { miniplayer ->
            dismissMiniPlay(resources, miniplayer)
        }
        binding.audioContainer.leftIcon.setOnClickListener { hideAudioDetail() }
        binding.audioContainer.rightIcon.setOnClickListener {

            clickImge(it)
        }
        binding.audioContainer.root.setOnClickListener {
            toggleMenu()
        }
//        binding.audioMenu.lv1.setOnClickListener {
//            clickImge(it)
//        }
//        binding.audioMenu.lv2.setOnClickListener { clickImge(it) }
//        binding.audioMenu.lv3.setOnClickListener { clickImge(it) }
//        binding.audioMenu.lv4.setOnClickListener { clickImge(it) }
//        binding.audioMenu.lv5.setOnClickListener { clickImge(it) }
        binding.container.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // 计算滑动因子
            val scrollFactor = scrollY.toFloat() / binding.container.height

            // 计算从透明到红色的颜色值
            val colorFrom = Color.TRANSPARENT
            val colorTo = Color.parseColor("#74798e")
            val color = ArgbEvaluator().evaluate(scrollFactor, colorFrom, colorTo) as Int

            // 设置NestedScrollView的背景颜色
            binding.topIv.setBackgroundColor(color)

        }

        binding.rcAudioList.run {
            adapter = hAdapter

            layoutManager =
                LinearLayoutManager(this@AudioMainAct, LinearLayoutManager.HORIZONTAL, false)


            DataSource.getAudioResource().onEachIndexed { index, imageData ->
                lifecycleScope.launch {
//                    if (index in 1..1) {
//                        delay(500)
//                    }
                    hAdapter.addData(imageData)
                }

            }

        }


    }

    private fun skip(v:View,position:Int) {
        var transName = "cover"


        val skipClass = when (position % 3) {
            0 -> {
                transName = "cover3"
                CardDetail4::class.java
            }

            1 -> {
                transName = "cover3"
                CardDetail3::class.java
            }

            else -> CardDetail2::class.java
        }

        v.transitionName = transName

        val cop = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@AudioMainAct,
            Pair(v, v.transitionName)
        )


        ////        rotationCenter(v,v.rotation,v.rotation+15,stiffness=15f)
        startActivity(Intent(this@AudioMainAct, skipClass).apply {
            putExtra("cover", DataSource.getImageResource().get(position).src)
            putExtra("bgColor", DataSource.getImageResource().get(position).bgColor)
            putExtra("rotation", v.rotation)
        }, cop.toBundle())
    }

    private fun clickImge(it: View?) {
        val objX = ObjectAnimator.ofFloat(it, "scaleX", 1f, 0.7f, 1.1f, 1f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 300
        }
        val objY = ObjectAnimator.ofFloat(it, "scaleY", 1f, 0.7f, 1.1f, 1f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 300
        }

        AnimatorSet().apply {
            playTogether(objX, objY)
            addListener(onEnd = {
                if (binding.moon.root.visibility != View.VISIBLE) {
                    showMoonPage()

                }else{
                    hideMoon()
                }

            })
        }.start()


    }

    var isMoon = false
    private fun showMoonPage() {
        binding.moon.root.visibility = View.VISIBLE

        isMoon = true

        ObjectAnimator.ofFloat(binding.audioContainer.moonbg,"alpha",0f,1f).apply {
            duration = 800
            doOnStart {
                binding.audioContainer.moonbg.visibility = View.VISIBLE
            }
            start()
        }

        showView(binding.moon.root, duration = 800)

        val it = binding.moon.root



        val animX = SpringAnimation(it, SpringAnimation.SCALE_X, 1f).apply {
            setStartValue(1.3f)
        }
        animX.spring = SpringForce(1f).apply {
            dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
            stiffness = SpringForce.STIFFNESS_VERY_LOW

        }
        animX.start()

        val animY = SpringAnimation(it, SpringAnimation.SCALE_Y, 1f).apply {
            setStartValue(1.3f)
        }
        animY.spring = SpringForce(1f).apply {
            dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
            stiffness = SpringForce.STIFFNESS_VERY_LOW
        }
        animY.start()

        val st = 10f
        transX(binding.moon.my1, -dp(R.dimen.ksl_dp_70), 0f, stiffness = st)
        transX(binding.moon.my2, screenWith + dp(R.dimen.ksl_dp_170), 0f, stiffness = st)

        binding.moon.root.setOnClickListener {
            hideMoon()
        }
    }

    private fun hideMoonPage() {


        isMoon = false

        ObjectAnimator.ofFloat(binding.audioContainer.moonbg,"alpha",1f,0f).apply {
            duration = 800
            doOnEnd {
                binding.audioContainer.moonbg.visibility = View.GONE
            }

            start()
        }

        hideView(binding.moon.root, duration = 800){
            binding.moon.root.visibility = View.GONE
        }

        val it = binding.moon.root



//        val animX = SpringAnimation(it, SpringAnimation.SCALE_X, 1.3f).apply {
//            setStartValue(1f)
//        }
//        animX.spring = SpringForce(1f).apply {
//            dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
//            stiffness = SpringForce.STIFFNESS_VERY_LOW
//
//        }
//        animX.start()
//
//        val animY = SpringAnimation(it, SpringAnimation.SCALE_Y, 1.3f).apply {
//            setStartValue(1f)
//        }
//        animY.spring = SpringForce(-100f).apply {
//            dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
//            stiffness = SpringForce.STIFFNESS_VERY_LOW
//        }
//        animY.start()

        val st = 10f
        transX(binding.moon.my1, 0f,-dp(R.dimen.ksl_dp_70),  stiffness = st)
        transX(binding.moon.my2, 0f,screenWith + dp(R.dimen.ksl_dp_170),  stiffness = st)

//        binding.moon.root.setOnClickListener {
//            hideMoon()
//        }
    }

    private fun hideMoon() {
//        hideView(binding.audioContainer.moonbg){
//            binding.audioContainer.moonbg.visibility = View.GONE
//        }
//        hideView(binding.moon.root) {
//
//            binding.moon.root.visibility = View.GONE
//        }
//        isMoon = false
        hideMoonPage()
        hideMenu()


    }

    override fun onBackPressed() {
        if (binding.moon.root.visibility == View.VISIBLE) {
            hideMoon()
        } else if (
            binding.audioContainer.audioDetail.visibility == View.VISIBLE
        ) {
            hideAudioDetail()
        } else {
            super.onBackPressed()
        }

    }


    private fun toggleMenu() {
        binding.audioMenu.menu.post {
            if (!isMenuOpen) {
                showMenu()
            } else {
                hideMenu()
            }
        }
    }

    var isMenuOpen = false


    private fun showMenu() {
//        val dy = screenHeight - dp(R.dimen.ksl_dp_380)
//        isMenuOpen = true
//        binding.audioMenu.root.visibility = View.VISIBLE
//        showView(binding.audioMenu.root)
//        trans(binding.audioMenu.root, screenHeight, dy)
    }

    private fun hideMenu() {
        isMenuOpen = false
        val dy = screenHeight - dp(R.dimen.ksl_dp_380)
        hideView(binding.audioMenu.root)
        trans(binding.audioMenu.root, dy, screenHeight) {
            binding.audioMenu.root.visibility = View.GONE
        }
    }

    fun dismissMiniPlay(resources: Resources, miniplayer: View) {
        val bottom = resources.displayMetrics.heightPixels.toFloat()
        val miniHeight = resources.getDimension(R.dimen.ksl_dp_170)
        val dy = bottom - miniHeight

        val objalpha = ObjectAnimator.ofFloat(miniplayer, "alpha", 1f, 0.2f).apply {
            addListener(
                onEnd = {
                    miniplayer.visibility = View.GONE
                }, onStart = {

                    startAudioDetail()

                }
            )
        }
        val objv = AnimatorSet()
        objv.duration = 500
        objv.playTogether(objalpha)
        objv.start()

        val anim = SpringAnimation(miniplayer, DynamicAnimation.TRANSLATION_Y)
        anim.setStartValue(dy)
        anim.animateToFinalPosition(miniHeight)
        anim.spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        anim.spring.stiffness = 300f
        anim.start()

        anim.addEndListener { _, _, _, _ ->

        }
    }

    private fun startAudioDetail() {
        hideHomePage()
        showAudioDetail()
        startTopBg()
        startMountain()
        startCloud()
        startSun()
        startStar()
        startBottomMenu()
        startTopMenu()

    }

    fun hideAudioDetail() {
        hideHomePage(false)
        startTopBg(false)
        startMountain(false)
        startCloud(false)
        startSun(false)
        startStar(false)
        startBottomMenu(false)
        startTopMenu(false)
        showAudioDetail(false)
        hideMenu()

    }

    /**
     * 云朵
     */

    private fun startCloud(show: Boolean = true) {
        if (!show) {

            trans(binding.audioContainer.yun1, 0f, screenHeight)
            trans(binding.audioContainer.yun2, 0f, screenHeight)
            trans(binding.audioContainer.yun3, 0f, screenHeight)
            return
        }
        trans(binding.audioContainer.yun1, screenHeight, 0f)
        trans(binding.audioContainer.yun2, screenHeight, 0f)
        trans(binding.audioContainer.yun3, screenHeight, 0f)
    }

    /**
     * 顶部menu
     */
    private fun startTopMenu(show: Boolean = true) {
        val stiffness = 55f

        if (!show) {
            transX(
                binding.audioContainer.leftIcon,
                0f,
                -dp(R.dimen.ksl_dp_170),
                stiffness = stiffness
            )
            transX(
                binding.audioContainer.rightIcon,
                0f,
                screenWith + dp(R.dimen.ksl_dp_170),
                stiffness = stiffness
            )
            return
        }
        transX(binding.audioContainer.leftIcon, -dp(R.dimen.ksl_dp_170), 0f, stiffness = stiffness)
        transX(
            binding.audioContainer.rightIcon,
            screenWith + dp(R.dimen.ksl_dp_170),
            0f,
            stiffness = stiffness
        )
    }

    /**
     * 底部菜单栏弹起
     */
    private fun startBottomMenu(show: Boolean = true) {

        if (!show) {

            val stiffness = 55f
            trans(binding.audioContainer.aav1, 0f, screenHeight, stiffness = stiffness, delay = 200)
            trans(binding.audioContainer.aav2, 0f, screenHeight, stiffness = stiffness, delay = 200)
            trans(
                binding.audioContainer.ivPlayIc,
                0f,
                screenHeight,
                stiffness = stiffness,
                delay = 150
            )
            trans(
                binding.audioContainer.seekL,
                0f,
                screenHeight,
                stiffness = stiffness,
                delay = 100
            )
            trans(
                binding.audioContainer.seekR,
                0f,
                screenHeight,
                stiffness = stiffness,
                delay = 100
            )
            trans(
                binding.audioContainer.seekBar,
                0f,
                screenHeight,
                stiffness = stiffness,
                delay = 50
            )
            return
        }

        /**
         * 标题，菜单，进度 有不同的延时
         */

        binding.audioContainer.aav1.translationY = screenHeight
        binding.audioContainer.aav2.translationY = screenHeight
        binding.audioContainer.ivPlayIc.translationY = screenHeight
        binding.audioContainer.seekL.translationY = screenHeight
        binding.audioContainer.seekR.translationY = screenHeight
        binding.audioContainer.seekBar.translationY = screenHeight

        val stiffness = 55f
        trans(binding.audioContainer.aav1, screenHeight, 0f, stiffness = stiffness, delay = 50)
        trans(binding.audioContainer.aav2, screenHeight, 0f, stiffness = stiffness, delay = 50)
        trans(binding.audioContainer.ivPlayIc, screenHeight, 0f, stiffness = stiffness, delay = 100)
        trans(binding.audioContainer.seekL, screenHeight, 0f, stiffness = stiffness, delay = 150)
        trans(binding.audioContainer.seekR, screenHeight, 0f, stiffness = stiffness, delay = 150)
        trans(binding.audioContainer.seekBar, screenHeight, 0f, stiffness = stiffness, delay = 200)

    }

    /**
     * 星星掉落
     */
    private fun startStar(show: Boolean = true) {
        val stiffness = 85f
        if (!show) {
            trans(
                binding.audioContainer.iv1,
                0f,
                -screenHeight,
                stiffness = stiffness,
                delay = Random.nextLong(300)
            )
            trans(
                binding.audioContainer.iv2,
                0f,
                -screenHeight,
                stiffness = stiffness,
                delay = Random.nextLong(300)
            )
            trans(
                binding.audioContainer.iv3,
                0f,
                -screenHeight,
                stiffness = stiffness,
                delay = Random.nextLong(300)
            )
            trans(
                binding.audioContainer.iv4,
                0f,
                -screenHeight,
                stiffness = stiffness,
                delay = Random.nextLong(300)
            )
            trans(
                binding.audioContainer.iv5,
                0f,
                -screenHeight,
                stiffness = stiffness,
                delay = Random.nextLong(300)
            )
            trans(
                binding.audioContainer.iv6,
                0f,
                -screenHeight,
                stiffness = stiffness,
                delay = Random.nextLong(300)
            )
            trans(
                binding.audioContainer.iv7,
                0f,
                -screenHeight,
                stiffness = stiffness,
                delay = Random.nextLong(300)
            )
            trans(
                binding.audioContainer.iv8,
                0f,
                -screenHeight,
                stiffness = stiffness,
                delay = Random.nextLong(300)
            )
            return
        }
        trans(
            binding.audioContainer.iv1,
            -screenHeight,
            0f,
            stiffness = stiffness,
            delay = Random.nextLong(300)
        )
        trans(
            binding.audioContainer.iv2,
            -screenHeight,
            0f,
            stiffness = stiffness,
            delay = Random.nextLong(300)
        )
        trans(
            binding.audioContainer.iv3,
            -screenHeight,
            0f,
            stiffness = stiffness,
            delay = Random.nextLong(300)
        )
        trans(
            binding.audioContainer.iv4,
            -screenHeight,
            0f,
            stiffness = stiffness,
            delay = Random.nextLong(300)
        )
        trans(
            binding.audioContainer.iv5,
            -screenHeight,
            0f,
            stiffness = stiffness,
            delay = Random.nextLong(300)
        )
        trans(
            binding.audioContainer.iv6,
            -screenHeight,
            0f,
            stiffness = stiffness,
            delay = Random.nextLong(300)
        )
        trans(
            binding.audioContainer.iv7,
            -screenHeight,
            0f,
            stiffness = stiffness,
            delay = Random.nextLong(300)
        )
        trans(
            binding.audioContainer.iv8,
            -screenHeight,
            0f,
            stiffness = stiffness,
            delay = Random.nextLong(300)
        )

    }

    /**
     * 首页渐变透明
     */
    private fun hideHomePage(show: Boolean = true) {
        val stiffness = 385f
        if (!show) {
            isPlay = true
            toggle()

            showView(binding.container, 1000)
            showView(binding.topIv, 1000)
            showView(binding.tvTitle, 1000)
            showView(binding.tvSubTitle, 1000)
            trans(binding.search, screenHeight, 0f, stiffness = stiffness, delay = 100)
//            trans(binding.ivPlay,screenHeight,0f, stiffness = stiffness,delay = 150)
            trans(binding.tvRecommend, screenHeight, 0f, stiffness = stiffness, delay = 150)
//            trans(binding.rcAudioList,screenHeight,0f, stiffness = stiffness,delay = 200)
//            trans(binding.ivRecommend2,screenHeight,0f, stiffness = stiffness,delay = 200)
            trans(binding.tvCat, screenHeight, 0f, stiffness = stiffness, delay = 250)
            trans(binding.iv1, screenHeight, 0f, stiffness = stiffness, delay = 300)
            trans(binding.iv2, screenHeight, 0f, stiffness = stiffness, delay = 350)
            trans(binding.iv3, screenHeight, 0f, stiffness = stiffness, delay = 400)
            trans(binding.iv4, screenHeight, 0f, stiffness = stiffness, delay = 450)
            trans(binding.topIv, screenHeight, 0f, stiffness = stiffness, delay = 50)
            trans(binding.topIcon, screenHeight, 0f, stiffness = stiffness, delay = 50)
            trans(binding.tvTitle, screenHeight, 0f, stiffness = stiffness, delay = 50)
            trans(binding.tvSubTitle, screenHeight, 0f, stiffness = stiffness, delay = 50)
            binding.nvg.translationY = screenHeight
            trans(binding.nvg, screenHeight, 0f, stiffness = 65f, delay = 0)

            return
        }
        hideView(binding.container, 1000)
        hideView(binding.topIv, 1000)
        hideView(binding.tvTitle, 1000)
        hideView(binding.tvSubTitle, 1000)
    }

    /**
     * 太阳下落
     */
    private fun startSun(show: Boolean = true) {


        binding.audioContainer.ivSun.run {
            post {

//                this.translationY=0f
                val start = this.top.toFloat() - dp(R.dimen.ksl_dp_170)
                val end = this.top.toFloat()


                if (!show) {
                    trans(this, 0f, -200f, stiffness = 20000f, duration = 1000) {
                        this.translationY = 0f
                    }
                    hideView(this, duration = 100)
                    return@post
                }
                showView(this, duration = 200)

                println("Ani----------start=$start ---- end=$end")
                trans(this, -start, 0f, stiffness = 30f, duration = 1000)
            }

        }


    }


    fun dp(resID: Int): Float {
        return resources.getDimension(resID)
    }

    val SHOW_AUDIO_DETAIL_DURATION = 500L

    /**
     * 顶部背景 速度顶上
     */
    private fun startTopBg(show: Boolean = true) {
        if (!show) {
            trans(binding.audioContainer.topBg, 0f, screenHeight)
            return
        }
        trans(binding.audioContainer.topBg, screenHeight, 0f)
    }

    /**
     * 山移动
     */
    private fun startMountain(show: Boolean = true) {
        if (!show) {
            trans(binding.audioContainer.shan, 0f, screenHeight)
            return
        }
        trans(binding.audioContainer.shan, screenHeight, 0f)
    }

    /**
     * 显示audioDetail()
     */
    private fun showAudioDetail(show: Boolean = true) {

        if (!show) {

            binding.audioContainer.audioDetail.alpha = 1f
            binding.audioContainer.audioDetail.run {
                val obj = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
                obj.duration = 500
                obj.doOnEnd {
                    binding.audioContainer.root.visibility = View.GONE
                }
                obj.start()
            }

            return
        }
        binding.audioContainer.root.visibility = View.VISIBLE
        binding.audioContainer.audioDetail.alpha = 0f
        binding.audioContainer.audioDetail.run {
            val obj = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
            obj.duration = 500
            obj.start()
        }
    }


    private fun toggleAudio() {

        if (!isPlay) {
            showMiniPlayer(resources, binding.miniContainer.root)
        } else {
            hideMiniPlayer(resources, binding.miniContainer.root)
        }
        toggle()
    }


    private fun toggle() {
//        binding.ivPlay.run {
//            val obj = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.3f, 1f)
//            val objx = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.75f, 1f)
//            val objy = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.75f, 1f)
//            obj.addUpdateListener {
//
//                val anv = it.animatedValue as Float
//                if (anv < 0.5f) {
//                    binding.ivPlay.setImageResource(if (isPlay) R.drawable.pause else R.drawable.play)
//                }
//            }
//            obj.addListener(onEnd = {
//                isPlay = !isPlay
//            })
        isPlay = !isPlay

//            val objv = AnimatorSet()
//            objv.duration = 350
//            objv.interpolator = AccelerateDecelerateInterpolator()
//            objv.playTogether(obj, objx, objy)
//            objv.start()
//        }
    }
}