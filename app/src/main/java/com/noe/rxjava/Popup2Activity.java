package com.noe.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.popup.CustomPopup2;
import com.noe.rxjava.popup.HorizontalPosition;
import com.noe.rxjava.popup.SmartPopupWindow;
import com.noe.rxjava.popup.VerticalPosition;
import com.noe.rxjava.util.ArouterUtils;

/**
 * Created by lijie on 2020-05-25.
 */
@Route(path = ArouterUtils.ACTIVITY_POPUP2)
public class Popup2Activity extends BaseActivity {

    CustomPopup2 customPopup2;
    LinearLayout bottomLayout;
    private View mPopupContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_popup);
        bottomLayout = findViewById(R.id.btn_click);
        View background = findViewById(R.id.view_background);
        customPopup2 = new CustomPopup2(mContext);
        mPopupContentView = getLayoutInflater().inflate(R.layout.popup_view2, null);

        View contentView = customPopup2.getContentView();


        //需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(makeDropDownMeasureSpec(customPopup2.getWidth()), makeDropDownMeasureSpec(customPopup2.getHeight()));


        bottomLayout.setOnClickListener(v -> {

//            showPopup();
//            background.setVisibility(View.VISIBLE);
            customPopup2.showOnAnchor(bottomLayout, VerticalPosition.ABOVE, HorizontalPosition.CENTER,true);
//            int offsetY = -(ScreenUtils.getScreenHeight(mContext)-bottomLayout.getHeight());
//            customPopup2.showAtLocation(bottomLayout, Gravity.TOP, 0, 0);
//            PopupWindowCompat.showAsDropDown(customPopup2,bottomLayout,0,offsetY,Gravity.START);
//            customPopup2.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
////                    background.setVisibility(View.GONE);
//                }
//            });


        });
    }

    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    private void showPopup() {
        SmartPopupWindow.Builder
                .build(Popup2Activity.this, mPopupContentView)
                .setAlpha(0.3f)
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                .createPopupWindow()
                .showAtAnchorView(bottomLayout, VerticalPosition.ABOVE, HorizontalPosition.CENTER);
//        PopupWindowCompat.showAsDropDown(customPopup2, bottomLayout, 0, -(customPopup2.getContentView().getMeasuredHeight() + bottomLayout.getHeight()), Gravity.START);
    }
}
