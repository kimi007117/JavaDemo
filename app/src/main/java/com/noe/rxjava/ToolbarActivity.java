package com.noe.rxjava;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.core.util.Logger;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.image.ImageLoaderManager;
import com.noe.rxjava.util.ArouterUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lijie on 2020/11/18.
 */
@Route(path = ArouterUtils.ACTIVITY_TOOLBAR)
public class ToolbarActivity extends BaseActivity {

    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mImageView;
    private String pic = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1091118290,1020273292&fm=26&gp=0.jpg";
    private String json = "{\n" +
            "    \"id\": \"7eh93jh92sbd\",\n" +
            "    \"style\": \"card\",\n" +
            "    \"content\": {\n" +
            "        \"type\": \"list\",\n" +
            "        \"url\": \"https://www.baidu.com/api\",\n" +
            "        \"title\": \"标题\",\n" +
            "        \"subTitle\": \"可乐/雪碧\",\n" +
            "        \"params\": {\n" +
            "            \"pid\": \"103500451210642\",\n" +
            "            \"tagName\":\"可乐/雪碧\",\n" +
            "            \"subjectId\": \"2169090\"\n" +
            "        }\n" +
            "    }\n" +
            "}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar);
        initView();
    }

    private void initView() {
        mAppBarLayout = findViewById(R.id.app_bar);
        mCollapsingToolbarLayout = findViewById(R.id.collapse_layout);
        mToolbar = findViewById(R.id.tool_bar);
        mImageView = findViewById(R.id.iv_banner);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.icon_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("详情界面");
        String str = "";
        Uri uri = Uri.parse(str);
        String path = uri.getEncodedPath();
        ImageLoaderManager.getInstance().showImage(ImageLoaderManager.getDefaultOptions(mImageView, pic));

        try {
            JSONObject jsonObject = new JSONObject(json);
            String s = jsonObject.optString("subTitle");

            Logger.i("xxxxxxxxxxx",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
