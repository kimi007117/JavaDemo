package com.noe.rxjava;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;

/**
 * Created by 58 on 2016/8/5.
 */
@Route(path = ArouterUtils.ACTIVITY_BAR)
public class BarActivity extends BaseActivity {

    private ListView mListViewBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatus();
        setContentView(R.layout.activity_bar);
        //给页面设置工具栏
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置工具栏标题
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle("cheeseName");

        mListViewBar = (ListView) findViewById(R.id.listView_bar);
        ArrayList<String> arrayList = new ArrayList<>();
        String mString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < mString.length(); i++) {
            arrayList.add(String.valueOf(mString.charAt(i)));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(BarActivity.this, android.R.layout.simple_list_item_1, arrayList);
        mListViewBar.setAdapter(arrayAdapter);

    }

}
