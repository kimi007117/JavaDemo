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
public class DiscoveryViewHolder extends BaseViewHolder<WCFeed> {

    TextView mTvName;
    TextView mTvAddress;
    TextView mTvTel;
    TextView mTvEmail;
    ImageView mIvLogo;

    public DiscoveryViewHolder(View itemView) {
        super(itemView);
        inflateView(itemView);
    }

    private void inflateView(View view) {
        mTvName = view.findViewById(R.id.tv_name);
        mTvAddress = view.findViewById(R.id.tv_address);
        mTvTel = view.findViewById(R.id.tv_tel);
        mTvEmail = view.findViewById(R.id.tv_email);
        mIvLogo = view.findViewById(R.id.iv_logo);
    }

    @Override
    public void onBind(WCFeed data, int position) {
        super.onBind(data, position);
        if (data == null) {
            return;
        }
        // 设置数据
        setData(data);
        // 标记已读未读
        setReadFlag(data);
    }

    private void setReadFlag(WCFeed data) {

    }

    private void setData(WCFeed data) {

    }
}