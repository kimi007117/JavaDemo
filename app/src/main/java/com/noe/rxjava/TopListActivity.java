package com.noe.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ArouterUtils.ACTIVITY_TOP_LIST)
public class TopListActivity extends AppCompatActivity {

    @BindView(R.id.listView_top)
    ListView mListView;

    @BindView(R.id.btn_top)
    Button mBtnTop;

    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        ButterKnife.bind(this);
        initData();
        mBtnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.smoothScrollToPositionFromTop(0, 0);
            }
        });

        mListView.setOnScrollListener(onScrollListener);
    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {


        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem <= 50) {
                mBtnTop.setVisibility(View.GONE);
            } else {
                mBtnTop.setVisibility(View.VISIBLE);
            }
        }
    };

    private void initData() {
        for (int m = 0; m <= 16; m++) {
            for (int i = 'A'; i <= 'Z'; i++) {
                arrayList.add(((char) i) + "");
            }
        }
        mListView.setAdapter(new StringAdapter());
    }

    class StringAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(TopListActivity.this, R.layout.recycler_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(arrayList.get(position));
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_recycler)
            TextView name;

            ViewHolder(View itemView) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
