package com.wiser.animationlistdemo.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/6/5 17:30
 * @author: jiaruihua
 * @desc :
 *
 */
class ToolbarHeaderBehavior3(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs)  {

        private var mOriginHeaderX = 0
    private  var mOriginHeaderY = 0

    private var bgView :View?=null

    var mCover:View?=null
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        bgView = parent.findViewById(R.id.cardBg)
        mCover = parent.findViewById(R.id.coverIv)
        beginX = mCover?.x?:0f
        return dependency.id == R.id.titleLay
    }

    var beginX = 0F


    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {


        if (dependency.y==0f) return false
//
//        if (mOriginHeaderX==0){
//            this.mOriginHeaderX = dependency.width/2-child.width/2
//        }
//        if (mOriginHeaderY==0){
//            this.mOriginHeaderY = 0
//        }
//
//        var mpx = dependency.y/mOriginHeaderX
//        if (mpx>=1){
//            mpx=1f
//        }
//
//        var mpy = dependency.y/mOriginHeaderY
//        if (mpy>=1){
//            mpy=1f
//        }
//
//        var x = mOriginHeaderX-mOriginHeaderX*mpx
//
//        if (x<=child.width){
//            x = child.width.toFloat()
//        }

        val b = mCover?.bottom?:0
        val scale = dependency.y/(b-100)

        println("-----dependency.height=${dependency.height}-1-y=${dependency.y}--t=${dependency.top}-b=${dependency.bottom}-child.h=${mCover?.height}-child.t=${mCover?.top}")



        mCover?.scaleX = 1-scale
        mCover?.scaleY = 1-scale

        val h = mCover?.height?:0


        mCover?.y = -dependency.top.toFloat()+h*scale/2-(50*scale)


        return super.onDependentViewChanged(parent, child, dependency)
    }

}