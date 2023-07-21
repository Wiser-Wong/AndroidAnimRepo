package com.wiser.animationlistdemo.combin.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;


import com.wiser.animationlistdemo.combin.base.ExRvItemViewHolderBase;

import androidx.recyclerview.widget.RecyclerView;

public class ExRvDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        onExRvDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        onExRvDrawOver(c, parent, state);
    }

    protected void onExRvDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        //nothing
    }

    protected void onExRvDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        //nothing
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        getExRvItemOffsets(outRect, view, parent, state);
    }

    protected void getExRvItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        ExRvItemViewHolderBase viewHolder = (ExRvItemViewHolderBase) parent.getChildViewHolder(view);
        getExRvDataItemOffsets(outRect, viewHolder, parent, state);
    }
    protected void getExRvDataItemOffsets(Rect outRect, ExRvItemViewHolderBase viewHolder, RecyclerView parent, RecyclerView.State state) {

        //nothing
    }

}
