package com.noe.rxjava;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseHeaderAndFooterRecyclerAdapter;
import com.noe.rxjava.base.BaseViewHolder;
import com.noe.rxjava.bean.Page;
import com.noe.rxjava.bean.User;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;
import java.util.List;

@Route(path = ArouterUtils.ACTIVITY_CONTACTS)
public class ContactsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Button mBtnAdd;
    private ContactsAdapter mAdapter;
    private int mPageIndex = 1;
    private int mPageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(v -> {
            mPageIndex++;
            List<User> contacts = getContacts(mPageIndex, mPageSize);
            mAdapter.addData(contacts);
        });

        initData();
    }

    private void initData() {
        mAdapter = new ContactsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        List<User> users = getContacts(mPageIndex, mPageSize);
        if (users != null && users.size() > 0) {
            mAdapter.setData(users);
        }
    }

    class ContactsAdapter extends BaseHeaderAndFooterRecyclerAdapter<User> {

        ContactsAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder<User> doCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = mInflater.inflate(R.layout.item_contacts, parent, false);
            return new ContactsViewHolder(convertView);
        }

        class ContactsViewHolder extends BaseViewHolder<User> {

            TextView mTextViewName;
            TextView mTextViewPhone;

            ContactsViewHolder(@NonNull View view) {
                super(view);
                mTextViewName = view.findViewById(R.id.text_name);
                mTextViewPhone = view.findViewById(R.id.text_phone);
            }

            @Override
            public void onBind(User data, int position) {
                super.onBind(data, position);
                mTextViewName.setText(data.name);
                mTextViewPhone.setText(data.phone);
            }
        }
    }

    public int getContactsCount() {
        int count = 0;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.DATA1,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        try {
            if (null != cursor) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * 分页查询系统联系人信息
     *
     * @param pageSize 每页最大的数目
     * @param page     页数
     * @return
     */
    public Page<List<User>> getContactsByPage(int page, int pageSize) {
        if (getContactsCount() == 0) {
            return null;
        }
        Page<List<User>> tempPage = new Page<>();
        tempPage.data = new ArrayList<>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        tempPage.count = getContactsCount();
        tempPage.pages = tempPage.count / pageSize + (tempPage.count % pageSize == 0 ? 0 : 1);
        if (page < 1) {
            page = 1;
        } else if (page > tempPage.pages) {
            page = tempPage.pages;
        }
        tempPage.page = page;
        tempPage.limit = pageSize <= 0 ? 10 : pageSize;
        int currentOffset = (tempPage.page - 1) * tempPage.limit;
        Cursor cursor = getContentResolver().query(uri, projection, null, null, ContactsContract.Contacts._ID + " ASC limit " + pageSize + " offset " + currentOffset);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                User info = new User();
                info.name = cursor.getString(0);
                info.phone = cursor.getString(1);
                info.id = cursor.getLong(2);

                tempPage.data.add(info);
                Log.e("ContactsPerson: ", info.toString());
            }
            Log.e("ContactsPerson: ", tempPage.toString());
            cursor.close();
        }
        return tempPage;
    }

    private List<User> getContacts(int page, int pageSize) {
        int count = getContactsCount();
        if (page <= 0 || pageSize <= 0 || count == 0) {
            return null;
        }

        List<User> list = new ArrayList<>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.DATA1,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        int currentOffset = (count / pageSize + (count % pageSize == 0 ? 0 : 1) - 1) * pageSize;
        Cursor cursor = getContentResolver().query(uri, projection, null, null, ContactsContract.Contacts._ID + " ASC limit " + pageSize + " offset " + currentOffset);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    User info = new User();
                    info.name = cursor.getString(0);
                    info.phone = cursor.getString(1);
                    info.id = cursor.getLong(2);
                    list.add(info);
                    Log.i("ContactsPerson: ", info.toString());
                }
                Log.i("ContactsPerson: ", list.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return list;
    }
}
