package com.noe.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;
import com.android.core.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ArouterUtils.ACTIVITY_JNI)
public class JNIActivity extends AppCompatActivity {
    @BindView(R.id.tv_jni)
    TextView mTextViewJni;
    @BindView(R.id.tv_java_md5)
    TextView mTextViewJavaMd5;
    @BindView(R.id.tv_jni_md5)
    TextView mTextViewJniMd5;
    @BindView(R.id.tv_sub)
    TextView mTextViewSub;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
        ButterKnife.bind(this);
        mTextViewJni.setText(encrypt("a=杜翔&b=11221&c=dddd"));
        mTextViewJavaMd5.setText(StringUtils.MD5("abc"));
        mTextViewJniMd5.setText(md5("abc"));
        mTextViewSub.setText(sub("abcdefghijklmn"));
    }


    private native String decrypt(String data);

    private native String encrypt(String data);

    private native String md5(String data);

    private native String sub(String data);

}
