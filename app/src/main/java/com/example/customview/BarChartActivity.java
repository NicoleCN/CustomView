package com.example.customview;

import com.example.customview.barchart.BarChartView;
import com.example.customview.base.BaseActivity;

/***
 * @date 2019/9/5 9:54
 * @author BoXun.Zhao
 * @description
 */
public class BarChartActivity extends BaseActivity  {
    private BarChartView barChartView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bar_chart;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        barChartView = findViewById(R.id.barCharView);
    }

}
