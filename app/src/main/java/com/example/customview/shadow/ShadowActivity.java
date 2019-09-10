package com.example.customview.shadow;

import android.widget.ImageView;

import com.example.customview.R;
import com.example.customview.base.BaseActivity;

/***
 * @date 2019/9/9 9:05
 * @author BoXun.Zhao
 * @description 不太好,但是还是传上去吧，还不如.9来的实在
 */
public class ShadowActivity extends BaseActivity {
    private ImageView imageView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shadow_layout;
    }

    @Override
    protected void initView() {
        imageView=findViewById(R.id.shadow_iv);
        ShadowDrawable.setShadow(imageView);
    }
}
