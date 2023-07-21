package com.custom.custombasebezier.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.custom.custombasebezier.R;

/**
 * @description：二阶贝塞尔曲线实现
 * @Author MRyan
 * @Date 2020/3/4 10:56
 * @Version 1.0
 */
public class BaseBezier2 extends View {
    private int mCustomSize;
    private int mCustomColor;
    private Paint mCustonPaint;
    private Paint mCustomCirlePaint;
    private int mSize;
    private int weight;
    private int height;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int eventX;
    private int eventY;
    private Paint mCustomLinePaint;
    private Boolean isClean = false;


    public BaseBezier2(Context context) {
        super(context);
    }

    public BaseBezier2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initPaint();
    }

    public BaseBezier2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        weight = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(weight, height);
        startX = weight / 2 - 500;
        startY = height / 2;
        endX = weight / 2 + 500;
        endY = height / 2;
        eventX = weight / 2;
        eventY = height / 2 - 500;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.reset();
        path.moveTo(startX, startY);
        path.quadTo(eventX, eventY, endX, endY);
        canvas.drawPath(path, mCustonPaint);

        canvas.drawCircle(startX, startY, 25, mCustomCirlePaint);//左固定点圆

        canvas.drawCircle(endX, endY, 25, mCustomCirlePaint);//右固定点圆

        canvas.drawCircle(eventX, eventY, 25, mCustomCirlePaint);//控制点圆

        canvas.drawLine(startX, startY, eventX, eventY, mCustomLinePaint);//路径连线
        canvas.drawLine(eventX, eventY, endX, endY, mCustomLinePaint);//路径连线


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                eventX = (int) event.getX();
                eventY = (int) event.getY();
                postInvalidate();
                break;
        }
        return true;
    }

    private void initPaint() {

        mCustonPaint = new Paint();
        mCustonPaint.setColor(mCustomColor);
        mCustonPaint.setAntiAlias(true);
        mCustonPaint.setStrokeWidth(20);
        mCustonPaint.setStyle(Paint.Style.STROKE);

        mCustomCirlePaint = new Paint();
        mCustomCirlePaint.setColor(Color.parseColor("#ffffff"));
        mCustomCirlePaint.setAntiAlias(true);
        mCustomCirlePaint.setStrokeWidth(17);
        mCustomCirlePaint.setStyle(Paint.Style.STROKE);

        mCustomLinePaint = new Paint();
        mCustomLinePaint.setColor(Color.parseColor("#ffffff"));
        mCustomLinePaint.setAntiAlias(true);
        mCustomLinePaint.setStrokeWidth(13);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseBezier2);
        mCustomColor = typedArray.getColor(R.styleable.BaseBezier2_custom_color2, mCustomColor);
        mCustomSize = (int) typedArray.getDimension(R.styleable.BaseBezier2_custom_size2, mCustomSize);
        typedArray.recycle();
    }

    public void cleanSub() {
        if (onIsCleanSubListener != null) {
            onIsCleanSubListener.onCleanSub(isClean);
        }
        isClean = false;
        mCustomLinePaint.setAlpha(255);
        postInvalidate();
    }


    public void Sub() {
        if (onIsCleanSubListener != null) {
            onIsCleanSubListener.onCleanSub(isClean);
        }
        isClean = true;
        mCustomLinePaint.setAlpha(0);
        postInvalidate();
    }


    /**
     * 是否清除辅助线
     */
    public void iscleanSub() {
        if (isClean) {
            cleanSub();
        } else {
            Sub();
        }


    }

    /**
     * 暴露接口
     */
    public onIsCleanSubListener2 onIsCleanSubListener;

    public interface onIsCleanSubListener2 {
        void onCleanSub(Boolean isclean);
    }

    public void setOnIsCleanSubListener(onIsCleanSubListener2 onIsCleanSubListener) {
        this.onIsCleanSubListener = onIsCleanSubListener;
    }

}
