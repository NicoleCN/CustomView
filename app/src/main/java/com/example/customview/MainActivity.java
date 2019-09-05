
package com.example.customview;

import android.view.View;

import com.example.customview.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public void goToLoopView(View view) {
        startAct(LoopViewActivity.class);
    }

    public void goToNineCell(View view) {
        startAct(NineCellActivity.class);
    }
}
