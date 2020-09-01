package com.noe.rxjava;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.ui.util.Navigation;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.constant.Constants;
import com.noe.rxjava.dialog.BottomDialog;
import com.noe.rxjava.dialog.CommentDialog;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Route(path = ArouterUtils.ACTIVITY_BOTTOM_SHEET_DIALOG)
public class BottomSheetDialogActivity extends BaseActivity {

    private TextView mTvContent;
    private LinearLayout rootViewGroup;
    private CheckBox mCheckBox;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_dialog);
        rootViewGroup = findViewById(R.id.root_view);
        Navigation.transparencyBar(this);
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mTvContent = findViewById(R.id.tv_content);
        mCheckBox = findViewById(R.id.cb_check);
        Constants.test = 299;
        mList.add("1");
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mList.remove("9");
                Toast.makeText(mContext, mList.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        setData();
    }

    private void setClickableSpan(final SpannableStringBuilder clickableHtmlBuilder, final Matcher matcher) {
        int start = matcher.start();
        int end = matcher.end();
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View view) {
                String result = getResources().getString(R.string.TAG);
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, TestActivity.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = new ArrayList<>();
                        list.add("不了解此功能怎么用");
                        list.add("系统回复内容不准确");
                        list.add("我更想亲自自己回复");
                        list.add("暂时无招聘需求");
                        list.add("其他原因");
                        CommentDialog dialog = new CommentDialog(mContext, list);
                        dialog.show();
                    }
                }, 2000);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.RED);
                ds.setUnderlineText(true);
                ds.clearShadowLayer();
            }
        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    private void showDialog() {
//        BaseBottomDialog bottomSheetDialog = new BaseBottomDialog(this);
//        bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet);
//        bottomSheetDialog.show();
        BottomDialog dialog = new BottomDialog(mContext);
        dialog.show();
    }

    private void setData() {

        String html = "欢迎来到xxxxAPP！<p>";
        html += "为保障您的权益，请您务必仔细阅读<i><b>《使用协议》</b></i>与<i><b>《隐私政策》</b></i>，理解并同意接受全部条款后再开始使用我们的产品和服务。<p>";
        html += "APP将一如既往坚守使命！<p>";

        CharSequence charSequence = Html.fromHtml(html);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(charSequence);

        Pattern pattern = Pattern.compile("《使用协议》");//根据正则匹配出带有超链接的文字
        Matcher matcher = pattern.matcher(stringBuilder);
        if (matcher.find()) {
            setClickableSpan(stringBuilder, matcher);
        }

        mTvContent.setText(stringBuilder);
        //该语句在设置后必加，不然没有任何效果
        mTvContent.setMovementMethod(LinkMovementMethod.getInstance());

    }

}
