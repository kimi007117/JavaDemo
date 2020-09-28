package com.noe.rxjava;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;

import com.noe.rxjava.dagger.ActivityModule;
import com.noe.rxjava.dagger.AppComponent;
import com.noe.rxjava.dagger.AppModule;
import com.noe.rxjava.dagger.DaggerActivityComponent;
import com.noe.rxjava.dagger.DaggerAppComponent;
import com.noe.rxjava.dagger.Person;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by lijie24 on 2017/1/16.
 */

public class DaggerActivity extends AppCompatActivity {
    private EditText mEditText;
    private String content = null;

    @Inject
    Person mPerson;

    @Inject
    Person mPerson2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);
        mEditText = (EditText) findViewById(R.id.edit_search);
        mEditText.setText(content);
        mEditText.setSelection(mEditText.getText().length());

//        TestComponent testComponent = DaggerTestComponent.builder().daggerModule(new DaggerModule(this)).build();

//        testComponent.inject(this);

//        Toast.makeText(this,mPerson.getText(),Toast.LENGTH_SHORT).show();

//        Log.i("dagger","person = "+ mPerson.toString()+"; person2 = "+ mPerson2.toString());

        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        DaggerActivityComponent.builder().appComponent(appComponent).activityModule(new ActivityModule()).build().inject(this);

        Timber.i("dagger person = %s; person2 = %s", mPerson.toString(), mPerson2.toString());
    }
}
