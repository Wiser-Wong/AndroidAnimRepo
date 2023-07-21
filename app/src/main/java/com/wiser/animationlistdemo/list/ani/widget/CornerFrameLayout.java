package com.wiser.animationlistdemo.list.ani.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @date: 2023/3/14 16:39
 * @author: jiaruihua
 * @desc :
 */

public class CornerFrameLayout extends FrameLayout {
    private final float[] mRadii = new float[8];
    private final Path mPath = new Path();

    public CornerFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mPath.isEmpty()) {
            canvas.clipPath(mPath);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        mPath.reset();
        mPath.addRoundRect(new RectF(0, 0, width, height), mRadii, Path.Direction.CW);
    }

    /**
     * set each corner radius.
     *
     * @param topLeft     top left corner radius.
     * @param topRight    top right corner radius.
     * @param bottomRight bottom right radius.
     * @param bottomLeft  bottom right radius.
     */
    public void setRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        mRadii[0] = topLeft;
        mRadii[1] = topLeft;

        mRadii[2] = topRight;
        mRadii[3] = topRight;

        mRadii[4] = bottomRight;
        mRadii[5] = bottomRight;

        mRadii[6] = bottomLeft;
        mRadii[7] = bottomLeft;
        invalidate();
    }

    /**
     * set each corner radius.
     *
     * @param topLeftX     top left X
     * @param topLeftY     top left y
     * @param topRightX    top right x
     * @param topRightY    top right y
     * @param bottomRightX bottom right x
     * @param bottomRightY bottom right y
     * @param bottomLeftX  bottom left x
     * @param bottomLeftY  bottom left y
     */
    public void setRadius(float topLeftX, float topLeftY, float topRightX, float topRightY,float bottomRightX, float bottomRightY, float bottomLeftX, float bottomLeftY) {
        mRadii[0] = topLeftX;
        mRadii[1] = topLeftY;

        mRadii[2] = topRightX;
        mRadii[3] = topRightY;

        mRadii[4] = bottomRightX;
        mRadii[5] = bottomRightY;

        mRadii[6] = bottomLeftX;
        mRadii[7] = bottomLeftY;
        invalidate();
    }
}