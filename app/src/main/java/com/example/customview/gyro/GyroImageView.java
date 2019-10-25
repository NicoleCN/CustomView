package com.example.customview.gyro;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

/***
 * @date 2019-10-24 09:54
 * @author BoXun.Zhao
 * @description
 */
public class GyroImageView extends AppCompatImageView {
    private GyroManager gyroManager;
    private Context mContext;
    protected float mAngelX;
    protected float mAngelY;
    private float mScaleX;
    private float mScaleY;
    private float mLenX;
    private float mLenY;
    private float mOffsetX;
    private float mOffsetY;

    public GyroImageView(Context context) {
        this(context, null);
    }

    public GyroImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GyroImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        setScaleType(ScaleType.CENTER);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (gyroManager == null) {
            gyroManager = new GyroManager(this);
        }
        if (!gyroManager.register(mContext)) {
            Toast.makeText(mContext, "设备不支持陀螺仪", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (gyroManager != null) {
            gyroManager.unRegister();
        }
    }

    public void update(float scaleX, float scaleY) {
        mScaleX = scaleX;
        mScaleY = scaleY;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        if (getDrawable() != null) {
            int drawableWidth = getDrawable().getIntrinsicWidth();
            int drawableHeight = getDrawable().getIntrinsicHeight();
            mLenX = Math.abs((drawableWidth - width) * 0.5f);
            mLenY = Math.abs((drawableHeight - height) * 0.5f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null || isInEditMode()) {
            super.onDraw(canvas);
        } else {
            mOffsetX = (mLenX * mScaleX);
            mOffsetY = (mLenY * mScaleY);
            canvas.save();
            canvas.translate(mOffsetX, mOffsetY);
            super.onDraw(canvas);
            canvas.restore();
        }
    }
}
