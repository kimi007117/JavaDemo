package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.bean.DouBanMovieBean;
import com.noe.rxjava.bean.MovieDetailBean;
import com.noe.rxjava.di.component.DaggerActivityComponent;
import com.noe.rxjava.di.module.ActivityModule;
import com.noe.rxjava.image.ImageLoaderManager;
import com.noe.rxjava.mvp.contract.DouBanContract;
import com.noe.rxjava.mvp.presenter.DouBanPresenter;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ArouterUtils.ACTIVITY_DOUBAN)
public class DouBanActivity extends AppCompatActivity implements DouBanContract.View {

    @Inject
    DouBanPresenter mDouBanPresenter;
    @BindView(R.id.rv_douban)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douban);
        ButterKnife.bind(this);
//        mDouBanPresenter = new DouBanPresenter(this);
//        mDuBanPresenter.subscribe(this);
        DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build().inject(this);
        mDouBanPresenter.subscribe(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(DouBanContract.Presenter presenter) {
    }

    @Override
    public void setContent(DouBanMovieBean douBanMovieBean) {
        DouBanAdapter adapter = new DouBanAdapter(DouBanActivity.this, douBanMovieBean.subjects);
        mRecyclerView.setLayoutManager(new GridLayoutManager(DouBanActivity.this, 3));
        mRecyclerView.setAdapter(adapter);
    }

    private class DouBanAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<MovieDetailBean> mSubjects;
        private LayoutInflater mInflater;
        private Context mContext;

        DouBanAdapter(Context context, ArrayList<MovieDetailBean> subjects) {
            this.mSubjects = subjects;
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_douban, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (mSubjects != null && mSubjects.size() > 0) {
                MovieDetailBean movieDetailBean = mSubjects.get(position);
                if (movieDetailBean != null) {
                    holder.title.setText(movieDetailBean.title);
                    if (movieDetailBean.images != null) {
                        ImageLoaderManager.getInstance().showImage(ImageLoaderManager.getDefaultOptions(holder.cover,movieDetailBean.images.large));
//                        Glide.with(mContext).load(movieDetailBean.images.large).into(holder.cover);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return mSubjects == null ? 0 : mSubjects.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_douban_cover)
        ImageView cover;
        @BindView(R.id.tv_douban_title)
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
