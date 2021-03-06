package com.example.customview.bezie;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/***
 * @date 2019-09-11 16:59
 * @author BoXun.Zhao
 * @description 球形wave
 */
public class CircleWaveProgressView extends View {
    //振幅比例
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.1f;
    //默认振幅高度 可以不用了解
    private static final float DEFAULT_AMPLITUDE_VALUE = 50.0f;
    //水位高度比例
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;

    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    //进度
    private static final int DEFAULT_WAVE_PROGRESS_VALUE = 0;
    //border宽度
    private static final float DEFAULT_BORDER_WIDTH = 2;

    /**
     * 振幅比例 外面没有set的情况下 默认是0.1f
     */
    private float mAmplitudeRatio;
    private Context mContext;
    private int mWaveColor = 0xFF8BDAF9;

    private float mDefaultWaterLevel;
    /**
     * 水位比例
     */
    private float mWaterLevelRatio = 0f;

    /**
     * 控制动画
     */
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    private int mProgressValue = DEFAULT_WAVE_PROGRESS_VALUE;

    private BitmapShader mWaveShader;
    private Matrix mShaderMatrix;
    private Paint mWavePaint;
    private Paint mBorderPaint;

    private ObjectAnimator waveShiftAnim;

    public CircleWaveProgressView(final Context context) {
        this(context, null);
    }

    public CircleWaveProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleWaveProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initParams();
        initAnimation();
        initPaint();
    }

    private void initParams() {
        float amplitudeRatioAttr = DEFAULT_AMPLITUDE_VALUE / 1000;
        mAmplitudeRatio = (amplitudeRatioAttr > DEFAULT_AMPLITUDE_RATIO) ? DEFAULT_AMPLITUDE_RATIO : amplitudeRatioAttr;
    }

    private void initPaint() {
        mShaderMatrix = new Matrix();
        mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(dp2px(DEFAULT_BORDER_WIDTH));
        mBorderPaint.setColor(0xFF5DCBF6);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mWaveShader != null) {
            if (mWavePaint.getShader() == null) {
                mWavePaint.setShader(mWaveShader);
            }
            //因为默认的shader 是一半所以 y原点在中间 所以缩放中心 mDefaultWaterLevel
            //位移也要做处理 DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio

            //前面的是缩放比例  后面是缩放中心
            //如果没有设置振幅比例  setScale(1,1,0,mDefaultWaterLevel控件高度的一半)
            //如果设置振幅比例 由于shader y是拉伸的 所以看不出来 mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO 无论等于多少
            //shader x是repeat的  如果是1 就一个波峰 如果是0.5就2个波峰,其实就是周期
            mShaderMatrix.setScale(1, mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0, mDefaultWaterLevel);

            //真正动画会影响绘制的方法
            //mWaveShiftRatio  左右的滚动 右移从0-1
            //mWaterLevelRatio 上下的水位 progress
            mShaderMatrix.postTranslate(mWaveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            mWaveShader.setLocalMatrix(mShaderMatrix);
            //画边框
            float borderWidth = mBorderPaint.getStrokeWidth();
            if (borderWidth > 0) {
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                        (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
            }

            //画带有shader的圆
            float radius = getWidth() / 2f - borderWidth;
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mWavePaint);
            //canvas.drawRect(0, 0, getWidth(), getHeight(), mWavePaint);
        } else {
            mWavePaint.setShader(null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateWaveShader();
    }

    private void updateWaveShader() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width > 0 && height > 0) {
            //角速度 弧度每秒
            double defaultAngularFrequency = 2.0f * Math.PI / width;
            //默认波浪振幅高度 控件的0.1
            float defaultAmplitude = height * DEFAULT_AMPLITUDE_RATIO;
            //默认水位高度 控件的0.5
            mDefaultWaterLevel = height * DEFAULT_WATER_LEVEL_RATIO;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //这里需要一个画布  所以只能自己new 要传入bitmap保存
            Canvas canvas = new Canvas(bitmap);

            Paint wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            wavePaint.setStrokeWidth(2);

            // y=Asin(ωx+φ)+h
            final int endX = width + 1;
            final int endY = height + 1;
            //这里1个像素点绘制一次
            float[] waveY = new float[endX];

            //浅色的wave
            wavePaint.setColor(adjustAlpha(mWaveColor, 0.3f));
            for (int beginX = 0; beginX < endX; beginX++) {
                double wx = beginX * defaultAngularFrequency;
                float beginY = (float) (mDefaultWaterLevel + defaultAmplitude * Math.sin(wx));
                canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
                waveY[beginX] = beginY;
            }
            //正常颜色的wave
            wavePaint.setColor(mWaveColor);
            final int wave2Shift = (int) ((float) width / 4);
            for (int beginX = 0; beginX < endX; beginX++) {
                canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
            }
            //x方向重复
            //y方向拉伸
            mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureSize(400, widthMeasureSpec);
        int height = measureSize(400, heightMeasureSpec);
        int finalSize = (width < height) ? width : height;
        setMeasuredDimension(finalSize, finalSize);
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

    public void setAmplitudeRatio(int amplitudeRatio) {
        if (this.mAmplitudeRatio != (float) amplitudeRatio / 1000) {
            this.mAmplitudeRatio = (float) amplitudeRatio / 1000;
            invalidate();
        }
    }

    public void setProgressValue(int progress) {
        mProgressValue = progress;
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", mWaterLevelRatio, ((float) mProgressValue / 100));
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSetProgress = new AnimatorSet();
        animatorSetProgress.play(waterLevelAnim);
        animatorSetProgress.start();
    }

    public void setWaveShiftRatio(float waveShiftRatio) {
        if (this.mWaveShiftRatio != waveShiftRatio) {
            this.mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public void setWaterLevelRatio(float waterLevelRatio) {
        if (this.mWaterLevelRatio != waterLevelRatio) {
            this.mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public void startAnimation() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (waveShiftAnim != null && !waveShiftAnim.isStarted()) {
                    waveShiftAnim.start();
                }
            }
        }, 300);
    }

    public void cancelAnimation() {
        if (waveShiftAnim != null) {
            waveShiftAnim.cancel();
        }
    }

    private void initAnimation() {
        waveShiftAnim = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelAnimation();
        super.onDetachedFromWindow();
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
