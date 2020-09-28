package com.noe.rxjava;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;

@Route(path = ArouterUtils.ACTIVITY_SVG)
public class SvgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
    }
}
