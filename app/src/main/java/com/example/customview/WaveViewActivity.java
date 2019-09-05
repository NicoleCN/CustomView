package com.example.customview;

import com.example.customview.bezie.WaveView;
import com.example.customview.base.BaseActivity;

/***
 * @date 2019/9/5 11:24
 * @author BoXun.Zhao
 * @description
 */
public class WaveViewActivity extends BaseActivity {

    private WaveView waveView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wave;
    }

    @Override
    protected void initView() {
        waveView = findViewById(R.id.waveView);
    }

    @Override
    protected void initData() {
        waveView.setProgress(75);
    }
}
