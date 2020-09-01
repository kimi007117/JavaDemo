package com.noe.rxjava.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseViewHolder;
import com.noe.rxjava.base.HeaderAndFooterRecyclerAdapter;

/**
 * Created by lijie on 2019-10-12.
 */
public class CustomerItemAdapter extends HeaderAndFooterRecyclerAdapter<String> {

    public CustomerItemAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<String> doCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<String> {

        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_recycler);
            itemView.setOnClickListener(v -> Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show());
        }

        @Override
        public void onBind(String data, int position) {
            super.onBind(data, position);
            if (TextUtils.isEmpty(data)) {
                return;
            }
            mTextView.setText(data);

        }
    }
}
