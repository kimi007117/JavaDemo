package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;

@Route(path = ArouterUtils.ACTIVITY_MARQUEE)
public class MarqueeActivity extends AppCompatActivity {
    private Context mContext;
    private TextView mTitleTextView;
    private TextView mHeaderTitleTextView;
    private RecyclerView mRecyclerView;
    private View mHeaderView;
    private Button mButton;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_marquee);
        mTitleTextView = findViewById(R.id.tv_title1);
        mRecyclerView = findViewById(R.id.recyclerView);
        mButton = findViewById(R.id.btn_ok);
        mHeaderView = View.inflate(this, R.layout.header_marquee_view, null);
        mHeaderTitleTextView = mHeaderView.findViewById(R.id.tv_header_title);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(arrayList);
        adapter.addHeaderView(mHeaderView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        initData();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "hahahhaha", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            arrayList.add(((char) i) + "");
        }

    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int height = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            height += dy;
            float percentage = height / 150f;
            if (percentage < 1f && percentage > 0.1f) {
                mTitleTextView.setVisibility(View.INVISIBLE);
                mHeaderTitleTextView.setVisibility(View.VISIBLE);
            } else if (percentage >= 1f) {
                mHeaderTitleTextView.setVisibility(View.INVISIBLE);
                mTitleTextView.setVisibility(View.VISIBLE);
            } else {
                mTitleTextView.setVisibility(View.INVISIBLE);
                mHeaderTitleTextView.setVisibility(View.VISIBLE);
            }

        }
    };

    class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<String> mArrayList;
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_NORMAL = 1;
        private View mHeaderView;

        public MyRecyclerAdapter(ArrayList<String> arrayList) {
            this.mArrayList = arrayList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mHeaderView != null && viewType == TYPE_HEADER) {
                return new ViewHolder(mHeaderView);
            }

            View view = LayoutInflater.from(MarqueeActivity.this).inflate(R.layout.recycler_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        public void addHeaderView(View headerView) {
            mHeaderView = headerView;
            notifyItemInserted(0);
        }

        @Override
        public int getItemViewType(int position) {
            if (mHeaderView == null) {
                return TYPE_NORMAL;
            }
            if (position == 0) {
                return TYPE_HEADER;
            }
            return TYPE_NORMAL;
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && holder.getLayoutPosition() == 0) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_HEADER) {
                return;
            }
            if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.mTextView.setText(arrayList.get(position - 1));
                viewHolder.mTextView.setTag(arrayList.get(position - 1));
            }

        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
            if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
            }

        }

        @Override
        public int getItemCount() {
            return mHeaderView == null ? mArrayList.size() : mArrayList.size() + 1;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
            mTextView = (TextView) itemView.findViewById(R.id.tv_recycler);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTextView.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
