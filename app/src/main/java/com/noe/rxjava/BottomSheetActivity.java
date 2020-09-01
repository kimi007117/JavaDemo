package com.noe.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;
import com.noe.rxjava.widget.TestBottomSheetLayout;

@Route(path = ArouterUtils.ACTIVITY_BOTTOM_SHEET)
public class BottomSheetActivity extends BaseActivity {

    TestBottomSheetLayout sheetLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        sheetLayout = findViewById(R.id.view_sheet);
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

    }

    private void showBottomSheet() {
        sheetLayout.show();
    }
}
