package com.noe.rxjava;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;
import com.noe.rxjava.view.AutoScaleTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ArouterUtils.ACTIVITY_AUTO_TEXT)
public class AutoTextActivity extends AppCompatActivity {

    private ArrayList<String> arrayList = new ArrayList<>();
    @BindView(R.id.gridview)
    GridView mGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_text);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        arrayList.add("房屋");
        arrayList.add("北京");
        arrayList.add("出售房源由多");
        arrayList.add("出租房源由多到少");
        arrayList.add("乌鲁木齐");
        arrayList.add("出售房源由多到少哈哈哈哈哈哈");
        arrayList.add("五室一厅啊");
        arrayList.add("三室一厅一卫嗯");
        arrayList.add("别墅");
        arrayList.add("公寓");
        mGridView.setAdapter(new TextAdapter());
    }

    class TextAdapter extends BaseAdapter {

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
                convertView = View.inflate(AutoTextActivity.this, R.layout.item_auto_text, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String str = arrayList.get(position);
//            if (str.length()>7){
//                holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
//            }else {
//                holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
//            }
            holder.title.setText(str);
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_title)
            AutoScaleTextView title;

            ViewHolder(View itemView) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
