package com.wiser.animationlistdemo.combin.manager;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TopSnappedLayoutManager extends GridLayoutManager {

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
        public TopSnappedSmoothScroller(Context context) {
            super(context);
        }

        public PointF computeScrollVectorForPosition(int targetPosition) {

            return TopSnappedLayoutManager.this.computeScrollVectorForPosition(targetPosition);
        }

        protected int getVerticalSnapPreference() {

            return -1;
        }
    }

    public TopSnappedLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TopSnappedLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public TopSnappedLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
