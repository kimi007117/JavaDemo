package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.refresh.SmartRefreshLayout;
import com.noe.rxjava.refresh.api.RefreshLayout;
import com.noe.rxjava.refresh.listener.OnRefreshListener;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;

import timber.log.Timber;

@Route(path = ArouterUtils.ACTIVITY_DIVIDER)
public class DividerActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private Context mContext;
    private View mHeaderView;
    private View mFooterView;
    SmartRefreshLayout mSmartRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divider);
        mContext = this;
        mSmartRefreshLayout = findViewById(R.id.smart_refresh);
        mListView = (ListView) findViewById(R.id.list_divider);
        for (int i = 'A'; i <= 'Z'; i++) {
            arrayList.add(((char) i) + "");
        }
        ItemAdapter itemAdapter = new ItemAdapter();
        mListView.setAdapter(itemAdapter);

        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.header_list, null);
        mListView.addHeaderView(mHeaderView, null, true);
        mListView.setHeaderDividersEnabled(false);

        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.footer_fragment, null);

        mListView.addFooterView(mFooterView);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_footer, PageFragment.newInstance(0));
//                fragmentTransaction.commitAllowingStateLoss();
//            }
//        },5000);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmartRefreshLayout.finishRefresh(2000);
            }
        });
    }

    private class ItemAdapter extends BaseAdapter {

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
