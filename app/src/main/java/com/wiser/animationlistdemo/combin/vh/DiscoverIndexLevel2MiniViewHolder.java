package com.wiser.animationlistdemo.combin.vh;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.wiser.animationlistdemo.R;
import com.wiser.animationlistdemo.combin.TextUtil;
import com.wiser.animationlistdemo.combin.base.ExRvItemViewHolderBase;
import com.wiser.animationlistdemo.combin.bean.Oper;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;


public class DiscoverIndexLevel2MiniViewHolder extends ExRvItemViewHolderBase {

    ImageView mFivCover;
    TextView mTvName;

    public DiscoverIndexLevel2MiniViewHolder(ViewGroup viewGroup) {

        super(viewGroup, R.layout.page_discover_index_level2_vh_mini);
        mFivCover = itemView.findViewById(R.id.fivCover);
        mTvName = itemView.findViewById(R.id.tvName);
    }

    @Override
    protected void initConvertView(View convertView) {

        convertView.setOnClickListener(this);
    }

    public void invalidateView(Oper oper){

        if (oper == null) {

            mTvName.setText(TextUtil.TEXT_EMPTY);
            //mFivCover.setImageUriByLp((String) null);
        } else {

            mTvName.setText(oper.getTitle());

            String pic = oper.getPic();
            if (!pic.startsWith("http")){
                if (pic.endsWith("gif")){
                    Resources res = this.getConvertView().getContext().getResources();
                    String packageName = this.getConvertView().getContext().getPackageName();
                    int resID = res.getIdentifier(pic.subSequence(0,pic.indexOf(".gif")).toString(), "drawable", packageName);
                    try {
                        GifDrawable gifDrawable = new GifDrawable(res,resID);
                        mFivCover.setImageDrawable(gifDrawable);
                    }catch (Exception e){

                    }

//                    Glide.with(mFivCover).asGif().load(resID).into(mFivCover);

                    return;
                }
            }
            //mFivCover.setImageUriByLp(oper.getPic());
            Glide.with(this.getConvertView().getContext())
                    .load(oper.getPic())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(mFivCover);
        }
    }
}
