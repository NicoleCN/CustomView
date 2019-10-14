
package com.example.customview;

import android.view.View;

import com.example.customview.barchart.BarChartActivity;
import com.example.customview.base.BaseActivity;
import com.example.customview.bezie.WaveViewActivity;
import com.example.customview.calendar.CalendarActivity;
import com.example.customview.loopview.LoopViewActivity;
import com.example.customview.ninepicture.NineCellActivity;
import com.example.customview.shadow.ShadowActivity;
import com.example.customview.threed.ThreeDActivity;

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

    public void goToWaveView(View view) {
        startAct(WaveViewActivity.class);
    }

    public void goToBarChartView(View view) {
        startAct(BarChartActivity.class);
    }

    public void goToShadowView(View view) {
        startAct(ShadowActivity.class);
    }

    public void goToCalendar(View view) {
        startAct(CalendarActivity.class);
    }

    public void goTo3D(View view) {
        startAct(ThreeDActivity.class);
    }
}
