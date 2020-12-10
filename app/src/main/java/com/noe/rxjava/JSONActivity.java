package com.noe.rxjava;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lijie on 2020/12/5.
 */
@Route(path = ArouterUtils.ACTIVITY_JSON)
public class JSONActivity extends BaseActivity {

    private TextView mTvSource;
    private TextView mTvResult;
    private Button mBtnToString;
    private Button mBtnJson1;
    private Button mBtnJson2;
    private String jsonStr = "\n" +
            "{\n" +
            "    \"id\": \"7eh93jh92sbd\",\n" +
            "    \"style\": \"card\",\n" +
            "    \"content\": {\n" +
            "        \"type\": \"list\",\n" +
            "        \"url\": \"https://www.baidu.com/api\",\n" +
            "        \"title\": \"标题\",\n" +
            "        \"subTitle\": \"可乐/雪碧\",\n" +
            "        \"params\": \"[{\\\"url\\\":\\\"https://www.baidu.com/list\\\",\\\"cateID\\\":\\\"6693\\\",\\\"quantity\\\":0,\\\"tagID\\\":\\\"8\\\",\\\"tagName\\\":\\\"可乐/雪碧\\\"}]\"\n" +
            "    }\n" +
            "}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        mTvSource = findViewById(R.id.tv_source);
        mTvResult = findViewById(R.id.tv_result);
        mBtnToString = findViewById(R.id.btn_to_string);
        mBtnJson1 = findViewById(R.id.btn_to_json1);
        mBtnJson2 = findViewById(R.id.btn_to_json2);
        initData();
    }

    private void initData() {
        mTvSource.setText(jsonStr);
        mBtnToString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    mTvResult.setText(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mBtnJson1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    StringBuilder sb = new StringBuilder();
                    sb.append(jsonObject.optString("id")).append("\n");
                    sb.append(jsonObject.optString("style")).append("\n");
                    JSONObject jsonObject1 = jsonObject.optJSONObject("content");

                    sb.append(jsonObject1.optString("type")).append("\n");
                    sb.append(jsonObject1.optString("url")).append("\n");
                    sb.append(jsonObject1.optString("title")).append("\n");
                    sb.append(jsonObject1.optString("subTitle")).append("\n");
                    sb.append(jsonObject1.optString("params")).append("\n");
                    mTvResult.setText(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mBtnJson2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    StringBuilder sb = new StringBuilder();
                    sb.append(jsonObject.optString("id")).append("\n");
                    sb.append(jsonObject.optString("style")).append("\n");
                    JSONObject jsonObject1 = jsonObject.optJSONObject("content");

                    sb.append(jsonObject1.optString("type")).append("\n");
                    sb.append(jsonObject1.optString("url")).append("\n");
                    sb.append(jsonObject1.optString("title")).append("\n");
                    sb.append(jsonObject1.optString("subTitle")).append("\n");

                    String params = jsonObject1.optString("params");

                    JSONArray jsonObject2 = new JSONArray(params);

//                    sb.append(jsonObject2.optString("cateID")).append("\n");
//                    sb.append(jsonObject2.optString("quantity")).append("\n");
//                    sb.append(jsonObject2.optString("tagID")).append("\n");
//                    sb.append(jsonObject2.optString("tagName")).append("\n");


                    sb.append(jsonObject2.toString().replace("\\/","/")).append("\n");

                    mTvResult.setText(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
