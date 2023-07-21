package com.example.secondfloor

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    private val model: MianVm by viewModels { ViewModelProvider.AndroidViewModelFactory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val motionLayout = findViewById<MotionLayout>(R.id.constraintLayout)
        val image1 = findViewById<ImageView>(R.id.image1)
        val scrollView = findViewById<NestedScrollView>(R.id.scrollView)
        val winheight: Int
        winheight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display!!.height
        } else {
            Resources.getSystem().displayMetrics.heightPixels
        }


        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.e("222",scrollY.toString())
            val i = image1.height - winheight
            val start = scrollY - i
            if (start >= 0){
                val progress = start.toFloat()/winheight
//                if (progress >= 0f && progress <=1f){
                    motionLayout.progress = progress
//                }
            }
        }


    }
}