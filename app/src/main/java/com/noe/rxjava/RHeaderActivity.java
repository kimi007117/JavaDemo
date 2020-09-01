package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.base.BaseViewHolder;
import com.noe.rxjava.base.ClickRecyclerAdapter;
import com.noe.rxjava.base.OnItemClickListener;
import com.noe.rxjava.image.CircleWithBorderTransformation;
import com.noe.rxjava.util.ArouterUtils;
import com.android.core.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by lijie on 2020-04-01.
 */
@Route(path = ArouterUtils.ACTIVITY_RHEADER)
public class RHeaderActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ImageView mIvCheck;
    private LinearLayout mCheckLayout;
    private Button mBtnClick;
    private InfoAdapter mInfoAdapter;
    private ArrayList<InfoVo> mList = new ArrayList<>();
    // 不可重复id
    private Set<String> ids = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_header);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mIvCheck = findViewById(R.id.iv_check);
        mCheckLayout = findViewById(R.id.layout_check);
        mBtnClick = findViewById(R.id.btn_click);
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_r_layout, null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mInfoAdapter = new InfoAdapter(this);
        mInfoAdapter.setOnItemClickListener(new OnItemClickListener<InfoVo>() {
            @Override
            public void onItemClick(View view, int position, InfoVo data) {
                Toast.makeText(mContext, data.title, Toast.LENGTH_SHORT).show();
            }
        });
        mInfoAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoAdapter.notifyDataSetChanged();
                updateChecked();
            }
        });
        mInfoAdapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(mInfoAdapter);

        mCheckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 相等代表已经全选需要反选，不相等代表没有全选，需要全选
                boolean checked = ids.size() != mList.size();
                for (InfoVo vo : mList) {
                    vo.checked = checked;
                }
                mInfoAdapter.notifyDataSetChanged();
                updateChecked();
            }
        });

        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "选中了 " + ids.size() + " 个", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateChecked() {
        for (InfoVo vo : mList) {
            if (vo.checked) {
                ids.add(vo.id);
            } else {
                ids.remove(vo.id);
            }
        }

        if (ids.size() == mList.size()) {
            mIvCheck.setImageResource(R.drawable.icon_checked);
        } else {
            mIvCheck.setImageResource(R.drawable.icon_unchecked);
        }
        if (ids.size() == 0) {
            mBtnClick.setText("跳转");
        } else {
            mBtnClick.setText(String.format(Locale.getDefault(), "跳转（%d）", ids.size()));
        }

        String strs = StringUtils.join(ids, ",");
        Toast.makeText(mContext, strs, Toast.LENGTH_SHORT).show();
    }


    private void initData() {
        InfoVo vo = new InfoVo();
        vo.id = "1";
        vo.title = "测试1";
        vo.subTitle = "这是一条测试内容的副标题1";
        vo.picUrl = "http://b-ssl.duitang.com/uploads/item/201608/28/20160828124034_GQrMv.thumb.700_0.jpeg";
        mList.add(vo);

        vo = new InfoVo();
        vo.id = "2";
        vo.title = "测试2";
        vo.subTitle = "这是一条测试内容的副标题2";
        vo.picUrl = "http://b-ssl.duitang.com/uploads/item/201608/28/20160828124034_GQrMv.thumb.700_0.jpeg";
        mList.add(vo);

        vo = new InfoVo();
        vo.id = "3";
        vo.title = "测试3";
        vo.subTitle = "这是一条测试内容的副标题3";
        vo.picUrl = "http://b-ssl.duitang.com/uploads/item/201608/28/20160828124034_GQrMv.thumb.700_0.jpeg";
        mList.add(vo);

        vo = new InfoVo();
        vo.id = "4";
        vo.title = "测试4";
        vo.subTitle = "这是一条测试内容的副标题4";
        vo.picUrl = "http://b-ssl.duitang.com/uploads/item/201608/28/20160828124034_GQrMv.thumb.700_0.jpeg";
        mList.add(vo);

        vo = new InfoVo();
        vo.id = "5";
        vo.title = "测试5";
        vo.subTitle = "这是一条测试内容的副标题5";
        vo.picUrl = "http://b-ssl.duitang.com/uploads/item/201608/28/20160828124034_GQrMv.thumb.700_0.jpeg";
        mList.add(vo);

        vo = new InfoVo();
        vo.id = "6";
        vo.title = "测试6";
        vo.subTitle = "这是一条测试内容的副标题6";
        vo.picUrl = "http://b-ssl.duitang.com/uploads/item/201608/28/20160828124034_GQrMv.thumb.700_0.jpeg";
        mList.add(vo);
        mInfoAdapter.setData(mList);
        mInfoAdapter.notifyDataSetChanged();
    }

    class InfoAdapter extends ClickRecyclerAdapter<InfoVo> {

        View.OnClickListener listener;

        public InfoAdapter(Context context) {
            super(context);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public BaseViewHolder<InfoVo> onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_r_header_layout, parent, false);
            return new ViewHolder(view, listener);
        }

        class ViewHolder extends BaseViewHolder<InfoVo> {
            ImageView mImageView;
            ImageView mIvCheck;
            TextView mTvTitle;
            TextView mTvSubTitle;

            public ViewHolder(View itemView, View.OnClickListener listener) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.iv_pic);
                mIvCheck = itemView.findViewById(R.id.iv_check);
                mTvTitle = itemView.findViewById(R.id.tv_title);
                mTvSubTitle = itemView.findViewById(R.id.tv_subtitle);
                mIvCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.checked = !data.checked;
                        if (listener != null) {
                            listener.onClick(v);
                        }
                    }
                });
            }

            @Override
            public void onBind(InfoVo data, int position) {
                super.onBind(data, position);
                Glide.with(mContext)
                        .load(data.picUrl)
                        .transform(new CircleWithBorderTransformation(mContext, 0, 0))
                        .into(mImageView);
                mTvTitle.setText(data.title);
                mTvSubTitle.setText(data.subTitle);
                if (data.checked) {
                    mIvCheck.setImageResource(R.drawable.icon_checked);
                } else {
                    mIvCheck.setImageResource(R.drawable.icon_unchecked);
                }
            }
        }
    }


    class InfoVo {
        public String id;
        public String title;
        public String subTitle;
        public String picUrl;
        public boolean checked;
    }
}
