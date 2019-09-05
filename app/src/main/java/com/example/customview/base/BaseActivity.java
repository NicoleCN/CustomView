package com.example.customview.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/***
 * @date 2019/9/5 9:15
 * @author BoXun.Zhao
 * @description
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected void initData() {

    }

    protected void initView() {

    }

    protected abstract int getLayoutId();

    protected void startAct(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
