package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseListAdapter;
import com.noe.rxjava.base.BaseViewHolder;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;
import java.util.List;

@Route(path = ArouterUtils.ACTIVITY_HEADER_FOOTER)
public class HeaderActivity extends AppCompatActivity {

    private Context mContext;

    private ListView mListView;

    private List<String> mStrings = new ArrayList<>();

    private View mHeaderView;

    private View mFooterView;

    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_header);
        mListView = findViewById(R.id.listView);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_view, null);
        mFooterView = LayoutInflater.from(this).inflate(R.layout.footer_view, null);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 26; i++) {
            mStrings.add(String.valueOf(i));
        }

        adapter = new MainAdapter(this);
        mListView.addHeaderView(mHeaderView);
        mListView.addFooterView(mFooterView);
        adapter.setOnChangeListener(this::updateItem);
        adapter.setData(mStrings);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - mListView.getHeaderViewsCount();
            if (index < 0 || index >= adapter.getCount()) {
                return;
            }
            Toast.makeText(mContext, mStrings.get(index), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateItem(String str, int position) {
        adapter.notifyItemChanged(mListView, str, position);
    }

    private class MainAdapter extends BaseListAdapter<String> {
        OnChangeListener mListener;

        MainAdapter(Context context) {
            super(context);
        }

        public void setOnChangeListener(OnChangeListener listener) {
            this.mListener = listener;
        }


        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = mInflater.inflate(R.layout.item_string, parent, false);
            return new MainHolderView(convertView);
        }

        class MainHolderView extends BaseViewHolder<String> {

            TextView textView;
            Button btnChange;

            MainHolderView(@NonNull View view) {
                super(view);
                textView = view.findViewById(R.id.text);
                btnChange = view.findViewById(R.id.btn_update);

            }

            @Override
            public void onBind(String data, int position) {
                super.onBind(data, position);
                textView.setText(data);
                btnChange.setOnClickListener(v -> mListener.onChange(data + "：update", position));
            }
        }
    }

    public interface OnChangeListener {
        void onChange(String str, int position);
    }
}
