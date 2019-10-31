
package com.example.customview;

import android.view.View;
import android.widget.Toast;

import com.example.customview.autotest.AutoTestActivity;
import com.example.customview.barchart.BarChartActivity;
import com.example.customview.base.BaseActivity;
import com.example.customview.bezie.WaveViewActivity;
import com.example.customview.calendar.CalendarActivity;
import com.example.customview.gyro.GyroActivity;
import com.example.customview.loopview.LoopViewActivity;
import com.example.customview.movearoundlayout.MoveAroundActivity;
import com.example.customview.ninepicture.NineCellActivity;
import com.example.customview.shadow.ShadowActivity;
import com.example.customview.threed.ThreeDActivity;
import com.example.customview.verify.VerifyActivity;
import com.example.customview.xfermode.XFerModeActivity;

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

    public void goToMoveAround(View view) {
        startAct(MoveAroundActivity.class);
    }

    public void goToXFerMode(View view) {
        startAct(XFerModeActivity.class);
    }

    public void goToVector(View view) {
        Toast.makeText(this, "没啥效果", Toast.LENGTH_SHORT).show();
//        startAct(VectorActivity.class);
    }

    public void goToGyro(View view) {
        startAct(GyroActivity.class);
    }

    public void autoTest(View view) {
        startAct(AutoTestActivity.class);
    }

    public void verifyCode(View view) {
        startAct(VerifyActivity.class);
    }
}
