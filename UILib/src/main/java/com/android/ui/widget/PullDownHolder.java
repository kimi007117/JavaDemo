package com.android.ui.widget;

import android.content.Context;
import android.view.View;

import com.android.ui.R;

/**
 * Created by lijie on 2019-08-17.
 */
public class PullDownHolder implements RefreshLayout.IPullDownLayoutHolder {

    private Context mContext;
    private View headerView;
    private final RefreshLayout mRefreshLayout;

    public PullDownHolder(Context context, RefreshLayout refreshLayout) {
        mContext = context;
        mRefreshLayout = refreshLayout;
        initHeader();
    }

    private void initHeader() {
        headerView = View.inflate(mContext, R.layout.refresh_header, null);
    }

    @Override
    public void onStateChanged(RefreshLayout.State state) {
        switch (state) {
            case DONE:
                break;
            case REFRESHING:
                break;
            case PULL_TO_REFRESH:
                break;
            case RELEASE_TO_REFRESH:
                break;
            case RELEASE_TO_SECOND_FLOOR:
                break;
        }
    }

    @Override
    public View getRefreshHeaderView() {
        return headerView;
    }

    @Override
    public int getRefreshStartHeight() {
        return 150;
    }

    @Override
    public int getSecondFloorStartHeight() {
        return 300;
    }

    @Override
    public void setupOffsetY(int offsetY) {

    }
}
