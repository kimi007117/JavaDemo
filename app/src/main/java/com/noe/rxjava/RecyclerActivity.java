package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.widget.GradientRecyclerView;

import java.util.ArrayList;

/**
 * Created by lijie24 on 2016/11/30.
 */

public class RecyclerActivity extends AppCompatActivity implements View.OnClickListener {
    private GradientRecyclerView mRecyclerView;
    private ArrayList<String> mArrayList;
    private Context mContext;
    private Button btnChange;
    LinearLayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggerStaggeredGridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        mContext = this;
        initView();
        initData();
        onListener();
    }


    private void initView() {
        btnChange = (Button) findViewById(R.id.btn_change);
        mRecyclerView = findViewById(R.id.r_recyclerView);
        layoutManager = new LinearLayoutManager(mContext);
        gridLayoutManager = new GridLayoutManager(mContext, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        staggerStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        //设置布局管理器
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void initData() {

        mArrayList = new ArrayList<>();
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        CusAdapter adapter = new CusAdapter(mArrayList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, String data) {
                Toast.makeText(mContext, mArrayList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void onListener() {
        btnChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change: // 仿淘宝列表页布局切换
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    mRecyclerView.setLayoutManager(layoutManager);
                } else {
                    mRecyclerView.setLayoutManager(gridLayoutManager);
                }
                break;
            default:
                break;
        }
    }

    int count;

    class CusAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<String> arrayList;
        private OnItemClickListener mOnItemClickListener;

        CusAdapter(ArrayList<String> mArrayList) {
            this.arrayList = mArrayList;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            Log.d("xxxx-->", "onViewRecycled: " + holder.mTextView.getText().toString() + ", position: " + holder.getAdapterPosition());
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.mTextView.setText(arrayList.get(position));
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position, arrayList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            Log.i("onCreateViewHolder", "ViewHolder" + count++);
            mTextView = (TextView) itemView.findViewById(R.id.tv_card);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String data);
    }
}
