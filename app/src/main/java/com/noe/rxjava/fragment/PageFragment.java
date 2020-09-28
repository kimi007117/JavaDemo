package com.noe.rxjava.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.noe.rxjava.R;
import com.noe.rxjava.view.NoScrollListView;

import java.util.ArrayList;

import timber.log.Timber;


/**
 * Created by lijie24 on 2016/11/28.
 */

public class PageFragment extends Fragment {
    private Context mContext;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ArrayList<String> arrayList = new ArrayList<>();
    private NoScrollListView mListView;
    private View mRootView;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);

        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_viewpager, container, false);
        initView(mRootView);
        return mRootView;
    }

    private void initView(View view) {

        Log.i("xxxxxxxx","go on");
        for (int i = 'A'; i <= 'Z'; i++) {
            arrayList.add(((char) i) + "");
        }
        mListView = (NoScrollListView) view.findViewById(R.id.list_fragment);
        ItemAdapter itemAdapter = new ItemAdapter();

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, arrayList);
        mListView.setAdapter(itemAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    for (int i = 'A'; i <= 'Z'; i++) {
                        arrayList.add(((char) i) + "");
                    }
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    class ItemAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager, null);
                holder = new ViewHolder();
                holder.mTitle = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(holder);
                Timber.i("createView----->" + position);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTitle.setText(arrayList.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView mTitle;
        }
    }
}
