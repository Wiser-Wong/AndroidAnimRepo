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
 * @description：三阶贝塞尔曲线实现
 * @Author MRyan
 * @Date 2020/3/4 10:56
 * @Version 1.0
 */
public class BaseBezier3 extends View {
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
    private int eventLeftX;
    private int eventLeftY;
    private int eventRightX;
    private int eventRightY;
    private Paint mCustomLinePaint;
    private boolean isMoveLeft = true;
    private boolean isClean = false;


    public BaseBezier3(Context context) {
        super(context);
    }

    public BaseBezier3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initPaint();
    }

    public BaseBezier3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        eventLeftX = weight / 2 / 2;
        eventLeftY = height / 2 - 500;
        eventRightX = weight / 4 * 3;
        eventRightY = height / 2 - 500;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Path path = new Path();

        path.moveTo(startX, startY);
        path.cubicTo(eventLeftX, eventLeftY, eventRightX, eventRightY, endX, endY);
        canvas.drawPath(path, mCustonPaint);

        canvas.drawCircle(startX, startY, 25, mCustomCirlePaint);//左固定点圆

        canvas.drawCircle(endX, endY, 25, mCustomCirlePaint);//右固定点圆

        canvas.drawCircle(eventLeftX, eventLeftY, 25, mCustomCirlePaint);//左控制点圆
        canvas.drawCircle(eventRightX, eventRightY, 25, mCustomCirlePaint);//右控制点圆

        canvas.drawLine(startX, startY, eventLeftX, eventLeftY, mCustomLinePaint);//路径连线
        canvas.drawLine(eventRightX, eventRightY, endX, endY, mCustomLinePaint);//路径连线
        canvas.drawLine(eventLeftX, eventLeftY, eventRightX, eventRightY, mCustomLinePaint);//路径连线
        path.reset();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (isMoveLeft) {
                    eventLeftX = (int) event.getX();
                    eventLeftY = (int) event.getY();
                } else {
                    eventRightX = (int) event.getX();
                    eventRightY = (int) event.getY();
                }
                postInvalidate();
                break;
        }
        return true;
    }

    //移动左控制点
    public void moveLeft() {
        isMoveLeft = true;
    }

    //移动右控制点
    public void moveRight() {
        isMoveLeft = false;
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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseBezier3);
        mCustomColor = typedArray.getColor(R.styleable.BaseBezier3_custom_color3, mCustomColor);
        mCustomSize = (int) typedArray.getDimension(R.styleable.BaseBezier3_custom_size3, mCustomSize);
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
    public onIsCleanSubListener onIsCleanSubListener;

    public interface onIsCleanSubListener {
        void onCleanSub(Boolean isclean);
    }

    public void setOnIsCleanSubListener(BaseBezier3.onIsCleanSubListener onIsCleanSubListener) {
        this.onIsCleanSubListener = onIsCleanSubListener;
    }
}
