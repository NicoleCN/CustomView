package com.example.customview.threed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;

/***
 * @date 2019-10-14 13:23
 * @author BoXun.Zhao
 * @description
 */
public class SonFlipLayout extends FlipLayout {

    public SonFlipLayout(Context context) {
        this(context, null);
    }

    public SonFlipLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SonFlipLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
   }

    @Override
    protected View getBackChildView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.back_flip_layout, null);
        return view;
    }

    @Override
    protected View getFrontChildView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.front_flip_layout, null);
        return view;
    }
}
