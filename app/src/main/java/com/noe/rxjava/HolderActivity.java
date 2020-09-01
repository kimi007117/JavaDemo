package com.noe.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.bean.User;
import com.noe.rxjava.holder.TestHolder;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijie on 2020/8/8.
 */
@Route(path = ArouterUtils.ACTIVITY_HOLDER)
public class HolderActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

        List<User> list = new ArrayList<>();
        User user;

        user = new User();
        user.name = "张一";
        user.phone = "12";
        list.add(user);

        user = new User();
        user.name = "张二";
        user.phone = "2";
        list.add(user);

        user = new User();
        user.name = "张三";
        user.phone = "31";
        list.add(user);

        user = new User();
        user.name = "张四";
        user.phone = "45";
        list.add(user);

        user = new User();
        user.name = "张五";
        user.phone = "89";
        list.add(user);

        TestHolder testHolder = TestHolder.create(mContext, list);
        testHolder.bind(findViewById(R.id.layout_content));


    }





}
