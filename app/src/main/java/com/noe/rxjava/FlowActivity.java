package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noe.rxjava.view.FlowLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;

public class FlowActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Context mContext;
    private ArrayList<String> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        mContext = this;
        mArrayList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.second_list)));
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_flow);
        mRecyclerView.setLayoutManager(new FlowLayoutManager());
        mRecyclerView.setAdapter(new FlowAdapter());

    }

    class FlowAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.item_flow, parent, false);
            return new ViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_flow);
        }


    }
}
