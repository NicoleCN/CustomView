package com.example.customview.barchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

/***
 * @date 2019/9/5 13:53
 * @author BoXun.Zhao
 * @description 柱状图(有坐标)
 */
public class BarChartView extends View {
    private static final String TAG = "zbx";
    private int mWidth;
    private int mHeight;
    private int mBarWidth;
    private int mMaxBarHeight;
    private int mVerticalDiver;
    private int mHorizontalDiver;
    private int firstXBarStart;
    private int firstYBarStart;
    private Context mContext;
    //暂时不要坐标
    //private Paint mCoordinatePaint;
    private Paint mChartPaint;
    private Paint mTextPaint;
    private int mSpacing = BarChartUtil.DEFAULT_NUMBER_INCREASE;
    private int mTextColor = 0xff15C0FF;
    private int mTextSize;
    private ArrayList<BarChartBean> dataList;
    private int maxNumber = BarChartUtil.DEFAULT_MAX_NUMBER;
    private int thresholdNumber = BarChartUtil.DEFAULT_THRESHOLD_NUMBER;
    private int[] mBarColorArray = new int[]{0xff37D7FF, 0xff14C0FF};
    private int mBarSize = 7;
    private float progress;

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
        mChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStrokeWidth(2);
        mTextSize = (int) (getDisplayDensity() * BarChartUtil.DEFAULT_TEXT_SIZE);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureSize(400, widthMeasureSpec);
        mHeight = measureSize(400, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        calculateParams();
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

    private void calculateParams() {
        //x方向
        mBarWidth = (int) (mWidth * BarChartUtil.DEFAULT_BAR_WIDTH_PERCENT);
        mHorizontalDiver = (int) (mWidth * BarChartUtil.DEFAULT_HORIZONTAL_DIVE_WIDTH_PERCENT);
        //留4个dp显示防止超过
        firstXBarStart = (int) (mWidth - mBarWidth - (mBarWidth + mHorizontalDiver) * (mBarSize - 1)-4*getDisplayDensity());
        //y方向
        mMaxBarHeight = (int) (mHeight - getDisplayDensity() * (BarChartUtil.DEFAULT_TEXT_SIZE + BarChartUtil.DEFAULT_BAR_TEXT_SPACE) * 2);
        mVerticalDiver = mMaxBarHeight / 5;
        firstYBarStart = (int) (BarChartUtil.DEFAULT_BAR_TEXT_SPACE * getDisplayDensity() + mTextSize);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinateText(canvas);
        drawBarAndNumber(canvas);
    }

    private void drawCoordinateText(Canvas canvas) {
        //外面设置的
        //y
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < 6; i++) {
            String drawText = null;
            if (i == 0) {
                drawText = maxNumber + "+";
            } else {
                drawText = String.valueOf(maxNumber - i * mSpacing);
            }
            drawText(canvas, drawText, 0, firstYBarStart + mTextSize / 2 + mVerticalDiver * i, mTextPaint);
        }
        //x
        canvas.save();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.translate(firstXBarStart, mHeight - firstYBarStart);
        int itemDiver = mBarWidth + mHorizontalDiver;
        for (int i = 0; i < mBarSize; i++) {
            drawText(canvas, dataList.get(i).date, mBarWidth / 2 + itemDiver * i,  BarChartUtil.DEFAULT_BAR_TEXT_SPACE * getDisplayDensity()+(mTextSize>>1), mTextPaint);
        }
        canvas.restore();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawBarAndNumber(Canvas canvas) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        canvas.save();
        canvas.translate(firstXBarStart, mHeight - firstYBarStart);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < mBarSize; i++) {
            BarChartBean barChartBean = dataList.get(i);
            int factNumber = barChartBean.number;
            if (factNumber <= 0) {
                continue;
            }
            int temPNumber =  factNumber> maxNumber ? maxNumber : factNumber;
            float height =temPNumber * 1.0f / maxNumber * mMaxBarHeight;
            int itemDiver = mBarWidth + mHorizontalDiver;
            int start = itemDiver * i;
            mChartPaint.setShader(new LinearGradient(mBarWidth / 2 + itemDiver * i, -height*progress, mBarWidth / 2 + itemDiver * i, 0, mBarColorArray, null, Shader.TileMode.CLAMP));
            canvas.drawRoundRect(start, -height*progress, start + mBarWidth, 0, 2 * getDisplayDensity(), 2 * getDisplayDensity(), mChartPaint);
            //number
            drawText(canvas,String.valueOf((int)(factNumber*progress)),start+(mBarWidth>>1),-height*progress-firstYBarStart+(mTextSize>>1),mTextPaint);
        }
        canvas.restore();
    }

    private float getDisplayDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(progress, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(animator -> {
            progress = (float) animator.getAnimatedValue();
            invalidate();
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setStartDelay(300);
        valueAnimator.start();
    }

    private void drawText(Canvas canvas, String text, float startX, float startY, Paint paint) {
        //Log.e(TAG, "text-->" + text + "centerX-->" + startX + "centerY-->" + centerY);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseline = startY + distance;
        canvas.drawText(text, startX, baseline, paint);
    }

    public BarChartView setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        mSpacing = maxNumber / 5;
        return this;
    }

    public void setDataList(ArrayList<BarChartBean> barChartBeans) {
        dataList = barChartBeans;
        startAnimation();
    }

    public static class BarChartBean {
        public String date;
        public int number;
    }
}
