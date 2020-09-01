package com.noe.rxjava.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseViewHolder;
import com.noe.rxjava.base.ClickRecyclerAdapter;
import com.noe.rxjava.bean.User;

import java.util.List;

/**
 * Created by lijie on 2020/8/8.
 */
public class TestHolder {

    private List<User> mList;
    private Context mContext;

    private TestHolder(Context context, List<User> list) {
        this.mContext = context;
        this.mList = list;
    }

    public static TestHolder create(Context context, List<User> list) {
        return new TestHolder(context, list);
    }

    public void bind(ViewGroup parent) {
        if (parent == null) {
            return;
        }

        View layout = View.inflate(mContext, R.layout.layout_test_holder, parent);
        setData(layout);
    }

    private void setData(View layout) {
        RecyclerView recyclerView = layout.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        TestAdapter testAdapter = new TestAdapter(mContext);
        testAdapter.setData(mList);
        recyclerView.setAdapter(testAdapter);
    }

    private static class TestAdapter extends ClickRecyclerAdapter<User> {

        public TestAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder<User> onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            return new TestViewHolder(mInflater.inflate(R.layout.item_test_holder, parent, false));
        }

        static class TestViewHolder extends BaseViewHolder<User> {

            TextView mTextView1;
            TextView mTextView2;


            public TestViewHolder(View itemView) {
                super(itemView);
                mTextView1 = itemView.findViewById(R.id.tv_title);
                mTextView2 = itemView.findViewById(R.id.tv_count);
            }

            @Override
            public void onBind(User data, int position) {
                super.onBind(data, position);
                if (data == null) {
                    return;
                }
                mTextView1.setText(data.name);
                mTextView2.setText(data.phone);
            }
        }
    }

}
