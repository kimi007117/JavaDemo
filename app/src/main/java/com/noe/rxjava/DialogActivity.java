package com.noe.rxjava;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.dialog.CommentDialog;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;
import java.util.List;

@Route(path = ArouterUtils.ACTIVITY_DIALOG)
public class DialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        List<String> list = new ArrayList<>();
        list.add("不了解此功能怎么用");
        list.add("系统回复内容不准确");
        list.add("我更想亲自自己回复");
        list.add("暂时无招聘需求");
        list.add("其他原因");
        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog dialog = new CommentDialog(mContext, list);
                dialog.show();
            }
        });
    }
}
