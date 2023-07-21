package com.wiser.animationlistdemo.home

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import com.hoko.blur.api.IFrameBuffer
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/6/5 17:30
 * @author: jiaruihua
 * @desc :
 *
 */
class TitleBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    private var mToolbarHeight = 0

    /**
     * scroll View
     */
    var content: NestedScrollView? = null

    var toobar: View? = null
    var cardBg: View? = null

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        content = parent.findViewById(R.id.content)
        cardBg = parent.findViewById(R.id.cardBg)
        toobar = parent.findViewById(R.id.tb)
        val contentTop = content?.top ?: 0
        val contentLeft = content?.left ?: 0
        val childHeight = child.height

        val scrollY = content?.scrollY ?: 0


        var offsetY = contentTop - childHeight / 2
        var offsetX = contentLeft + child.resources.getDimension(R.dimen.ksl_dp_30)

        var tbY = toobar?.y ?: 0f
        var tbb = toobar?.bottom ?: 0

        val cY = child.y
        println("TitleBehavior--tbY=$tbY-----child.Y=${child.y}-child.top=${child.top}---childBottom=${child.bottom}--offsetY=$offsetY---contentTop=$contentTop--contentLeft=$contentLeft--childHeight=$childHeight--offsetX=$offsetX-")
        if (cY > 0 && cY <= tbY) {
            child.y = tbY

        } else {
            child.offsetTopAndBottom(offsetY)
            child.x = offsetX
            if (scrollY != 0) {
                child.y -= 2
            }
        }

        if (cY > tbb) {
            if (child is TextView) {
                child.setTextColor(Color.parseColor("#4a4a4a"))
            }
        }else{
            if (child is TextView) {
                child.setTextColor(Color.parseColor("#ffffff"))
            }
        }

        return dependency.id == R.id.content
    }


    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {


        return super.onDependentViewChanged(parent, child, dependency)
    }

}