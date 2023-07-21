package com.custom.custombasebezier.activity

import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.custom.custombasebezier.R
import com.custom.custombasebezier.customview.SinusoidalWaveView

class MainActivity2 : AppCompatActivity() {
    private var isNight = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, PlaceholderFragment())
                .commit()
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment(), SensorEventListener, View.OnClickListener {
        private var bezierCurveChart: SinusoidalWaveView? = null
        private val btn: Button? = null
        private var img1: ImageView? = null
        private var img2: ImageView? = null
        private var img3: ImageView? = null
        private var img4: ImageView? = null
        private var img5: View? = null
        private var mSensorManager: SensorManager? = null
        private val values = FloatArray(3) //包含 x,y,z的偏移量
        private val values2v = DoubleArray(5)
        private var xValueIndex = 0
        private val Sensororientation = FloatArray(9) //旋转矩阵
        private var mMagneticValues: FloatArray? = null
        private var mAccelerateValues: FloatArray? = null
        private var b1: ImageView? = null
        private var b2: ImageView? = null
        private var b3: ImageView? = null
        private var b4: ImageView? = null
        private var sensorlayout: View? = null
        private var day: View? = null
        private var night: View? = null
        private var root: View? = null
        private var night_bg: View? = null
        private var day_bg: View? = null
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            mSensorManager = context!!.getSystemService(SENSOR_SERVICE) as SensorManager
            val accelerateSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            mSensorManager!!.registerListener(
                this, accelerateSensor, SensorManager.SENSOR_DELAY_GAME
            )
            // 地磁场传感器
            val magneticSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            mSensorManager!!.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME)
            val sinusoidalWaveView =
                rootView.findViewById<SinusoidalWaveView>(R.id.bezier_curve_chart)

            return rootView
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            bezierCurveChart = view.findViewById(R.id.bezier_curve_chart)
            img1 = view.findViewById(R.id.img1)
            img2 = view.findViewById(R.id.img2)
            img3 = view.findViewById(R.id.img3)
            img4 = view.findViewById(R.id.img4)
            img5 = view.findViewById(R.id.img5)
            b1 = view.findViewById(R.id.b1)
            b2 = view.findViewById(R.id.b2)
            b3 = view.findViewById(R.id.b3)
            b4 = view.findViewById(R.id.b4)
            day = view.findViewById(R.id.day)
            night = view.findViewById(R.id.night)
            day_bg = view.findViewById(R.id.day_bg)
            night_bg = view.findViewById(R.id.night_bg)
            root = view.findViewById(R.id.root)
            sensorlayout = view.findViewById(R.id.sensorlayout)
            b1?.setOnClickListener(this)
            b2?.setOnClickListener(this)
            b3?.setOnClickListener(this)
            b4?.setOnClickListener(this)
            img1?.setCameraDistance(500f)
            img2?.setCameraDistance(500f)
            img3?.setCameraDistance(600f)
            img4?.setCameraDistance(400f)
            img5?.setCameraDistance(200f)
        }


        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                mAccelerateValues = event.values
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mMagneticValues = event.values
            }
            if (mMagneticValues != null && mAccelerateValues != null) SensorManager.getRotationMatrix(
                Sensororientation, null, mAccelerateValues, mMagneticValues
            )
            SensorManager.getOrientation(Sensororientation, values)
            // y轴的偏转角度
            val degreeY = Math.toDegrees(values[2].toDouble()).toFloat().toDouble()
            if (xValueIndex < values2v.size) {
                values2v[xValueIndex] = degreeY
                xValueIndex++
            } else {
                smoothMotion()
            }
        }

        private fun smoothMotion() {
            xValueIndex = 0
            var sum = 0.0
            for (i in values2v.indices) {
                sum += values2v[i]
            }
            val v = sum / values2v.size
            img1!!.rotationY = (v / 200f).toFloat()
            img2!!.rotationY = (v / 200f).toFloat()
            img3!!.rotationY = (v / 200f).toFloat()
            img4!!.rotationY = (v / 200f).toFloat()
            img5!!.rotationY = (v / 200f).toFloat()
            sensorlayout!!.rotationY = (v / 50f).toFloat()
        }

        override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
        override fun onClick(view: View) {
            val rect = Rect()
            val locaton = IntArray(2)
            view.getLocationInWindow(locaton)
            bezierCurveChart!!.setnewHoleWindowLocation(locaton, view.width, view.height)
            if (view == b1 || view == b3) {
                changeNight()
            } else {
                changeDay()
            }
        }

        fun changeNight() {
            val anima = TranslateAnimation(0f, 0f, 0f, -100f)
            val animalpa = AlphaAnimation(1f, 0f)
            animalpa.duration = 1000
            anima.duration = 1000
            val animationSet = AnimationSet(true)
            animationSet.addAnimation(anima)
            animationSet.addAnimation(animalpa)
            animationSet.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    day?.visibility = View.GONE
                    night?.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

            })

            day?.startAnimation(animationSet)
            val animalpa3 = AlphaAnimation(1f, 0f)
            animalpa3.duration = 1000
            animalpa3.fillAfter = true
            day_bg?.startAnimation(animalpa3)
        }

        fun changeDay() {
            day?.visibility = View.VISIBLE
            val anima = TranslateAnimation(0f, 0f, -100f, -0F)
            val animalpa = AlphaAnimation(0f, 1f)
            animalpa.duration = 1000
            anima.duration = 1000
            val animationSet = AnimationSet(true)
            animationSet.addAnimation(anima)
            animationSet.addAnimation(animalpa)
            animationSet.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    day?.clearAnimation()
                    day?.visibility = View.VISIBLE
                    night?.visibility = View.GONE
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

            })

            day?.startAnimation(animationSet)

            val animalpa2 = AlphaAnimation(0f, 1f)
            animalpa2.duration = 1000
            animalpa2.fillAfter = true
            day_bg?.startAnimation(animalpa2)
        }
    }


}