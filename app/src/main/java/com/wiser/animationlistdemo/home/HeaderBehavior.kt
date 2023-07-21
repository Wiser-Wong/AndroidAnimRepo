package com.wiser.animationlistdemo.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/6/2 17:20
 * @author: jiaruihua
 * @desc :
 *
 */
class HeaderBehavior: CoordinatorLayout.Behavior<View> {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        // child: 当前 Behavior 所关联的 View，此处是 HeaderView
        // dependency: 待判断是否需要监听的其他子 View
        return dependency.id == R.id.content
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {


        if (dependency.translationY>0){
            println("-------------dependency.translationY=${dependency.translationY}-----tag=${dependency.tag}")

            //避免入场动画
            return true
        }

        child.translationY = dependency.translationY * 0.5f
//        child.alpha = 1 + dependency.translationY / (child.height * 0.6f)
        // 如果改变了 child 的大小位置必须返回 true 来刷新
        return true
    }
}