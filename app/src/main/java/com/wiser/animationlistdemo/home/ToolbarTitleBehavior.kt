package com.wiser.animationlistdemo.home

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/6/5 17:30
 * @author: jiaruihua
 * @desc :
 *
 */
class ToolbarTitleBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs)  {

        private var mToolbarHeight = 0

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is TextView
    }


    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {



        if (mToolbarHeight==0){
            this.mToolbarHeight = child.bottom*2//慢效果
        }


        var p = dependency.y/mToolbarHeight
        if (p>=1){
            p=1f
        }

       child.alpha = p
        return super.onDependentViewChanged(parent, child, dependency)
    }

}