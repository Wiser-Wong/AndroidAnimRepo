package com.wiser.animationlistdemo.combin.vh;

import android.view.View;
import android.view.ViewGroup;

import com.wiser.animationlistdemo.combin.base.ExRvItemViewHolderBase;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ExRvItemViewHolderEmpty extends ExRvItemViewHolderBase {

    private ExRvItemViewHolderEmpty(ViewGroup parent, int orientation) {

        super(getEmptyView(parent, orientation));
    }

    public static ExRvItemViewHolderEmpty newHoriInstance(ViewGroup parent) {

        return new ExRvItemViewHolderEmpty(parent, OrientationHelper.HORIZONTAL);
    }

    public static ExRvItemViewHolderEmpty newVertInstance(ViewGroup parent) {

        return new ExRvItemViewHolderEmpty(parent, OrientationHelper.VERTICAL);
    }

    private static View getEmptyView(ViewGroup parent, int orientation) {

        View view = new View(parent.getContext());

        //0表示水平朝向，1表示垂直朝向
//        if(orientation == OrientationHelper.VERTICAL)
//            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 100));
//        else
//            view.setLayoutParams(new RecyclerView.LayoutParams(50, RecyclerView.LayoutParams.WRAP_CONTENT));
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    protected void initConvertView(View convertView) {

        //nothing
    }
}
