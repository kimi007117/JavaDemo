package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;

@Route(path = ArouterUtils.ACTIVITY_BEHAVIOR)
public class BehaviorActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Context mContext;

    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        for (int i = 'A'; i <= 'Z'; i++) {
            arrayList.add(((char) i) + "");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);


        MyRecyclerAdapter adapter = new MyRecyclerAdapter(arrayList);
        mRecyclerView.setAdapter(adapter);
    }


    class MyRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<String> mArrayList;


        public MyRecyclerAdapter(ArrayList<String> arrayList) {
            this.mArrayList = arrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(arrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.tv_recycler);
        }
    }
}
