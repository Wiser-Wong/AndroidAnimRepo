/*
 * Copyright © Marc Auberer 2023. All rights reserved
 */

package com.custom.custombasebezier.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

public class SinusoidalWaveView extends View {

    private int width;
    private int height;

    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path linePath;

    private double amplitude;

    //角频率， 控制周期
    private double angularFrequency = 8 * 1.0f / 4;
    //初次相位角,
    private double phaseAngle = 0 * Math.PI / 180 + Math.PI / 2 * -1;
    private RectF hole = new RectF();
    private RectF newHole;
    private int radius = 80;
    private boolean isTrans = false;
    private float transRadio;
    float[] left = new float[2];
    float[] right = new float[2];

    Path path1 = new Path();
    //
    Path path2 = new Path();
    PointF holeCenter = new PointF(0f, 0f);
    ValueAnimator animatorMain;

    public SinusoidalWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        linePaint.setColor(Color.parseColor("#ffffff"));
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(2);
        linePaint1.setColor(Color.parseColor("#ff00ff"));
        linePaint1.setStyle(Paint.Style.STROKE);
        linePaint1.setStrokeWidth(2);
        linePath = new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.width = w;
        this.height = h / 8;

        amplitude = height / 2;
        if (animatorMain != null) {
            animatorMain.cancel();
        }
        animatorMain = ValueAnimator.ofInt(0, 1080);
        animatorMain.setDuration(4000);
        animatorMain.setInterpolator(new LinearInterpolator());
        animatorMain.setRepeatCount(ValueAnimator.INFINITE);
        animatorMain.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setPhaseAngle((Integer) animation.getAnimatedValue());
            }
        });
        animatorMain.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(widthMeasureSpec / 2, MeasureSpec.EXACTLY));
    }

    public void setnewHoleWindowLocation(int[] location,int width ,int height){
        int[] myLocation = new int[2];
        getLocationInWindow(myLocation);
        int x = location[0] - myLocation[0];
        int y = location[1] - myLocation[1];
        setNewHole(new RectF(x-30,y-30,x+width+30 ,y+height+30));
    }
    public void setNewHole(RectF hole) {
        newHole = hole;
        beginTransHole();
    }

    private void beginTransHole() {
        if (isTrans) return;
        isTrans = true;
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(800);
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                transRadio = (float) animation.getAnimatedValue();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hole = newHole;
                isTrans = false;
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        linePath.reset();
        path1.reset();
        path2.reset();
        if (isTrans){
            drawTrans(canvas);
        } else {
            drawCommon(canvas);
        }

        path1.op(path2, Path.Op.UNION);
        linePath.op(path1, Path.Op.DIFFERENCE);
        canvas.drawPath(linePath, linePaint);
//        canvas.drawPath(path1, linePaint1);
    }

    private void drawTrans(Canvas canvas) {
        holeCenter.x = ((newHole.left + newHole.right) / 2 - (hole.left + hole.right) / 2) * transRadio + (hole.left + hole.right) / 2;
        holeCenter.y = hole.top + (newHole.top - hole.top) * transRadio;
        float holeWidth = hole.right - hole.left;
        float totalYoffSet = holeCenter.y;
        for (int i = 0; i < width; i++) {
            double angle = i * 1F / width * 2 * Math.PI;
            double y = amplitude * Math.sin(angle * angularFrequency + phaseAngle);
            float offsety;
            if (holeCenter.x > i) {
                offsety = (i) / holeCenter.x * totalYoffSet;
            } else {
                offsety = (width - i) / (width - holeCenter.x) * totalYoffSet;
            }

            linePath.lineTo(i, (float) (y + height / 2) + offsety);

            if ((int) i == (int) (holeCenter.x - holeWidth)) {
                left[0] = (int) (holeCenter.x - holeWidth);
                left[1] = ((float) (y + height / 2) + offsety);
            }
            if (i == (int) (holeCenter.x + holeWidth)) {
                right[0] = (int) (holeCenter.x + holeWidth);
                right[1] = ((float) (y + height / 2) + offsety);
            }


        }
        linePath.lineTo(width, height + 500);
        linePath.lineTo(0, height + 500);
        linePath.close();


        float holeCenterLeft = holeCenter.x - Math.abs((holeCenter.x - ((newHole.left - hole.left) * transRadio + hole.left)) * (transRadio - 0.5f));
        float holeCenterRight = holeCenter.x + Math.abs((holeCenter.x - ((newHole.right - hole.right) * transRadio + hole.right)) * (transRadio - 0.5f));
        if (transRadio < 0.5f && hole != null) {
            float width1 = hole.width() / 2;
            float height1 = hole.height() / 2;
            if (transRadio < 0.3f) {
                path2.addRoundRect(hole.centerX() - width1 * (1 - transRadio * 2),
                        hole.centerY() - height1 * (1 - transRadio * 2),
                        hole.centerX() + width1 * (1 - transRadio * 2),
                        hole.centerY() + height1 * (1 - transRadio * 2), new float[]{radius, radius, radius, radius, radius, radius, radius, radius}, Path.Direction.CW);
            }


            float thisRadio = transRadio / 2f;
            path1.moveTo(holeCenterLeft,
                    totalYoffSet + height1 * (1 - transRadio * 2));
            path1.quadTo(holeCenter.x - width1 * (1 - thisRadio * 4), left[1], left[0], left[1]);
            path1.lineTo(left[0], -100);
            path1.lineTo(right[0], -100);
            path1.lineTo(right[0], right[1]);
            path1.quadTo(holeCenter.x + width1 * (1 - thisRadio * 4),
                    right[1],
                    holeCenterRight,
                    totalYoffSet + height1 * (1 - transRadio * 2));
            if (transRadio < 0.1f) {
                float b = (hole.right + hole.left) / 2;
                path1.quadTo(b,
                        hole.bottom,
                        holeCenterLeft,
                        totalYoffSet + height1 * (1 - transRadio * 2)
                );
            }

        } else {
            float width1 = newHole.width() / 2;
            float height1 = newHole.height() / 2;
            if (transRadio > 0.7f) {
                path2.addRoundRect(newHole.centerX() - width1 * ((transRadio - 0.5f) * 2),
                        newHole.centerY() - height1 * ((transRadio - 0.5f) * 2),
                        newHole.centerX() + width1 * ((transRadio - 0.5f) * 2),
                        newHole.centerY() + height1 * ((transRadio - 0.5f) * 2), new float[]{radius, radius, radius, radius, radius, radius, radius, radius}, Path.Direction.CW);
            }
            if (transRadio > 0.5f) {

                float thisRadio = (transRadio - 0.5f) / 2f;
                path1.moveTo(holeCenterLeft,
                        totalYoffSet + height1 * ((transRadio - 0.5f) * 2));
                path1.quadTo(holeCenter.x - width1 * (1 - thisRadio * 4), left[1], left[0], left[1]);
                path1.lineTo(left[0], -100);
                path1.lineTo(right[0], -100);
                path1.lineTo(right[0], right[1]);
                path1.quadTo(holeCenter.x + width1 * (1 - thisRadio * 4),
                        right[1],
                        holeCenterRight,
                        totalYoffSet + height1 * ((transRadio - 0.5f) * 2));
                if (transRadio > 0.9f) {
                    float b = (newHole.right + newHole.left) / 2;
                    path1.quadTo(b,
                            newHole.bottom,
                            holeCenterLeft,
                            totalYoffSet + height1 * ((transRadio - 0.5f) * 2)
                    );
                }

            }
        }

        path1.close();

    }

    private void drawCommon(Canvas canvas) {

        holeCenter.x = hole != null? ((hole.left + hole.right) / 2):0;
        holeCenter.y = hole != null? hole.top:0;
        float holeWidth = hole != null? hole.right - hole.left:0;

        float totalYoffSet = holeCenter.y;
        for (int i = 0; i < width; i++) {
            double angle = i * 1F / width * 2 * Math.PI;
            double y = amplitude * Math.sin(angle * angularFrequency + phaseAngle);
            float offsety = 0;
            if (holeCenter.x > i) {
                offsety = (i) / holeCenter.x * totalYoffSet;
            } else {
                offsety = (width - i) / (width - holeCenter.x) * totalYoffSet;
            }

            linePath.lineTo(i, (float) (y + height / 2) + offsety);
            if (hole != null){
                if ((int) i == (int)(hole.left - holeWidth / 2)) {
                    left[0] = (hole.left - holeWidth / 2);
                    left[1] = ((float) (y + height / 2) + offsety);
                }
                if (i == (int)(hole.right + holeWidth / 2)) {
                    right[0] = (hole.right + holeWidth / 2);
                    right[1] = ((float) (y + height / 2) + offsety);
                }
            }



        }
        linePath.lineTo(width, height + 500);
        linePath.lineTo(0, height + 500);
        linePath.close();

        if (hole == null)return;
        path1.moveTo(hole.left, hole.top + radius);
        path1.quadTo(hole.left, left[1], left[0], left[1]);
        path1.lineTo(left[0], -100);
        path1.lineTo(right[0], -100);
        path1.lineTo(right[0], right[1]);
        path1.quadTo(hole.right, right[1], hole.right, hole.top + radius);
        path1.cubicTo(hole.right, hole.bottom, hole.left, hole.bottom, hole.left, hole.top + radius);

        float width1 = hole.width() / 2;
        float height1 = hole.height() / 2;
        path2.addRoundRect(hole.centerX() - width1,
                hole.centerY() - height1,
                hole.centerX() + width1,
                hole.centerY() + height1, new float[]{radius, radius, radius, radius, radius, radius, radius, radius}, Path.Direction.CW);


    }

    public void setAmplitude(int progress) {
        amplitude = progress * 1.0F / 100 * height / 2;
        invalidate();
    }

    public void setAngularFrequency(int progress) {
        angularFrequency = progress * 1.0F / 4;
        invalidate();
    }

    public void setPhaseAngle(int progress) {
        phaseAngle = progress * Math.PI / 180 + Math.PI / 2 * -1;
        invalidate();
    }
}
//————————————————
//版权声明：本文为CSDN博主「王先生技术栈」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/wanggang514260663/article/details/85114900