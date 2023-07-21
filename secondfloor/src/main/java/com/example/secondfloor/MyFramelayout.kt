package com.example.secondfloor

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout

class MyFramelayout : FrameLayout {
    private var vm: MianVm? = null
    fun setVm(vm: MianVm?) {
        this.vm = vm
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    var imageView: ImageView? = null
    private fun init() {
        val im = LayoutInflater.from(context).inflate(R.layout.pager, this, true)
        im.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val imageview = im.findViewById<ImageView>(R.id.image)
        imageView = imageview
        //        MotionLayout view = (MotionLayout) (LayoutInflater.from(getContext()).inflate(R.layout.ani, this, false).findViewById(R.id.constraintLayout));
//        addView(view,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        val motionLayout = LayoutInflater.from(context).inflate(R.layout.ani,this,false).findViewById<MotionLayout>(R.id.constraintLayout)
//        addView(motionLayout, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        val winheight: Int
        winheight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display!!.height
        } else {
            Resources.getSystem().displayMetrics.heightPixels
        }
        viewTreeObserver.addOnScrollChangedListener {
            getLocationInWindow(lo)
            val progress = (winheight - lo[1]) / winheight.toFloat()
            if (vm != null) {
                vm!!.data.postValue(progress)
            }
            if (progress < 1.0f && progress >= 0) {
                if (vm != null) {
                    vm!!.data.postValue(progress)
                }
                Log.e("zzz", progress.toString() + "")
            }
        }
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private val lo = IntArray(2)
}