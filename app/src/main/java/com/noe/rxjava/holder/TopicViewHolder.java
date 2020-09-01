package com.noe.rxjava.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.noe.rxjava.R;
import com.noe.rxjava.adapter.WCFeed;
import com.noe.rxjava.base.BaseViewHolder;

/**
 * Created by lijie on 2020-06-22.
 */
public class TopicViewHolder extends BaseViewHolder<WCFeed> {

    TextView mTvTitle;
    TextView mTvSubTitle;
    TextView mTvDesc;
    ImageView mIvBackdrop;

    public TopicViewHolder(View itemView) {
        super(itemView);
        inflateView(itemView);
    }

    private void inflateView(View view) {
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvSubTitle = view.findViewById(R.id.tv_subtitle);
        mTvDesc = view.findViewById(R.id.tv_desc);
        mIvBackdrop = view.findViewById(R.id.iv_backdrop);
    }

    @Override
    public void onBind(WCFeed data, int position) {
        super.onBind(data, position);
        if (data == null) {
            return;
        }
        // 设置item数据
        setItemData(data);
        // 标记已读未读
        setReadFlag(data);
    }

    private void setReadFlag(WCFeed data) {

    }

    private void setItemData(WCFeed data) {

    }
}