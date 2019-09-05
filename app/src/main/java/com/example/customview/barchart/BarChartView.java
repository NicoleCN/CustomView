package com.example.customview.barchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.customview.R;

/***
 * @date 2019/9/5 13:53
 * @author BoXun.Zhao
 * @description 柱状图(有坐标)
 */
public class BarChartView extends View {

    private int mWidth;
    private int mHeight;
    private Context mContext;
    private Paint mCoordinatePaint;
    private Paint mChartPaint;
    private Paint mTextPaint;

    public BarChartView(Context context) {
        this(context, null);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    private void initPaint() {
        mCoordinatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoordinatePaint.setColor(ContextCompat.getColor(mContext,R.color.colorAccent));
        mCoordinatePaint.setStrokeWidth(2f);
        mChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureSize(400, widthMeasureSpec);
        mHeight = measureSize(400, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = size;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
    }

    private void drawCoordinate(Canvas canvas) {
        canvas.drawLine(0,0,100,100,mCoordinatePaint);
    }
}
