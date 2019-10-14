package com.example.customview.calendar;

import android.text.TextUtils;
import android.widget.Toast;

import com.example.customview.R;
import com.example.customview.base.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends BaseActivity implements DatePickerController {
    private DayPickerView mDayPickerView;
    private boolean isSingle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initView() {
        mDayPickerView = findViewById(R.id.day_picker);
        mDayPickerView.setController(this);
        isSingle = getIntent().getBooleanExtra("isSingle", true);
        mDayPickerView.setSingle(isSingle);
        String date = getIntent().getStringExtra("minDay");
        if (!TextUtils.isEmpty(date)) {
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTimeInMillis(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mDayPickerView.setMinDay(calendar);
        }
    }

    @Override
    public int getMaxYear() {
        return Calendar.getInstance().get(Calendar.YEAR) + 2;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        if (isSingle) {
            Toast.makeText(this, "当前单选一个日期：" + year + "-" + (month + 1) + "-" + day, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
        if (!isSingle) {
            //获取第一个选择的日期
            SimpleMonthAdapter.CalendarDay firstDay = selectedDays.getFirst();
            //获取第二个选择日期
            SimpleMonthAdapter.CalendarDay lastDay = selectedDays.getLast();
            //根据需求做对应的逻辑处理，
            //我这里是做了一个往返日期逻辑，得到两个选中的日期后需要进行判断出小的日期肯定是出发日期。
            if (firstDay != null) {
                Calendar firstCalendar = Calendar.getInstance();
                firstCalendar.set(firstDay.year, firstDay.month, firstDay.day);
            } else {
            }
            if (lastDay != null) {
                Calendar lastCalendar = Calendar.getInstance();
                lastCalendar.set(lastDay.year, lastDay.month, lastDay.day);
            } else {
            }
        }
    }
}
