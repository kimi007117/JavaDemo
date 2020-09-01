package com.noe.rxjava;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

/**
 * Created by 58 on 2016/8/5.
 */
@Route(path = ArouterUtils.ACTIVITY_BAR)
public class BarActivity extends AppCompatActivity {

    private ListView mListViewBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        setStatusBarColorForKitkat();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void setStatusBarColorForKitkat() {
        /**
         * Android4.4
         */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(getResources().getColor(R.color.colorPrimary));
        }
    }
}
