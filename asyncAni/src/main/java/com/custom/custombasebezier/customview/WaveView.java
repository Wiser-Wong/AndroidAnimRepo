package com.custom.custombasebezier.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.custom.custombasebezier.R;

/**
 * @description：自定义View水波纹
 * @Author MRyan
 * @Date 2020/3/9 16:04
 * @Version 1.0
 */
public class WaveView extends View {
    private int mWaveViewColor;
    private Paint mWaveViewPaint;
    private int height;
    private int weight;
    private float offset;
    private ValueAnimator animator;
    private boolean isStart = false;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initPaint();
    }


    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        mWaveViewColor = typedArray.getColor(R.styleable.WaveView_waveview_color, mWaveViewColor);
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mWaveViewPaint = new Paint();
        mWaveViewPaint.setColor(mWaveViewColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        weight = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(weight, height);
    }

    /**
     * 开启动画
     */
    private void start() {
        isStart = true;
        if(onStartListener!=null){
            onStartListener.onIsStart(true);
        }
        animator = ValueAnimator.ofFloat(0, weight);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorValue = (float) animation.getAnimatedValue();
                offset = animatorValue;
                postInvalidate();
            }
        });
        animator.setDuration(1500);
        animator.setRepeatCount(ValueAnimator.INFINITE);//重复执行 无限次数
        animator.start();

    }

    /**
     * 关闭动画
     */
    public void stop() {
        isStart = false;
        if(onStartListener!=null){
            onStartListener.onIsStart(false);
        }
        animator.cancel();
    }

    public void isStart(){
        if(isStart){
            stop();
        }else{
            start();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int itemWidth = weight / 2;
        path.moveTo(-3 * itemWidth, height / 2);
        path.moveTo(-3 * itemWidth, height / 2);
        for (int i = -3; i < 2; i++) {
            int controlX = i * itemWidth;
            path.quadTo(itemWidth / 2 + controlX + offset, getWaveHeight(i), controlX + itemWidth + offset, height / 2);
        }
        path.lineTo(weight, height);
        path.lineTo(0, height);
        path.close();//封闭区间
        canvas.drawPath(path, mWaveViewPaint);//画路径
    }


    //根据给定的i 确定是波峰还是波谷
    private int getWaveHeight(int count) {
        if (count % 2 == 0) {
            return height / 2 - 170;
        }
        return height / 2 + 170;
    }

    public interface onStartListener {
        void onIsStart(boolean isStart);
    }
    public onStartListener onStartListener;

    public void setOnStart(onStartListener onStartListener) {
        this.onStartListener = onStartListener;
    }
}
