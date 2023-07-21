package com.custom.custombasebezier.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * @description：TODO
 * @Author MRyan
 * @Date 2020/3/9 23:12
 * @Version 1.0
 */
public class BeatBall extends View {
    private Path mPath;
    private Paint mballPaint;//画笔球
    private Paint mLinePaint;//画笔绳
    private Paint mLineCirclePaint;//画绳两边球
    private int width;
    private int height;
    private float mballControlly;//球运动的控制点y坐标
    private int mballMiny = 300;//球运动的最低点
    private int mballMaxy = -1300;//球运动的最高点
    private float mLineControllx;//绳的贝塞尔曲线控制点x坐标
    private float mLineControlly;//绳的贝塞尔曲线控制点y坐标
    private ValueAnimator downAnimator;
    private ValueAnimator upAnimator;

    public BeatBall(Context context) {
        super(context);
    }

    public BeatBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        startAni();
    }


    public BeatBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        mLineControllx = width / 2;
        mballControlly = mballMaxy;

    }

    private void initPaint() {
        mPath = new Path();
        mballPaint = new Paint();
        mballPaint.setColor(Color.YELLOW);
        mballPaint.setStrokeWidth(15);
        mballPaint.setAntiAlias(true);
        mballPaint.setDither(true);


        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(20);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);


        mLineCirclePaint = new Paint();
        mLineCirclePaint.setColor(Color.YELLOW);
        mLineCirclePaint.setStrokeWidth(15);
        mLineCirclePaint.setAntiAlias(true);
        mLineCirclePaint.setDither(true);
        mLineCirclePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(30, height / 2+500, 25, mLineCirclePaint);//绳左球
        canvas.drawCircle(width - 30, height / 2+500, 25, mLineCirclePaint);//绳右球
        canvas.drawCircle(width / 2, height / 2+500 + mballControlly, 60, mballPaint);//画下落的球
        /*贝塞尔曲线绘制绳*/
        mPath.reset();
        mPath.moveTo(23, height / 2+500);
        mPath.quadTo(mLineControllx, height / 2+500 + mLineControlly, width - 23, height / 2+500);

        canvas.drawPath(mPath, mLinePaint);
    }

    /**
     * 运行动画
     */
    public void startAni() {
        //加速插值器
        downAnimator = ValueAnimator.ofFloat(mballMaxy, mballMiny);
        //球加速下降 当球下降接触到绳时 绳也下降
        downAnimator.setDuration(1500);
        downAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        downAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mballControlly = (float) animation.getAnimatedValue();
                if (height / 2 + mballControlly >= height / 2) {
                    mLineControlly = (float) ((float) (animation.getAnimatedValue()) * 2.5);
                }
                postInvalidate();
            }
        });
        downAnimator.start();
        downAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startUpAnimator();
            }
        });
        //球和绳减速上升 当绳超过反向mballControlly 时球绳分离
        //减速插值器
        upAnimator = ValueAnimator.ofFloat(mballMiny, mballMaxy);//300->-900
        upAnimator.setDuration((long) (1500*0.8));
        upAnimator.setInterpolator(new DecelerateInterpolator());
        upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mballControlly = animatedValue;
                if(mballControlly+height/2>=height/2+mballMaxy*0.4&&mballControlly<150){//小球先上升后绳在上升
                    mLineControlly=animatedValue;
                }/*else if(mballControlly+height/2<height/2+mballMaxy/3) {
                    mLineControlly = -animatedValue / 2;
                }*/
                if(animatedValue<=-898){//执行完毕 绳回到中心点
                    mLineControlly=0;
                }
                postInvalidate();
            }
        });
        upAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startDownAnimator();
            }
        });
    }

    /**
     * 开始反弹
     */
    public void startUpAnimator() {
        if (upAnimator != null && upAnimator.getValues() != null && upAnimator.getValues().length > 0 && !upAnimator.isRunning()) {
            upAnimator.start();
        }
    }

    /**
     * 开始下降
     */
    public void startDownAnimator() {
        if (downAnimator != null && downAnimator.getValues() != null && downAnimator.getValues().length > 0 && !downAnimator.isRunning()) {
            downAnimator.start();
        }
    }
}