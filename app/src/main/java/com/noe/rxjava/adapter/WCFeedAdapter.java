package com.noe.rxjava.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseViewHolder;
import com.noe.rxjava.base.HeaderAndFooterRecyclerAdapter;
import com.noe.rxjava.holder.DiscoveryViewHolder;
import com.noe.rxjava.holder.DynamicViewHolder;
import com.noe.rxjava.holder.TopicViewHolder;

/**
 * 公司列表adapter
 * Created by lijie on 2019-06-22.
 */
public class WCFeedAdapter extends HeaderAndFooterRecyclerAdapter<WCFeed> {


    public static final int VIEW_TYPE_BASE = 0;
    public static final int VIEW_TYPE_TOPIC = VIEW_TYPE_BASE + 1;
    public static final int VIEW_TYPE_DISCOVERY = VIEW_TYPE_BASE + 2;
    public static final int VIEW_TYPE_DYNAMIC = VIEW_TYPE_BASE + 3;


    public WCFeedAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<WCFeed> doCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TOPIC:
                return new TopicViewHolder(mInflater.inflate(R.layout.wc_topic_list_item,
                        parent, false));
            case VIEW_TYPE_DISCOVERY:
                return new DiscoveryViewHolder(mInflater.inflate(R.layout.wc_discovery_list_item,
                        parent, false));
            case VIEW_TYPE_DYNAMIC:
                return new DynamicViewHolder(mInflater.inflate(R.layout.wc_dynamic_list_item,
                        parent, false));
            default:
                return null;
        }
    }

}
