package com.example.customview.bezie;

import com.example.customview.R;
import com.example.customview.base.BaseActivity;

/***
 * @date 2019/9/5 11:24
 * @author BoXun.Zhao
 * @description
 */
public class WaveViewActivity extends BaseActivity {

    private WaveView waveView;
    private CircleWaveProgressView circleWaveProgressView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wave;
    }

    @Override
    protected void initView() {
        waveView = findViewById(R.id.waveView);
        circleWaveProgressView = findViewById(R.id.circleWaveProgressView);
    }

    @Override
    protected void initData() {
        waveView.setProgress(75);
        circleWaveProgressView.setProgressValue(75);
        circleWaveProgressView.startAnimation();
    }
}
