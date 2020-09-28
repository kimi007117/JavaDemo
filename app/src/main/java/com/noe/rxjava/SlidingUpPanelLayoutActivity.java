package com.noe.rxjava;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.core.util.Logger;
import com.android.ui.widget.SlidingUpPanelLayout;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

/**
 * Created by lijie on 2020/3/23.
 */
@Route(path = ArouterUtils.ACTIVITY_SLIDING_PANEL)
public class SlidingUpPanelLayoutActivity extends BaseActivity {

    SlidingUpPanelLayout slidingUpPanelLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_panel);
        hideIMSoftKeyboard();
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Logger.i("xxxxxx--onPanelStateChanged", previousState.toString() + "---" + newState.toString());
//                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当按下返回键时所执行的命令
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 此处写你按返回键之后要执行的事件的逻辑
            SlidingUpPanelLayout.PanelState panelState = slidingUpPanelLayout.getPanelState();
            Logger.i("xxxxxx", panelState.toString());
            if (panelState == SlidingUpPanelLayout.PanelState.ANCHORED
                    || panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);


    }
}
