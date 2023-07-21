package com.wiser.animationlistdemo.combin.adapter;

import android.view.ViewGroup;

import com.wiser.animationlistdemo.DensityUtil;
import com.wiser.animationlistdemo.combin.CollectionUtil;
import com.wiser.animationlistdemo.combin.DeviceUtil;
import com.wiser.animationlistdemo.combin.base.ExRvAdapterBase;
import com.wiser.animationlistdemo.combin.base.ExRvItemViewHolderBase;
import com.wiser.animationlistdemo.combin.bean.DiscoverOper;
import com.wiser.animationlistdemo.combin.bean.Oper;
import com.wiser.animationlistdemo.combin.vh.DiscoverIndexLevel2BannerViewHolder;
import com.wiser.animationlistdemo.combin.vh.DiscoverIndexLevel2MiniViewHolder;
import com.wiser.animationlistdemo.combin.vh.DiscoverIndexLevel2TitleViewHolder;
import com.wiser.animationlistdemo.combin.vh.ExRvItemViewHolderEmpty;

public class DiscoverLevel2Adapter extends ExRvAdapterBase {

    public static final int TYPE_ITEM_TITLE = 0;     // Title 运营位
    public static final int TYPE_ITEM_CONTAINER = 1;  // item
    public static final int TYPE_ITEM_BANNER = 2;   // Banner
    private static final int TYPE_ITEM_NONE = 3;           // NONE

    /**
     * 查询指定elementId的运营位元素pos
     *
     * @param elementId
     * @return 未找到返回-1
     */
    public int getSelectPosition(int elementId) {

        for (int i = 0; i < CollectionUtil.size(getData()); i++) {

            DiscoverOper discoverOper = (DiscoverOper) CollectionUtil.getItem(getData(), i);
            if (discoverOper.getElement_id() == elementId)
                return i;
        }
        return -1;
    }

    @Override
    public int getDataItemViewType(int dataPos) {

        Object obj = getDataItem(dataPos);
        if (obj instanceof DiscoverOper) {

            DiscoverOper oper = (DiscoverOper) obj;

            if (oper.isTypeContainer())
                return TYPE_ITEM_CONTAINER;
            else if (oper.isTypeBanner())
                return TYPE_ITEM_BANNER;
            else if (oper.isTypeTitle())
                return TYPE_ITEM_TITLE;
        }

        return TYPE_ITEM_NONE;
    }

    @Override
    public ExRvItemViewHolderBase onCreateDataViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_ITEM_TITLE:
                return new DiscoverIndexLevel2TitleViewHolder(parent);
            case TYPE_ITEM_CONTAINER:
                return new DiscoverIndexLevel2MiniViewHolder(parent);
            case TYPE_ITEM_BANNER:
                int bannerWidth = (int) (DeviceUtil.getScreenWidth() * (0.75f)) - DensityUtil.dip2px(20f);
                return new DiscoverIndexLevel2BannerViewHolder(parent, bannerWidth);
            default:
            case TYPE_ITEM_NONE:
                return ExRvItemViewHolderEmpty.newVertInstance(parent);
        }
    }

    @Override
    public void onBindDataViewHolder(ExRvItemViewHolderBase holder, int dataPos) {

        if (holder instanceof DiscoverIndexLevel2TitleViewHolder)
            ((DiscoverIndexLevel2TitleViewHolder) holder).invalidateView((DiscoverOper) getDataItem(dataPos));
        else if (holder instanceof DiscoverIndexLevel2MiniViewHolder)
            ((DiscoverIndexLevel2MiniViewHolder) holder).invalidateView((Oper) getDataItem(dataPos));
        else if (holder instanceof DiscoverIndexLevel2BannerViewHolder)
            ((DiscoverIndexLevel2BannerViewHolder) holder).invalidateView((Oper) getDataItem(dataPos));
    }
}
