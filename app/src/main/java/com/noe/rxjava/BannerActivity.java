package com.noe.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;
import com.noe.rxjava.window.BannerController;

import java.util.ArrayList;
import java.util.List;


@Route(path = ArouterUtils.ACTIVITY_BANNER)
public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        LinearLayout linearLayout = findViewById(R.id.ll_dot_container);
        BannerController bannerController = new BannerController(this,recyclerView,linearLayout);

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.banner);
        list.add(R.drawable.banner);
        list.add(R.drawable.banner);
        list.add(R.drawable.banner);
        bannerController.setAdapter(list);
    }





}
