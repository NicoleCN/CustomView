package com.example.customview.barchart;

import com.example.customview.R;
import com.example.customview.base.BaseActivity;

import java.util.ArrayList;

/***
 * @date 2019/9/5 9:54
 * @author BoXun.Zhao
 * @description
 */
public class BarChartActivity extends BaseActivity {
    private BarChartView barChartView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bar_chart;
    }

    @Override
    protected void initData() {
        ArrayList<BarChartView.BarChartBean> barChartBeans = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            BarChartView.BarChartBean barChartBean = new BarChartView.BarChartBean();
            barChartBean.date = "10.16";
            if (i == 6) {
                barChartBean.date = "今天";
            }
            barChartBean.number = (i + 1) * 400;
            barChartBeans.add(barChartBean);
        }
        barChartView.setMaxNumber(2000).
                setDataList(barChartBeans);
    }

    @Override
    protected void initView() {
        barChartView = findViewById(R.id.barCharView);
    }

}
