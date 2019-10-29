package com.example.customview.autotest;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.base.BaseActivity;

/***
 * @date 2019-10-28 16:54
 * @author BoXun.Zhao
 * @description
 */
public class AutoTestActivity extends BaseActivity implements View.OnClickListener {
    private Button button;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auto_test_layout;
    }

    @Override
    protected void initView() {
        button = findViewById(R.id.auto_test_button);
        imageView = findViewById(R.id.auto_test_ImageView);
        textView = findViewById(R.id.auto_test_TextView);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auto_test_button:
                textView.setText("自动化测试完成");
                textView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_android_svg);
                break;
            default:
                break;
        }
    }
}
