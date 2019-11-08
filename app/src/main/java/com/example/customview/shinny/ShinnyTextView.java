package com.example.customview.shinny;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/***
 * @date 2019-11-08 09:38
 * @author BoXun.Zhao
 * @description
 */
public class ShinnyTextView extends AppCompatTextView {
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 宽度
     */
    private int mViewWidth;
    /**
     * 线性渐变对象
     */
    private LinearGradient mLinearGradient;
    /**
     * 矩阵对象
     */
    private Matrix mGradientMatrix;
    /**
     * 平移距离
     */
    private int mTranslate;

    public ShinnyTextView(Context context) {
        this(context, null);
    }

    public ShinnyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShinnyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(
                        0,
                        0,
                        mViewWidth / 5,
                        0,
                        new int[]{Color.BLUE, Color.RED, Color.BLUE},
                        null,
                        Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文字之前
        super.onDraw(canvas);
        //绘制文字之后
        if (mGradientMatrix != null) {
            mTranslate += 10;
            if (mTranslate > mViewWidth) {//决定文字闪烁的频繁:快慢
                mTranslate = -mViewWidth / 10;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(10);
        }
    }
}
