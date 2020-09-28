package com.noe.rxjava;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.ui.widget.WaveView;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

/**
 * Created by lijie on 2020-04-28.
 */
@Route(path = ArouterUtils.ACTIVITY_WAVE)
public class WaveActivity extends BaseActivity {


    private WaveView wave;
    private TextView edit;
    private int currentVolume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        wave = findViewById(R.id.main_wave);
        edit = findViewById(R.id.main_edit);
        findViewById(R.id.main_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentVolume < 30) {
                    currentVolume++;
                }
                wave.setVolume(currentVolume);
                edit.setText("当前音量：" + currentVolume);
            }
        });
        findViewById(R.id.main_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wave.startAnim();
            }
        });

        findViewById(R.id.main_subtract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentVolume > 0) {
                    currentVolume--;
                }
                wave.setVolume(currentVolume);
                edit.setText("当前音量：" + currentVolume);
            }
        });
    }
}
