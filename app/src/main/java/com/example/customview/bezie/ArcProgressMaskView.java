package com.example.customview.bezie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/***
 *@date 创建时间 2019-12-04 19:21
 *@author 作者: BoXun.Zhao
 *@description 暂时废弃
 */
@Deprecated
public class ArcProgressMaskView extends View {

    private int mMaskColor;

    private int mArcProgressColor;

    private int mRoundWidth;

    private int mArcWidth;

    private float mMaxValue = 100f;

    private float mRatio;

    private Paint mMaskPaint;

    private Paint mArcProgressPaint;

    private RectF mRectF;

    public ArcProgressMaskView(Context context) {
        this(context, null);
    }

    public ArcProgressMaskView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressMaskView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        mRoundWidth = DensityUtil.dp2px(50);
//        mArcWidth = DensityUtil.dp2px(2);
        float half = mArcWidth / 2f;
        mRectF = new RectF(half, half, mRoundWidth - half, mRoundWidth - half);
//        mMaskColor = ContextCompat.getColor(context, R.color.download_mask_Color);
//        mArcProgressColor = ContextCompat.getColor(context, R.color.download_arc_Color);

        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setColor(mMaskColor);
        mMaskPaint.setStyle(Paint.Style.FILL);

        mArcProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcProgressPaint.setColor(mArcProgressColor);
        mArcProgressPaint.setStyle(Paint.Style.STROKE);
        mArcProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcProgressPaint.setStrokeWidth(mArcWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mRoundWidth, mRoundWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMask(canvas);
        drawARcProgress(canvas);
    }

    private void drawMask(Canvas canvas) {
        canvas.drawCircle(mRoundWidth / 2f, mRoundWidth / 2f, mRoundWidth / 2f, mMaskPaint);
    }

    private void drawARcProgress(Canvas canvas) {
        canvas.drawArc(mRectF, -90, mRatio * 360, false, mArcProgressPaint);
    }

    public synchronized void setProgress(float currentValue) {
        if (currentValue < 0) {
            currentValue = 0;
        }
        if (currentValue > mMaxValue) {
            currentValue = mMaxValue;
        }
        if (currentValue <= mMaxValue) {
            mRatio = currentValue / mMaxValue;
            postInvalidate();
        }
    }
}
