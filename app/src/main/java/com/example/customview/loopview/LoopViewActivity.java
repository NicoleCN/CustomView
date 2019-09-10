package com.example.customview.loopview;

import com.example.customview.R;
import com.example.customview.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/***
 * @date 2019/9/5 9:15
 * @author BoXun.Zhao
 * @description
 */
public class LoopViewActivity extends BaseActivity {

    private LoopView loopView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loop;
    }

    @Override
    protected void initView() {
        loopView = findViewById(R.id.loopView);
    }

    @Override
    protected void initData() {
        loopView.setStringItems(getDataList());
    }

    private List<String> getDataList() {
        List<String> dataList=new ArrayList<>();
        for (int i = 10; i <= 100; i++) {
            dataList.add(String.valueOf(i).concat("公斤"));
        }
        return dataList;
    }
}
