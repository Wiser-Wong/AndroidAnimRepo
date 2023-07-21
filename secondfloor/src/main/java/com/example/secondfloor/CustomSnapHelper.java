package com.example.secondfloor;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CustomSnapHelper extends LinearSnapHelper {

    private int mMaxFlingVelocity = -1;
    private int mMinFlingVelocity = -1;
    private Interpolator mInterpolator;
    public CustomSnapHelper(Context context) {
        super();
        mInterpolator = new DecelerateInterpolator();
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaxFlingVelocity = configuration.getScaledMaximumFlingVelocity() / 2; // 设置最大fling速度为系统默认值的一半
        mMinFlingVelocity = configuration.getScaledMinimumFlingVelocity() ; // 设置最小fling速度为系统默认值的两倍
    }

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        // 对fling速度进行限制
        int clampedVelocityX = clampFlingVelocity(velocityX);
        int clampedVelocityY = clampFlingVelocity(velocityY);
        return super.onFling(clampedVelocityX, clampedVelocityY);
    }
    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        if (recyclerView != null) {
            recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
                @Override
                public boolean onFling(int velocityX, int velocityY) {
                    // 应用插值器
                    if (mInterpolator != null) {
                        velocityX = (int) (velocityX * mInterpolator.getInterpolation(Math.abs(velocityX)));
                        velocityY = (int) (velocityY * mInterpolator.getInterpolation(Math.abs(velocityY)));
                    }
                    return CustomSnapHelper.super.onFling(velocityX, velocityY);
                }
            });
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }
    private int clampFlingVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, mMaxFlingVelocity);
        } else {
            return Math.max(velocity, -mMaxFlingVelocity);
        }
    }
}