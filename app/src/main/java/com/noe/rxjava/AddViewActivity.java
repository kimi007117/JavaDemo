package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseListAdapter;
import com.noe.rxjava.base.BaseViewHolder;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;

/**
 * Created by lijie on 2019/3/13.
 */
@Route(path = ArouterUtils.ACTIVITY_ADDVIEW)
public class AddViewActivity extends AppCompatActivity {

    private Context mContext;

    private ListView mListView;
    private ArrayList<String> mArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view);
        mContext = this;
        mListView = findViewById(R.id.listView);
        initData();
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        mArrayList.add(4,"哈哈");
        ItemAdapter itemAdapter = new ItemAdapter(mContext);
        itemAdapter.setData(mArrayList);
        mListView.setAdapter(itemAdapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            String s = mArrayList.get(position);
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
        });
    }

    class ItemAdapter extends BaseListAdapter<String> {

        ItemAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager, null);
            return new ViewHolder(inflate);
        }

        class ViewHolder extends BaseViewHolder<String> {
            TextView mTitle;

            public ViewHolder(@NonNull View view) {
                super(view);
                mTitle = view.findViewById(R.id.title);
            }

            @Override
            public void onBind(String data, int position) {
                super.onBind(data, position);
                if (TextUtils.isEmpty(data)) {
                    return;
                }
                mTitle.setText(data);
            }
        }
    }
}
