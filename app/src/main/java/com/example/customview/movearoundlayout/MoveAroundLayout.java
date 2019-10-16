package com.example.customview.movearoundlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/***
 * @date 2019-10-15 13:59
 * @author BoXun.Zhao
 * @description
 */
public class MoveAroundLayout extends FrameLayout {
    private static final float SPEED_PERCENT = 0.01f;
    private Matrix mMatrix;
    private Paint mPaint;
    private PorterDuffXfermode mXFerMode;
    private float mDensity;
    private float mRound;
    private int mWidth;
    private int mHeight;
    private PathMeasure mPathMeasure;
    private RectF mRectF;
    private Path mSrcPath;
    private int mPathLength;
    private int mSpeedPx;
    private LinearGradient mLinearGradient;
    private int mDx;
    private int mDy;
    private Path mDstPath1;
    private Path mDstPath2;
    private Path mDstPath3;
    private Path mDstPath4;
    private int mStartD1;
    private int mLength1;
    private int mStopD1;
    private int mStopD2;
    private int mLength2;
    private int mStartD2;
    private int mStartD3;
    private int mLength3;
    private int mStopD3;
    private int mStartD4;
    private int mLength4;
    private int mStopD4;
    private Bitmap mBitmapFrame;

    public MoveAroundLayout(@NonNull Context context) {
        this(context, null);
    }

    public MoveAroundLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveAroundLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = context.getResources().getDisplayMetrics().density;
        initView();
    }

    private void initView() {
        mMatrix = new Matrix();
        //抗锯齿防抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(mDensity * 8);
        mRound = mDensity * 14;
        mXFerMode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mPathMeasure = new PathMeasure();
        mRectF = new RectF(0, 0, w, h);
        mSrcPath = new Path();
        mDstPath1 = new Path();
        mDstPath2 = new Path();
        mDstPath3 = new Path();
        mDstPath4 = new Path();
        //CW顺时针  CCW逆时针
        mSrcPath.addRoundRect(mRectF, mRound, mRound, Path.Direction.CW);
        mPathMeasure.setPath(mSrcPath, true);
        mPathLength = (int) mPathMeasure.getLength();
        mStartD1 = 0;
        //因为是逆时针 原点在左下角
        mLength1 = mPathLength / 8;
        mStopD1 = mStartD1 + mLength1;

        //有1dp的像素没连接  先写2 原码没有2
        mStopD2 = mPathLength + 2;
        mLength2 = mPathLength / 8;
        mStartD2 = mStopD2 - mLength2;

        mStartD3 = (mPathLength / 2) - (mPathLength / 8);
        mLength3 = mPathLength / 4;
        mStopD3 = mStartD3 + mLength3;

        mStartD4 = 0;
        mLength4 = 0;
        mStopD4 = mStartD4 + mLength4;

        mSpeedPx = (int) (mPathLength * SPEED_PERCENT);
        //从上到下
        mLinearGradient = new LinearGradient(0, 0, 0, (float) h,
                new int[]{Color.parseColor("#FF64A1"), Color.parseColor("#A643FF"),
                        Color.parseColor("#64EBFF"), Color.parseColor("#FFFE39"),
                        Color.parseColor("#FF9964")},
                new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1.0f},
                Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        mBitmapFrame = makeRoundRectFrame(getWidth(), getHeight());
    }

    //画自己
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //画孩子
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mRectF != null) {
            getDstPath(mPathMeasure);
            handlePos();
            handleGradient();
            drawPathWithRound(canvas);
            reset();
            invalidate();
        }
    }

    private void handlePos() {
        if (mStopD1 >= mPathLength) {
            mStopD2 = mPathLength;
            mStartD2 = mStartD1;
            mStartD1 = 0;
            mStopD1 = 1;
        }
        if (mStartD2 >= mPathLength) {
            mStartD1 += mSpeedPx;
        }
        mStopD1 += mSpeedPx;
        mStartD2 += mSpeedPx;
        mStartD3 += mSpeedPx;
        mStopD3 = mStartD3 + mLength3;
        if (mStopD3 >= mPathLength) {
            mStopD4 += mSpeedPx;
        }
        if (mStartD3 >= mPathLength) {
            mStopD4 = 0;
            mStartD4 = 0;
            mStartD3 = 0;
            mStopD3 = mStartD3 + mLength3;
        }
    }

    private void drawPathWithRound(Canvas canvas) {
        //保存当前所有
        int saveLayer = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mDstPath1, mPaint);
        canvas.drawPath(mDstPath2, mPaint);
        canvas.drawPath(mDstPath3, mPaint);
        canvas.drawPath(mDstPath4, mPaint);
        mPaint.setXfermode(mXFerMode);
        //保留路径和多余的角 多余的角 保留路径与整个bitmap 重叠的 路径部分(bitmap角是圆弧 会去掉多余路径不规则的地方)
        canvas.drawBitmap(mBitmapFrame, 0, 0, mPaint);
        //最后将画笔去除Xfermode
        mPaint.setXfermode(null);
        //回到保存的时候
        canvas.restoreToCount(saveLayer);
    }

    private void handleGradient() {
        //就是这么写的 添加闪光的效果
        mDx += mSpeedPx;
        mDy += mSpeedPx;
        mMatrix.setTranslate(mDx, mDy);
        mLinearGradient.setLocalMatrix(mMatrix);
    }

    private void getDstPath(PathMeasure mPathMeasure) {
        mPathMeasure.getSegment((float) mStartD1, (float) mStopD1, mDstPath1, true);
        mPathMeasure.getSegment((float) mStartD2, (float) mStopD2, mDstPath2, true);
        mPathMeasure.getSegment((float) mStartD3, (float) mStopD3, mDstPath3, true);
        mPathMeasure.getSegment((float) mStartD4, (float) mStopD4, mDstPath4, true);
    }

    private void reset() {
        mDstPath1.reset();
        mDstPath2.reset();
        mDstPath3.reset();
        mDstPath4.reset();
        mDstPath1.lineTo(0, 0);
        mDstPath2.lineTo(0, 0);
        mDstPath3.lineTo(0, 0);
        mDstPath4.lineTo(0, 0);
    }

    private Bitmap makeRoundRectFrame(int width, int height) {
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Path path = new Path();
        path.addRoundRect(new RectF(0.0f, 0.0f, (float) width, (float) height), mRound, mRound, Path.Direction.CW);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#0000ff"));
        canvas.drawPath(path, paint);
        return createBitmap;
    }
}
