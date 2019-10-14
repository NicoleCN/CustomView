package com.example.customview.threed;

import android.view.View;

import com.example.customview.R;
import com.example.customview.base.BaseActivity;

/***
 * @date 2019-10-14 10:28
 * @author BoXun.Zhao
 * @description
 */
public class ThreeDActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_threed;
    }

    @Override
    protected void initView() {
        FlipLayout flipLayout = findViewById(R.id.roll3DLayout);
        flipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipLayout.flip();
            }
        });
    }
}
