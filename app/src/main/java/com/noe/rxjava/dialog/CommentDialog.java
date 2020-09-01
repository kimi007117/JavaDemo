package com.noe.rxjava.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseHeaderAndFooterRecyclerAdapter;
import com.noe.rxjava.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijie on 2019-06-19.
 */
public class CommentDialog extends Dialog {

    private Context mContext;
    private ImageButton mBtnClose;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private TextView mTvCommentLength;
    private Button mBtnSubmit;
    private RelativeLayout mEditLayout;
    private ImageButton mArrow;
    private InputMethodManager mInputManager;

    private ArrayList<String> mList;

    private String mString;

    public CommentDialog(Context context, List<String> list) {
        super(context, R.style.dialog_goku);
        mInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        setCancelable(false);
        mContext = context;
        this.mList = (ArrayList<String>) list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        initView();
        initData();
    }

    private void initView() {
        mBtnClose = findViewById(R.id.btn_close);
        mEditLayout = findViewById(R.id.edit_layout);
        mRecyclerView = findViewById(R.id.recycle_view);
        mArrow = findViewById(R.id.btn_arrow);
        mEditText = findViewById(R.id.et_content);
        mEditText.addTextChangedListener(editWatcher);
        mBtnSubmit = findViewById(R.id.btn_submit);
        findViewById(R.id.btn_other).setSelected(true);
        mArrow.setOnClickListener(v -> {
            hideSoftInput();
            mEditLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
//            translateIn(mRecyclerView);
        });
        mBtnClose.setOnClickListener(v -> dismiss());
        mTvCommentLength = findViewById(R.id.tv_content_length);
        mBtnSubmit.setOnClickListener(v -> doSubmit());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private TextWatcher editWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mTvCommentLength.setText(s.length() + "/100");
            updateView();
        }
    };

    private void initData() {
        mTvCommentLength.setText("0/100");
        CommentAdapter adapter = new CommentAdapter(mContext);
        adapter.addData(mList);
        adapter.setOnItemClickListener((view, position, data) -> {
            mString = data;
            updateView();
            return false;
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void translateIn(View view) {
        Animation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                0, Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0);
        translateAnimation.setDuration(300);//动画持续的时间为10s
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        view.setAnimation(translateAnimation);//给imageView添加的动画效果
        translateAnimation.startNow();//动画开始执行 放在最后即可
    }


    private void translateOut(View view) {
        Animation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, -1f);
        translateAnimation.setDuration(300);//动画持续的时间为10s
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        view.setAnimation(translateAnimation);//给imageView添加的动画效果
        translateAnimation.startNow();//动画开始执行 放在最后即可
    }

    private void translateEditLayoutIn(View view) {
        Animation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                0, Animation.RELATIVE_TO_PARENT, 1f,
                Animation.RELATIVE_TO_PARENT, 0);
        translateAnimation.setDuration(300);//动画持续的时间为10s
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        view.setAnimation(translateAnimation);//给imageView添加的动画效果
        translateAnimation.startNow();//动画开始执行 放在最后即可
    }

     private void translateEditLayoutOut(View view) {
        Animation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1f);
        translateAnimation.setDuration(300);//动画持续的时间为10s
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        view.setAnimation(translateAnimation);//给imageView添加的动画效果
        translateAnimation.startNow();//动画开始执行 放在最后即可
    }


    private void updateView() {
        if ("其他原因".equals(mString)) {
            mEditLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
//            translateOut(mRecyclerView);
            if (TextUtils.isEmpty(mEditText.getText().toString())){
                mBtnSubmit.setEnabled(false);
            } else {
                mBtnSubmit.setEnabled(true);
            }
        } else {
            mBtnSubmit.setEnabled(true);
        }
    }

    private void doSubmit() {
        hideSoftInput();
        String comment = mEditText.getText().toString();

    }


    private class CommentAdapter extends BaseHeaderAndFooterRecyclerAdapter<String> {

        private int selected = -1;

        CommentAdapter(Context context) {
            super(context);
        }

        private OnItemViewClickListener<String> mOnItemClickListener;

        public void setOnItemClickListener(OnItemViewClickListener<String> onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        private void setSelection(int position) {
            this.selected = position;
            notifyDataSetChanged();
        }

        @Override
        public BaseViewHolder doCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_comment, parent, false);
            return new ViewHolder(view);
        }

        class ViewHolder extends BaseViewHolder<String> {

            private TextView mTextView;

            ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.tv_title);
            }


            @Override
            public void onBind(String data, int position) {
                super.onBind(data, position);
                if (data == null) {
                    return;
                }
                mTextView.setText(data);
                if (selected == position) { // 选中后背景色改变
                    mTextView.setSelected(true);
                } else {
                    mTextView.setSelected(false);
                }
                itemView.setOnClickListener(v -> {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemViewClick(itemView, position, data);
                        setSelection(position);
                    }
                });
            }
        }
    }

    /**
     * 隐藏键盘
     */
    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public interface OnItemViewClickListener<T> {
        boolean onItemViewClick(View view, int position, T data);
    }

}
