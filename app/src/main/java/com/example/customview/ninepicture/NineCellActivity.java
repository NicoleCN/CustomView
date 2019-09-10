package com.example.customview.ninepicture;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.customview.R;
import com.example.customview.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;

/***
 * @date 2019/9/5 9:54
 * @author BoXun.Zhao
 * @description
 */
public class NineCellActivity extends BaseActivity implements NineCellBitmap.BitmapCallBack {
    private ImageView ivNineCell;
    private ArrayList<String> dataList;
    private String src = Environment.getExternalStorageDirectory() + "/android.jpg";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nine_cell;
    }

    @Override
    protected void initData() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        File file = new File(src);
        if (file.exists()) {
            dataList.add(src);
            NineCellBitmap.with().build().collectBitmap(this, dataList, this);
        } else {
            Toast.makeText(this, "请设置图片路径", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initView() {
        ivNineCell = findViewById(R.id.nine_cell_iv);
        Glide.with(this).load(src).into(ivNineCell);
    }

    public void addImage(View view) {
        dataList.add(src);
        NineCellBitmap.with().build().collectBitmap(this, dataList, this);
    }

    @Override
    public void onLoadingFinish(Bitmap bitmap) {
        Glide.with(this).load(bitmap).into(ivNineCell);
    }
}
