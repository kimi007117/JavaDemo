package com.noe.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

/**
 * Created by lijie on 2020-06-12.
 */
@Route(path = ArouterUtils.ACTIVITY_WEBVIEW)
public class WebViewActivity extends BaseActivity {

    private WebView mWebView;
    private static final String URL = "https://www.jianshu.com/p/eccfda6f39c9";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = findViewById(R.id.webView);
        mWebView.loadUrl(URL);
    }
}
