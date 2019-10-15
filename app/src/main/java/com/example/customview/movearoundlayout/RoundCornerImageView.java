package com.example.customview.movearoundlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.customview.R;


public class RoundCornerImageView extends AppCompatImageView {
    float cornerSize;
    Path path;

    public RoundCornerImageView(Context context) {
        this(context, null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        cornerSize = a.getDimension(R.styleable.RoundCornerImageView_corners, 0);
        a.recycle();
        if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(true);
        } else {
            path = new Path();
        }
    }


    @Override
    public void onDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.onDraw(canvas);
        } else {
            final int saveCount = canvas.save();
            canvas.clipPath(path);
            super.onDraw(canvas);
            canvas.restoreToCount(saveCount);
        }

    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, w, h, cornerSize);
                }
            });
        } else {
            path.reset();
            final RectF rect = new RectF(0, 0, w, h);
            path.addRoundRect(rect, cornerSize, cornerSize, Path.Direction.CCW);
        }
    }
}