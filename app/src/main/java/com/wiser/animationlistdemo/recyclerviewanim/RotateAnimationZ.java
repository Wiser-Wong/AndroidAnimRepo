package com.wiser.animationlistdemo.recyclerviewanim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class RotateAnimationZ extends Animation {

    int centerX, centerY;

    Camera camera = new Camera();

    public final int X = 0;

    public final int Y = 1;

    public int direction = X;

    public boolean isZhengfangxiang = true;

    public static int angle = 45;

    @Override

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        //获取中心点坐标
        centerX = width / 2;
        centerY = height / 2;
    }

    @Override

    protected void applyTransformation(float interpolatedTime, Transformation t) {

        super.applyTransformation(interpolatedTime, t);

        System.out.println("=============>>" + interpolatedTime);

        final Matrix matrix = t.getMatrix();

        camera.save();

        //中心是绕Y轴旋转 这里可以自行设置X轴 Y轴 Z轴
        if (direction == X) {
            if (isZhengfangxiang) {
                if (interpolatedTime > 0.5f) {
                    interpolatedTime = 1 - interpolatedTime;
                }
                camera.rotateX(angle * interpolatedTime);
            } else {
//                if (interpolatedTime > 0.5f) {
//                    interpolatedTime = 1 - interpolatedTime;
//                }
                camera.rotateX(angle - angle * interpolatedTime);
            }

        } else {
            if (isZhengfangxiang) {
                camera.rotateY(angle * interpolatedTime);
            } else {
                camera.rotateY(angle - angle * interpolatedTime);
            }
        }

        //把我们的摄像头加在变换矩阵上
        camera.getMatrix(matrix);
        //设置翻转中心点
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        camera.restore();
    }
}