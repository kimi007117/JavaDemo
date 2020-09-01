package com.noe.rxjava.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseViewHolder;
import com.noe.rxjava.base.ClickRecyclerAdapter;

/**
 * Created by lijie on 2019-10-12.
 */
public class RecyclerItemAdapter extends ClickRecyclerAdapter<String> {

    public RecyclerItemAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<String> onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.header_item_image, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<String> {

        ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_image);
        }

        @Override
        public void onBind(String data, int position) {
            super.onBind(data, position);

        }
    }
}
