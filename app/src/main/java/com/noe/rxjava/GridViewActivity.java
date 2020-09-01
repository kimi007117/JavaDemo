package com.noe.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.noe.rxjava.view.GridViewShowAll;

import java.util.ArrayList;

/**
 * Created by 58 on 2016/9/20.
 */
public class GridViewActivity extends AppCompatActivity {
    private ListView mListView;
    private GridViewShowAll mGridViewShowAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        mListView = (ListView) findViewById(R.id.listView);
        View header = getLayoutInflater().inflate(R.layout.gridheader, null);
        mGridViewShowAll = (GridViewShowAll) header.findViewById(R.id.gridview);


        ArrayList<String> arrayList = new ArrayList<>();
        String mString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < mString.length(); i++) {
            arrayList.add(String.valueOf(mString.charAt(i)));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GridViewActivity.this, android.R.layout.simple_list_item_1, arrayList);
        mListView.setAdapter(arrayAdapter);

        mGridViewShowAll.setAdapter(arrayAdapter);

        mListView.addHeaderView(header);

        Toast.makeText(this,getIntent().getStringExtra("ha"),Toast.LENGTH_LONG).show();

        mGridViewShowAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GridViewActivity.this,arrayList.get(position),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface OnClickCityListener{
        void OnItemClickListener();
    }
}
