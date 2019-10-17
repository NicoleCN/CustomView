package com.example.customview.xfermode;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.example.customview.R;

/***
 *@date 创建时间 2019-10-17 10:16
 *@author 作者: BoXun.Zhao
 *@description
 */
public class WaterView extends AppCompatImageView {

    private Context mContext;
    private Paint mPaint;
    private Path mPath;
    private int mOffset;//位移
    private int mBezierDegree = 30;//曲线曲度（波峰波谷差值）
    private int mWidth;
    private int mHeight;
    private int waveLength;
    private int mWaveY;
    private Bitmap mBitmap;
    private ValueAnimator mValueAnimator;
    private PorterDuffXfermode mXfermode;

    public WaterView(Context context) {
        super(context);
        init(context);

    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mPath = new Path();
        this.mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.white));

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);//正片叠底

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_water_full);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 设置View的离屏缓冲。在绘图的时候新建一个“层”，所有的操作都在该层而不会影响该层以外的图像
         * 必须设置，否则设置的PorterDuffXfermode会无效，具体原因不明
         */
        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mBitmap, (mWidth - mBitmap.getWidth()) / 2, (mHeight - mBitmap.getHeight()) / 2, mPaint);
        mPaint.setXfermode(mXfermode);
        mPath.reset();
        mPath.lineTo(0, mWaveY);
        // 绘制2个贝塞尔曲线
        for (int i = 0; i < 2; i++) {
            mPath.quadTo(-waveLength * 3 / 4 + i * waveLength + mOffset, mWaveY + mBezierDegree / 2, -waveLength / 2 + i * waveLength + mOffset, mWaveY);
            mPath.quadTo(-waveLength / 4 + i * waveLength + mOffset, mWaveY - mBezierDegree / 2, i * waveLength + mOffset, mWaveY);
        }

        // 封闭波浪
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);

        // 还原画布，与canvas.saveLayer配套使用
        canvas.restoreToCount(sc);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = this.getMeasuredWidth();
        mHeight = this.getMeasuredHeight();
        waveLength = mWidth;
        mWaveY = (int) (mHeight * 0.3);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnim();
    }

    public void startAnim() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofInt(0, mWidth);
            mValueAnimator.setDuration(1200);
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(animation -> {
                mOffset = (int) animation.getAnimatedValue();
                invalidate();
            });
        }
        mValueAnimator.start();
    }
}
