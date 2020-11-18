package com.noe.rxjava;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 58 on 2016/8/24. 分发页面
 */
@Route(path = ArouterUtils.ACTIVITY_SECOND)
public class SecondActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private Context mContext;
    private ArrayList<String> mArrayLists;
    private SecondAdapter mSecondAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_second);
        mContext = this;
        String[] mStrings = getResources().getStringArray(R.array.second_list);
        mArrayLists = new ArrayList<>(Arrays.asList(mStrings));

        mRecyclerView = (RecyclerView) findViewById(R.id.r_recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mSecondAdapter = new SecondAdapter();
        mRecyclerView.setAdapter(mSecondAdapter);


    }


    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String msg) {
        mArrayLists.set(0, msg);
        mSecondAdapter.notifyItemChanged(0);
    }

    private class SecondAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_second, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.button.setText(mArrayLists.get(position));
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            Intent intent = new Intent(SecondActivity.this, EventBusActivity.class);
                            startActivityForResult(intent, 1);
                            break;
                        case 1:
                            try {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(SecondActivity.this, "不支持定位", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            Bundle bundle = new Bundle();
                            bundle.putString("ha", "haha");
                            Intent intent1 = new Intent(SecondActivity.this, GridViewActivity.class);
                            intent1.putExtras(bundle);
                            break;
                        case 3:
                            startActivity(new Intent(SecondActivity.this, ViewPagerActivity.class));
                            break;
                        case 4:
                            startActivity(new Intent(SecondActivity.this, SmoothBarActivity.class));
                            break;
                        case 5:
                            startActivity(new Intent(SecondActivity.this, RecyclerActivity.class));
                            break;
                        case 6:
                            startActivity(new Intent(SecondActivity.this, StaggeredGridActivity.class));
                            break;
                        case 7:
                            Intent intent2 = getPackageManager().getLaunchIntentForPackage("com.ganji.android");
                            intent2.setData(Uri.parse("ganji://postdetail?data=%7B%22url%22%3A%22postdetail%22%2C%22cParam%22%3A%7B%22fromurl%22%3A%22http%3A%2F%2F3g.ganji.com%2Fbj_zpjigongyibangongren%2F99095303x%3Ffrom%3Dsinglemessage%22%2C%22from%22%3A%22weixin%22%2C%22fromposition%22%3A%22wap%22%7D%2C%22bParam%22%3A%7B%22category_id%22%3A%222%22%2C%22puid%22%3A%2299095303%22%7D%2C%22root_url%22%3A%22http%3A%2F%2Fsta.ganji.com%2Fatt%2Fproject%2Ftouch%2Fdownload_app%2Findex.html%3F%22%2C%22arg%22%3A%22http%3A%2F%2Fganji.cn%2Ft%2Fedetz9%22%2C%22schemaParm%22%3A%22ganji%3A%2F%2Fpostdetail%3F%22%2C%22other_url%22%3A%22https%3A%2F%2Fapplesite.ganji.com%22%7D"));
                            startActivity(intent2);
                            finish();
                            break;
                        case 8:
                            Intent intent3 = new Intent(Intent.ACTION_MAIN);
                            intent3.addCategory(Intent.CATEGORY_LAUNCHER);
                            ComponentName cn = new ComponentName("com.ganji.android", "com.ganji.android.wxapi.WXEntryActivity");
                            intent3.setComponent(cn);
                            intent3.setData(Uri.parse("ganji://postdetail?data=%7B%22url%22%3A%22postdetail%22%2C%22cParam%22%3A%7B%22fromurl%22%3A%22http%3A%2F%2F3g.ganji.com%2Fbj_zpjigongyibangongren%2F99095303x%3Ffrom%3Dsinglemessage%22%2C%22from%22%3A%22weixin%22%2C%22fromposition%22%3A%22wap%22%7D%2C%22bParam%22%3A%7B%22category_id%22%3A%222%22%2C%22puid%22%3A%2299095303%22%7D%2C%22root_url%22%3A%22http%3A%2F%2Fsta.ganji.com%2Fatt%2Fproject%2Ftouch%2Fdownload_app%2Findex.html%3F%22%2C%22arg%22%3A%22http%3A%2F%2Fganji.cn%2Ft%2Fedetz9%22%2C%22schemaParm%22%3A%22ganji%3A%2F%2Fpostdetail%3F%22%2C%22other_url%22%3A%22https%3A%2F%2Fapplesite.ganji.com%22%7D"));
                            startActivity(intent3);
                            finish();
                            break;
                        case 9:
                            startActivity(new Intent(SecondActivity.this, DaggerActivity.class));
                            break;
                        case 10:
                            Intent intentCamera = new Intent(); //调用照相机
                            intentCamera.setAction("android.media.action.STILL_IMAGE_CAMERA");
                            startActivity(intentCamera);
                            break;
                        case 11:
                            Toast.makeText(SecondActivity.this, getVersionName(SecondActivity.this), Toast.LENGTH_SHORT).show();
                            break;
                        case 12:
                            startActivity(new Intent(SecondActivity.this, VLayoutActivity.class));
                            break;
                        case 13:
                            startActivity(new Intent(SecondActivity.this, ConstraintLayoutActivity.class));
                            break;
                        case 14:
                            startActivity(new Intent(SecondActivity.this, GradientActivity.class));
                            break;
                        case 15:
                            startActivity(new Intent(SecondActivity.this, IntroActivity.class));
                            break;
                        case 16:
                            startActivity(new Intent(SecondActivity.this, Intro2Activity.class));
                            break;
                        case 17:
                            startActivity(new Intent(SecondActivity.this, FlowActivity.class));
                            break;
                        case 18:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_DOUBAN).navigation();
                            break;
                        case 19:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_XRECYCLER).navigation();
                            break;
                        case 20:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_XRECYCLER).navigation();
                            break;
                        case 21:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_PARALLAX).navigation();
                        case 22:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_PULL).navigation();
                            break;
                        case 23:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_JNI).navigation();
                            break;
                        case 24:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_VIEWFLIPPER).navigation();
                            break;
                        case 25:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_TOP_LIST).navigation();
                            break;
                        case 26:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_AUTO_TEXT).navigation();
                            break;
                        case 27:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_POP).navigation();
                            break;
                        case 28:
                            try {
                                Intent schemelIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("gjlogin://login?data=345"));
                                startActivityForResult(schemelIntent, 2);
                            } catch (Exception e) {
                                Toast.makeText(SecondActivity.this, "ActivityNotFoundException", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 29:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_LAUNCH).navigation();
                            break;
                        case 30:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_SLIDE).navigation();
                            break;
                        case 31:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_DIVIDER).navigation();
                            break;
                        case 32:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_GLIDE).navigation();
                            break;
                        case 33:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_FACE_DETECTOR).navigation();
                            break;
                        case 34:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_MARQUEE).navigation();
                            break;
                        case 35:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_HEADER_FOOTER).navigation();
                            break;
                        case 36:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_CONTACTS).navigation();
                            break;
                        case 37:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_ADDVIEW).navigation();
                            break;
                        case 38:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_TOPTOAST).navigation();
                            break;
                        case 39:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_BLOCKTAB).navigation();
                            break;
                        case 40:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_DIALOG).navigation();
                            break;
                        case 41:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_SVG).navigation();
                            break;
                        case 42:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_BANNER).navigation();
                            break;
                        case 43:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_TAB).navigation();
                            break;
                        case 44:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_TAB2).navigation();
                            break;
                        case 45:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_FRAME).navigation();
                            break;
                        case 46:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_COLLAPSING).navigation();
                            break;
                        case 47:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_POSTER).navigation();
                            break;
                        case 48:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_COMPOSE).navigation();
                            break;
                        case 49:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_DEMO_TAB).navigation();
                            break;
                        case 50:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_BOTTOM_NAVIGATION).navigation();
                            break;
                        case 51:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_NESTEDSCROLLVIEW_NAVIGATION).navigation();
                            break;
                        case 52:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_TEST).navigation();
                            break;
                        case 53:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_BOTTOM_SHEET_DIALOG).navigation();
                            break;
                        case 54:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_BOTTOM_SHEET).navigation();
                            break;
                        case 55:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_MAP).navigation();
                            break;
                        case 56:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_SLIDING_PANEL).navigation();
                            break;
                        case 57:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_RHEADER).navigation();
                            break;
                        case 58:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_PROXY).navigation();
                            break;
                        case 59:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_WAVE).navigation();
                            break;
                        case 60:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_SERVICE).navigation();
                            break;
                        case 61:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_POPUP2).navigation();
                            break;
                        case 62:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_POPUP3).navigation();
                            break;
                        case 63:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_WEBVIEW).navigation();
                            break;
                        case 64:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_WEBVIEW2).navigation();
                            break;
                        case 65:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_NETWORK).navigation();
                            break;
                        case 66:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_HOLDER).navigation();
                            break;
                        case 67:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_TEST_FRAGMENT).navigation();
                            break;
                        case 68:
                            ARouter.getInstance().build(ArouterUtils.ACTIVITY_TOOLBAR).navigation();
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayLists.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Toast.makeText(SecondActivity.this, "认证成功" + data.getStringExtra("token"), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SecondActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button button;

        ViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.btn_second);
        }
    }

}
