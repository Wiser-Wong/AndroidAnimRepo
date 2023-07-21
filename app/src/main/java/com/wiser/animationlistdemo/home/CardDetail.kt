package com.wiser.animationlistdemo.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.SharedElementCallback
import androidx.core.view.updateLayoutParams
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.transY
import com.wiser.animationlistdemo.databinding.CardDetailBinding

/**
 * @date: 2023/5/11 16:28
 * @author: jiaruihua
 * @desc :
 *
 */
class CardDetail : AppCompatActivity() {
    var binding: CardDetailBinding? = null
    val screenHeight by lazy { resources.displayMetrics.heightPixels.toFloat() }
    val screenWith by lazy { resources.displayMetrics.widthPixels.toFloat() }

    private var mDownX = 0f
    private var mDownY = 0f
    private var mPointerId = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CardDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
        val rotation = intent.getFloatExtra("rotation", 0f)
        binding?.cover?.setImageResource(intent.getIntExtra("cover", R.drawable.a))

//        transANI(window)


        setEnterSharedElementCallback(object :SharedElementCallback(){
            override fun onSharedElementStart(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementStart(
                    sharedElementNames,
                    sharedElements,
                    sharedElementSnapshots
                )

                println("c--------------------------------00")
            }

            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                println("c--------------------------------11")
            }

            override fun onCaptureSharedElementSnapshot(
                sharedElement: View?,
                viewToGlobalMatrix: Matrix?,
                screenBounds: RectF?
            ): Parcelable {
                println("c--------------------------------22")
                return super.onCaptureSharedElementSnapshot(
                    sharedElement,
                    viewToGlobalMatrix,
                    screenBounds
                )


            }

            override fun onCreateSnapshotView(context: Context?, snapshot: Parcelable?): View {
                println("c--------------------------------33")
                return super.onCreateSnapshotView(context, snapshot)
            }

            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                super.onMapSharedElements(names, sharedElements)
                println("c--------------------------------44")
            }

            override fun onSharedElementsArrived(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                listener: OnSharedElementsReadyListener?
            ) {
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener)
                println("c--------------------------------55")

            }

            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
                super.onRejectSharedElements(rejectedSharedElements)
                println("c--------------------------------66")
            }
        })


        binding?.cardBg?.post {
            binding?.cardBg?.let {
                it.setBackgroundColor(intent.getIntExtra("bgColor", Color.parseColor("#cce3c6")))

                val aniReveal = ViewAnimationUtils.createCircularReveal(
                    it,
                    screenWith.toInt(),
                    0,
                    0f,
                    screenHeight
                ).apply {

                    duration = 800
                    interpolator = LinearInterpolator()

                }

                aniReveal.start()

            }
        }
        binding?.back?.setOnClickListener { onBackPressed() }

        binding?.content?.let {
            it.translationY = screenHeight
            transY(it, screenHeight, 0f, stiffness = 45f)

            it.updateLayoutParams {
                height = screenHeight.toInt()
            }

//            it.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//                binding?.cardBg?.translationY = -scrollY.toFloat()
//                binding?.content?.translationY = -scrollY.toFloat()
//
//            }
        }




    }


}