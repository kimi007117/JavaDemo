package com.noe.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;
import com.noe.rxjava.view.CustomPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ArouterUtils.ACTIVITY_POP)
public class PopupActivity extends AppCompatActivity {

    @BindView(R.id.et_pop)
    EditText mEditText;
    @BindView(R.id.ll_pop)
    LinearLayout mLinearLayout;
    private CustomPopupWindow mCustomPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        ButterKnife.bind(this);
        mCustomPopupWindow = new CustomPopupWindow(this);
        mEditText.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mCustomPopupWindow.showAsDropDown(mLinearLayout);
        }
    };
}
