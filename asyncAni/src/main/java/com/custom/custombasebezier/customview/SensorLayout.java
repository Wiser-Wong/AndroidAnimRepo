/*
 * Copyright © Marc Auberer 2023. All rights reserved
 */

package com.custom.custombasebezier.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.custom.custombasebezier.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 传感器监听
* author tangxianfeng
* created  2021.8.15
**/
public class SensorLayout extends FrameLayout implements SensorEventListener {
    private final SensorManager mSensorManager;
    private float[] mAccelerateValues;
    private float[] mMagneticValues;
    private final Scroller mScroller;
    private double mDegreeYMin = -50;//最小偏移度数  Y
    private double mDegreeYMax = 50;//最大偏移度数  Y
    private double mDegreeXMin = -50;//最小偏移度数  X
    private double mDegreeXMax = 50;//最大偏移度数  X
    private static final double MOVE_DISTANCE_X = -100;//X轴移动偏移量 实际偏移为MOVE_DISTANCE_X*acclerateratio
    private static final double MOVE_DISTANCE_Y = 0;//Y轴移动偏移量 实际偏移为MOVE_DISTANCE_Y*acclerateratio
    private float acclerateratio = 1;//偏移加速的倍率 可以通过设置此倍率改变偏移速度
    private final float[] values = new float[3];//包含 x,y,z的偏移量
    private final float[] Sensororientation = new float[9];//旋转矩阵
    private int[] scrollXArray = new int[5];
    private int[] scrollYArray = new int[5];
    private int mCurrIndex = 0;

    public SensorLayout(@NonNull Context context) {
        this(context, null);
    }

    public SensorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SensorLayoutStyle);
            acclerateratio = typedArray.getFloat(R.styleable.SensorLayoutStyle_AccelerateRatio, 1);
        }
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            Sensor accelerateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // 地磁场传感器
            Sensor magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerateValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagneticValues = event.values;
        }

        if (mMagneticValues != null && mAccelerateValues != null)
            SensorManager.getRotationMatrix(Sensororientation, null, mAccelerateValues, mMagneticValues);
        SensorManager.getOrientation(Sensororientation, values);
        // x轴的偏转角度
        double degreeX = (float) Math.toDegrees(values[1]);
        // y轴的偏转角度
        double degreeY = (float) Math.toDegrees(values[2]);
        int scrollX = mScroller.getFinalX();
        int scrollY = mScroller.getFinalY();
        if (degreeY <= 0 && degreeY > mDegreeYMin) {
            scrollX = (int) (degreeY / Math.abs(mDegreeYMin) * MOVE_DISTANCE_X * acclerateratio);
        } else if (degreeY > 0 && degreeY < mDegreeYMax) {
            scrollX = (int) (degreeY / Math.abs(mDegreeYMax) * MOVE_DISTANCE_X * acclerateratio);
        }
        if (degreeX <= 0 && degreeX > mDegreeXMin) {
            scrollY = (int) (degreeX / Math.abs(mDegreeXMin) * MOVE_DISTANCE_Y * acclerateratio);
        } else if (degreeX > 0 && degreeX < mDegreeXMax) {
            scrollY = (int) (degreeX / Math.abs(mDegreeXMax) * MOVE_DISTANCE_Y * acclerateratio);
        }
        smoothScroll(scrollX, scrollY);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //移动
    public void smoothScroll(int destX, int destY) {
        if (mCurrIndex < scrollXArray.length){
            scrollXArray[mCurrIndex] = destX;
            scrollYArray[mCurrIndex] = destY;
            mCurrIndex++;
        }  else {
            mCurrIndex = 0;
            int scrollY = getScrollY();
            int delta = destY - scrollY;
            int sumx =0;
            int sumy= 0;
            for (int i = 0; i < scrollXArray.length; i++) {
                sumx+=scrollXArray[i];
                sumy+= scrollYArray[i];
            }
            int finalx = sumx/scrollXArray.length;
            int finaly = sumy/scrollYArray.length;
            mScroller.startScroll(finalx, finaly, 0, delta, 200);
            invalidate();
        }

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    //解绑监听
    public void unregister() {
        mSensorManager.unregisterListener(this);
    }

    public void setDegree(double degreeYMin,double degreeYMax,double degreeXMin,double degreeXMax) {
        mDegreeYMin = degreeYMin;
        mDegreeYMax=degreeYMax;
        degreeXMax=degreeYMax;
        degreeXMin=degreeXMin;
    }

    public void setAcclerateratio(float acclerateratio) {
        this.acclerateratio = acclerateratio;
    }

    @IntDef({DIRECTION_LEFT, DIRECTION_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface ADirection {

    }

    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = -1;
}
//————————————————
//版权声明：本文为CSDN博主「玖流之辈」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/number_cmd9/article/details/119722039