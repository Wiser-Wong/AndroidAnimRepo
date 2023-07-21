package com.wiser.animationlistdemo.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.widget.NestedScrollView
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.transX

/**
 * @date: 2023/6/2 17:20
 * @author: jiaruihua
 * @desc :
 *
 */
class CoverBehavior : CoordinatorLayout.Behavior<View> {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    lateinit var dependencyView: View
    lateinit var titleView:TextView
    var scw: Int = 0
    var center = -1f

    var beginX = -1f
    var beginX2 = -1f
    var childH = -1f
    var childW = -1f

    var lastScale = -1f
    var isUpScroll = false
    var isFirst = false

    var lastScrollScale = lastScale

    var startX = 0F
    var startY = 0F

    /**
     * test 另外一种思路
     */
    var endX = 0F
    var endY = 0F
    var dY = 0F
    var dX = 0F

    var sH = 0f
    var sW = 0f

    var eH = 0F
    var eW = 0F


    var endScale = 0f


    var isAniStart = false

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        scw = parent.width


        startX = child.x
        startY = 0f

        sW= child.width.toFloat()
        sH = child.height.toFloat()


        val cardBgHeight = (parent.findViewById<View>(R.id.cardBg).height * 0.35).toInt()
        titleView = parent.findViewById(R.id.title)
        eH = cardBgHeight * 0.8f
        eW= eH

        endScale = eH/sH



        dependencyView = dependency

        endX = scw/2f-eW/2

        dY = sH-child.resources.getDimension(R.dimen.ksl_dp_15)
        dX = startX-endX

        println("-----sw=$sW--sH=$sH--scw=$scw--eH=$eH--endScale=$endScale---startX=$startX----endX=$endX")

        if (dependencyView is NestedScrollView) {

            dependencyView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->


                if (lastScrollScale == lastScale) {
                    extracted(child, dependency, scrollY)
                }
                if (lastScale != lastScrollScale) {
                    lastScrollScale = lastScale
                }

            }
        }

        // child: 当前 Behavior 所关联的 View，此处是 HeaderView
        // dependency: 待判断是否需要监听的其他子 View
        return dependency.id == R.id.content
    }


    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {


        if (dependency.translationY >= 0) {
            println("-------------dependency.translationY=${dependency.translationY}-----tag=${dependency.tag}")

            //避免入场动画
            return true
        }




        extracted(child, dependency)
//        child.alpha = 1 + dependency.translationY / (child.height * 0.6f)
        // 如果改变了 child 的大小位置必须返回 true 来刷新
        return true
    }

    private fun extracted(child: View, dependency: View, tranY: Int = 0) {
        if (beginX < 0) {
            beginX = child.x
        }

        val ty = dependency.translationY - tranY

        var scale = (1 + (ty / child.height))
//        if (scale<endScale){
//            return
//        }

        if (lastScale == -1f) {
            lastScale = scale
        }
        if (scale - lastScale < 0) {
            //逐渐缩小 ，上滑
            isUpScroll = true
            isFirst = false
        } else if (scale - lastScale > 0) {
            isUpScroll = false
            isFirst = false
        } else {
            isFirst = true
        }

        lastScale = scale


        val alpha = 1-scale

        titleView.alpha = if (alpha<0.15f){
            0f
        } else if(alpha<0.5f) alpha else 1f

        child.translationY = -child.height * (1 - scale)
//        val ddy =  -dY*(1-scale)
//        val dSY = -child.resources.getDimension(R.dimen.ksl_dp_15)
//        println("dddy--------$ddy---dY=$dY---scale=$scale--dSY=$dSY")
////        if (ddy<(sH-eH))
////        child.translationY = ddy
        val tranx = beginX + beginX * (1 - scale)
//        child.translationX = tranx
//        child.scaleX = scale
//        child.scaleY = scale


//
//
        val scroll = if (isUpScroll) "上滑" else "下滑"

        val cZ = child.translationZ
        val pz = dependencyView.translationZ

        println("-------translationY=${dependency.translationY}---scale=$scale--tranx=${tranx}---$scroll--pz=$pz---cz=$cZ-childHeight=${child.height}---childW=${child.width}-childX=${child.x}----")

        if (!isUpScroll && !isFirst) {

        }
        child.translationZ = -1f

        child.translationX = tranx
        beginX2 = child.x
        child.scaleX = scale
        child.scaleY = scale
        childH = child.height * scale
        childW = child.width * scale
    }
}