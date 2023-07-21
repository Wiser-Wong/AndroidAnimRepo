package com.wiser.animationlistdemo.toast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.util.ArrayList;

public class MaterialProgressDrawable extends Drawable implements Animatable {
    public static final int LARGE = 0;
    public static final int DEFAULT = 1;
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator END_CURVE_INTERPOLATOR = new EndCurveInterpolator();
    private static final Interpolator START_CURVE_INTERPOLATOR = new StartCurveInterpolator();
    private static final Interpolator EASE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final int CIRCLE_DIAMETER = 40;
    private static final float CENTER_RADIUS = 8.75f;
    private static final float STROKE_WIDTH = 2.5f;
    private static final int CIRCLE_DIAMETER_LARGE = 56;
    private static final float CENTER_RADIUS_LARGE = 12.5f;
    static final float STROKE_WIDTH_LARGE = 3f;
    private static final int ANIMATION_DURATION = 1000 * 80 / 60;
    private static final float NUM_POINTS = 5f;
    private static final int ARROW_WIDTH = 10;
    private static final int ARROW_HEIGHT = 5;
    private static final float ARROW_OFFSET_ANGLE = 0;
    static final int ARROW_WIDTH_LARGE = 12;
    static final int ARROW_HEIGHT_LARGE = 6;
    private static final float MAX_PROGRESS_ARC = .8f;
    private final int[] COLORS = new int[]{
            Color.BLACK
    };
    private final ArrayList<Animation> mAnimators = new ArrayList<Animation>();
    private final Ring mRing;
    private final Callback mCallback = new Callback() {
        @Override
        public void invalidateDrawable(Drawable d) {
            invalidateSelf();
        }

        @Override
        public void scheduleDrawable(Drawable d, Runnable what, long when) {
            scheduleSelf(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable d, Runnable what) {
            unscheduleSelf(what);
        }
    };
    boolean mFinishing;
    private float mRotation;
    private Resources mResources;
    private View mAnimExcutor;
    private Animation mAnimation;
    private float mRotationCount;
    private double mWidth;
    private double mHeight;
    private boolean mShowArrowOnFirstStart = false;

    public MaterialProgressDrawable(Context context, View animExcutor) {
        mAnimExcutor = animExcutor;
        mResources = context.getResources();

        mRing = new Ring(mCallback);
        mRing.setColors(COLORS);

        updateSizes(DEFAULT);
        setupAnimators();
    }

    public void setSizeParameters(double progressCircleWidth, double progressCircleHeight,
                                   double centerRadius, double strokeWidth, float arrowWidth, float arrowHeight) {
        final Ring ring = mRing;
        mWidth = progressCircleWidth;
        mHeight = progressCircleHeight ;
        ring.setStrokeWidth((float) strokeWidth );
        ring.setCenterRadius(centerRadius);
        ring.setColorIndex(0);
        ring.setArrowDimensions(arrowWidth , arrowHeight );
        ring.setInsets((int) mWidth, (int) mHeight);
    }

    public void updateSizes(@ProgressDrawableSize int size) {
        final DisplayMetrics metrics = mResources.getDisplayMetrics();
        final float screenDensity = metrics.density;

        if (size == LARGE) {
            setSizeParameters(CIRCLE_DIAMETER_LARGE*screenDensity, CIRCLE_DIAMETER_LARGE*screenDensity, CENTER_RADIUS_LARGE*screenDensity,
                    STROKE_WIDTH_LARGE*screenDensity, ARROW_WIDTH_LARGE*screenDensity, ARROW_HEIGHT_LARGE*screenDensity);
        } else {
            setSizeParameters(CIRCLE_DIAMETER*screenDensity, CIRCLE_DIAMETER*screenDensity, CENTER_RADIUS*screenDensity, STROKE_WIDTH*screenDensity,
                    ARROW_WIDTH*screenDensity, ARROW_HEIGHT*screenDensity);
        }
    }

    public void showArrow(boolean show) {
        mRing.setShowArrow(show);
    }

    public void setArrowScale(float scale) {
        mRing.setArrowScale(scale);
    }

    public void setStartEndTrim(float startAngle, float endAngle) {
        mRing.setStartTrim(startAngle);
        mRing.setEndTrim(endAngle);
    }

    public void setProgressRotation(float rotation) {
        mRing.setRotation(rotation);
    }

    public void setBackgroundColor(int color) {
        mRing.setBackgroundColor(color);
    }

    public void setColorSchemeColors(int... colors) {
        mRing.setColors(colors);
        mRing.setColorIndex(0);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) mHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) mWidth;
    }

    @Override
    public void draw(Canvas c) {
        final Rect bounds = getBounds();
        final int saveCount = c.save();
        c.rotate(mRotation, bounds.exactCenterX(), bounds.exactCenterY());
        mRing.draw(c, bounds);
        c.restoreToCount(saveCount);
    }

    public int getAlpha() {
        return mRing.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        mRing.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mRing.setColorFilter(colorFilter);
    }

    @SuppressWarnings("unused")
    private float getRotation() {
        return mRotation;
    }

    @SuppressWarnings("unused")
    void setRotation(float rotation) {
        mRotation = rotation;
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isRunning() {
        return  !this.mAnimation.hasEnded();
    }

    @Override
    public void start() {
        mAnimation.reset();
        mRing.storeOriginals();
        mRing.setShowArrow(mShowArrowOnFirstStart);

        if (mRing.getEndTrim() != mRing.getStartTrim()) {
            mFinishing = true;
            mAnimation.setDuration(ANIMATION_DURATION / 2);
            mAnimExcutor.startAnimation(mAnimation);
        } else {
            mRing.setColorIndex(0);
            mRing.resetOriginals();
            mAnimation.setDuration(ANIMATION_DURATION);
            mAnimExcutor.startAnimation(mAnimation);
        }
    }

    @Override
    public void stop() {
        mAnimExcutor.clearAnimation();
        setRotation(0);
        mRing.setShowArrow(false);
        mRing.setColorIndex(0);
        mRing.resetOriginals();
    }

    private void applyFinishTranslation(float interpolatedTime, Ring ring) {
        float targetRotation = (float) (Math.floor(ring.getStartingRotation() / MAX_PROGRESS_ARC)
                + 1f);
        final float startTrim = ring.getStartingStartTrim()
                + (ring.getStartingEndTrim() - ring.getStartingStartTrim()) * interpolatedTime;
        ring.setStartTrim(startTrim);
        final float rotation = ring.getStartingRotation()
                + ((targetRotation - ring.getStartingRotation()) * interpolatedTime);
        ring.setRotation(rotation);
    }

    private void setupAnimators() {
        final Ring ring = mRing;
        final Animation animation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                if (mFinishing) {
                    applyFinishTranslation(interpolatedTime, ring);
                } else {
                    final float minProgressArc = (float) Math.toRadians(
                            ring.getStrokeWidth() / (2 * Math.PI * ring.getCenterRadius()));
                    final float startingEndTrim = ring.getStartingEndTrim();
                    final float startingTrim = ring.getStartingStartTrim();
                    final float startingRotation = ring.getStartingRotation();

                    final float minArc = MAX_PROGRESS_ARC - minProgressArc;
                    float endTrim = startingEndTrim + (minArc
                            * START_CURVE_INTERPOLATOR.getInterpolation(interpolatedTime));
                    float startTrim = startingTrim + (MAX_PROGRESS_ARC
                            * END_CURVE_INTERPOLATOR.getInterpolation(interpolatedTime));

                    final float sweepTrim =  endTrim-startTrim;
                    if(Math.abs(sweepTrim)>=1){
                        endTrim = startTrim+0.5f;
                    }

                    ring.setEndTrim(endTrim);

                    ring.setStartTrim(startTrim);

                    final float rotation = startingRotation + (0.25f * interpolatedTime);
                    ring.setRotation(rotation);

                    float groupRotation = ((720.0f / NUM_POINTS) * interpolatedTime)
                            + (720.0f * (mRotationCount / NUM_POINTS));
                    setRotation(groupRotation);
                }
            }
        };
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(LINEAR_INTERPOLATOR);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mRotationCount = 0;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                ring.storeOriginals();
                ring.goToNextColor();
                ring.setStartTrim(ring.getEndTrim());
                if (mFinishing) {
                    mFinishing = false;
                    animation.setDuration(ANIMATION_DURATION);
                    ring.setShowArrow(false);
                } else {
                    mRotationCount = (mRotationCount + 1) % (NUM_POINTS);
                }
            }
        });
        mAnimation = animation;
    }

    public void showArrowOnFirstStart(boolean showArrowOnFirstStart) {
        this.mShowArrowOnFirstStart = showArrowOnFirstStart;
    }


    public @interface ProgressDrawableSize {
    }

    private static class Ring {
        private final RectF mTempBounds = new RectF();
        private final Paint mPaint = new Paint();
        private final Paint mArrowPaint = new Paint();

        private final Callback mCallback;
        private final Paint mCirclePaint = new Paint();
        private float mStartTrim = 0.0f;
        private float mEndTrim = 0.0f;
        private float mRotation = 0.0f;
        private float mStrokeWidth = 5.0f;
        private float mStrokeInset = 2.5f;
        private int[] mColors;
        private int mColorIndex;
        private float mStartingStartTrim;
        private float mStartingEndTrim;
        private float mStartingRotation;
        private boolean mShowArrow;
        private Path mArrow;
        private float mArrowScale;
        private double mRingCenterRadius;
        private int mArrowWidth;
        private int mArrowHeight;
        private int mAlpha;
        private int mBackgroundColor;

        public Ring(Callback callback) {
            mCallback = callback;

            mPaint.setStrokeCap(Paint.Cap.SQUARE);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Style.STROKE);

            mArrowPaint.setStyle(Style.FILL);
            mArrowPaint.setAntiAlias(true);
        }

        public void setBackgroundColor(int color) {
            mBackgroundColor = color;
        }

        public void setArrowDimensions(float width, float height) {
            mArrowWidth = (int) width;
            mArrowHeight = (int) height;
        }

        public void draw(Canvas c, Rect bounds) {
            final RectF arcBounds = mTempBounds;
            arcBounds.set(bounds);
            arcBounds.inset(mStrokeInset, mStrokeInset);

            final float startAngle = (mStartTrim + mRotation) * 360;
            final float endAngle = (mEndTrim + mRotation) * 360;
            float sweepAngle = endAngle - startAngle;
            mPaint.setColor(mColors[mColorIndex]);
            c.drawArc(arcBounds, startAngle, sweepAngle, false, mPaint);

            drawTriangle(c, startAngle, sweepAngle, bounds);

            if (mAlpha < 255) {
                mCirclePaint.setColor(mBackgroundColor);
                mCirclePaint.setAlpha(255 - mAlpha);
                c.drawCircle(bounds.exactCenterX(), bounds.exactCenterY(), bounds.width() / 2,
                        mCirclePaint);
            }
        }

        private void drawTriangle(Canvas c, float startAngle, float sweepAngle, Rect bounds) {
            if (mShowArrow) {
                if (mArrow == null) {
                    mArrow = new Path();
                    mArrow.setFillType(Path.FillType.EVEN_ODD);
                } else {
                    mArrow.reset();
                }

                float x = (float) (mRingCenterRadius * Math.cos(0) + bounds.exactCenterX());
                float y = (float) (mRingCenterRadius * Math.sin(0) + bounds.exactCenterY());

                mArrow.moveTo(0, 0);
                mArrow.lineTo((mArrowWidth) * mArrowScale, 0);
                mArrow.lineTo(((mArrowWidth) * mArrowScale / 2), (mArrowHeight
                        * mArrowScale));
                mArrow.offset(x-((mArrowWidth) * mArrowScale / 2), y);
                mArrow.close();
                c.rotate(startAngle + (sweepAngle<0?0:sweepAngle) - ARROW_OFFSET_ANGLE, bounds.exactCenterX(),
                        bounds.exactCenterY());
                c.drawPath(mArrow, mArrowPaint);
            }
        }

        public void setColors(int[] colors) {
            mColors = colors;
            setColorIndex(0);
        }

        public void setColorIndex(int index) {
            mColorIndex = index;
        }

        public void goToNextColor() {
            mColorIndex = (mColorIndex + 1) % (mColors.length);
        }

        public void setColorFilter(ColorFilter filter) {
            mPaint.setColorFilter(filter);
            invalidateSelf();
        }

        public int getAlpha() {
            return mAlpha;
        }

        public void setAlpha(int alpha) {
            mAlpha = alpha;
        }

        @SuppressWarnings("unused")
        public float getStrokeWidth() {
            return mStrokeWidth;
        }

        public void setStrokeWidth(float strokeWidth) {
            mStrokeWidth = strokeWidth;
            mPaint.setStrokeWidth(strokeWidth);
            invalidateSelf();
        }

        @SuppressWarnings("unused")
        public float getStartTrim() {
            return mStartTrim;
        }

        @SuppressWarnings("unused")
        public void setStartTrim(float startTrim) {
            mStartTrim = startTrim;
            invalidateSelf();
        }

        public float getStartingStartTrim() {
            return mStartingStartTrim;
        }

        public float getStartingEndTrim() {
            return mStartingEndTrim;
        }

        @SuppressWarnings("unused")
        public float getEndTrim() {
            return mEndTrim;
        }

        @SuppressWarnings("unused")
        public void setEndTrim(float endTrim) {
            mEndTrim = endTrim;
            invalidateSelf();
        }

        @SuppressWarnings("unused")
        public float getRotation() {
            return mRotation;
        }

        @SuppressWarnings("unused")
        public void setRotation(float rotation) {
            mRotation = rotation;
            invalidateSelf();
        }

        public void setInsets(int width, int height) {
            final float minEdge = (float) Math.min(width, height);
            float insets;
            if (mRingCenterRadius <= 0 || minEdge < 0) {
                insets = (float) Math.ceil(mStrokeWidth / 2.0f);
            } else {
                insets = (float) (minEdge / 2.0f - mRingCenterRadius);
            }
            mStrokeInset = insets;
        }

        @SuppressWarnings("unused")
        public float getInsets() {
            return mStrokeInset;
        }

        public double getCenterRadius() {
            return mRingCenterRadius;
        }

        public void setCenterRadius(double centerRadius) {
            mRingCenterRadius = centerRadius;
        }

        public void setShowArrow(boolean show) {
            if (mShowArrow != show) {
                mShowArrow = show;
                invalidateSelf();
            }
        }

        public void setArrowScale(float scale) {
            if (scale != mArrowScale) {
                mArrowScale = scale;
                invalidateSelf();
            }
        }

        public float getStartingRotation() {
            return mStartingRotation;
        }

        public void storeOriginals() {
            mStartingStartTrim = mStartTrim;
            mStartingEndTrim = mEndTrim;
            mStartingRotation = mRotation;
        }
        public void resetOriginals() {
            mStartingStartTrim = 0;
            mStartingEndTrim = 0;
            mStartingRotation = 0;
            setStartTrim(0);
            setEndTrim(0);
            setRotation(0);
        }

        private void invalidateSelf() {
            mCallback.invalidateDrawable(null);
        }
    }

    private static class EndCurveInterpolator extends AccelerateDecelerateInterpolator {
        @Override
        public float getInterpolation(float input) {
            return super.getInterpolation(Math.max(0, (input - 0.5f) * 2.0f));
        }
    }

    private static class StartCurveInterpolator extends AccelerateDecelerateInterpolator {
        @Override
        public float getInterpolation(float input) {
            return super.getInterpolation(Math.min(1, input * 2.0f));
        }
    }
}