package com.noe.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.core.util.Logger;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

import java.util.Locale;

/**
 * Created by lijie on 2019-11-22.
 */
@Route(path = ArouterUtils.ACTIVITY_TEST)
public class TestActivity extends BaseActivity {

    private TextView mTextView1;
    private TextView mTextView2;
    private static final int AA = 0X1;
    private static final int BB = 0X2;
    private static final int CC = 0X3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initView() {
        String message1 = "10秒快速发布职位\n<u><font color = '#ff704f'>8</font></u>小时内马上招到人";
        mTextView1 = findViewById(R.id.tv_text1);
        mTextView2 = findViewById(R.id.tv_text2);
        mTextView1.setText(Html.fromHtml(message1.replace("\n", "<br>")));

        String tips = getResources().getString(R.string.test_tips);
        mTextView2.setText(Html.fromHtml(String.format(Locale.getDefault(), tips, 10)));


//        Toast.makeText(mContext, getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

        String str1 = null;
        String str2 = "xxxx";
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.equals(str1,str2)) {
//                    Toast.makeText(mContext, "equals", Toast.LENGTH_SHORT).show();
//                } else {
//                    String processName = AndroidUtils.getProcessName(mContext);
//                    Toast.makeText(mContext, processName, Toast.LENGTH_SHORT).show();
//                }

                Logger.i("xxxxxxxx",(1 == AA) + "");
                Logger.i("xxxxxxxx",(4 == BB) + "");
                Logger.i("xxxxxxxx",(3 == CC) + "");
            }
        });

    }
}
