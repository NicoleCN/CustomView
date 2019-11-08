package com.example.customview.verify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.customview.R;

import java.util.Random;

/***
 * @date 2019-11-08 10:31
 * @author BoXun.Zhao
 * @description 移动拼图验证
 */
public class SlidVerifyView extends View {
    private Bitmap bgBitmap;
    private Bitmap newBgBitmap;
    private Bitmap cutBitmap;

    /**
     * 裁剪区域空白部分画笔
     */
    private Paint paintCutShadow;
    /**
     * 裁剪区域画笔
     */
    private Paint paintCut;

    private float curX;
    private float lastX;

    private int dx;
    /**
     * 裁剪圆形的半径
     */
    private int cutRadius;

    /**
     * 挖孔图片距离边界
     */
    private int padding;
    private int shadowLeft;
    private int srcLeft = padding;

    private int mWidth, mHeight;

    private Paint bgPaint;

    public SlidVerifyView(Context context) {
        this(context, null);
    }

    public SlidVerifyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidVerifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        paintCutShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCutShadow.setColor(Color.LTGRAY);

        paintCut = new Paint(Paint.ANTI_ALIAS_FLAG);
        //位图滤波
        paintCut.setFilterBitmap(true);
        paintCut.setStyle(Paint.Style.FILL_AND_STROKE);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setMaskFilter(new BlurMaskFilter(VerifyUtil.dp2px(context, 10), BlurMaskFilter.Blur.OUTER));
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.LTGRAY);

        cutRadius = VerifyUtil.dp2px(context, 30);
        padding = VerifyUtil.dp2px(context, 40);
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.xxjy);
    }

    /**
     * 底图
     */
    public Bitmap scaleBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    /**
     * 创建被裁剪的
     */
    public Bitmap createCutBitmap(Bitmap cutBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(cutRadius * 2, cutRadius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(cutRadius, cutRadius, cutRadius, paintCut);
        paintCut.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /*
         * 在指定范围随机生成空缺部分坐标，
         * 保证空缺部分出现在View右侧
         */
        int min = mWidth / 3;
        int max = mWidth - cutRadius - padding;

        Random random = new Random();
        shadowLeft = random.nextInt(max) % (max - min + 1) + min;
        //原图的方块 有XFerMode
        Rect rect = new Rect(shadowLeft, mHeight / 2 - cutRadius, cutRadius * 2 + shadowLeft, mHeight / 2 + cutRadius);
        RectF rectF = new RectF(0, 0, cutRadius * 2, cutRadius * 2);
        canvas.drawBitmap(cutBitmap, rect, rectF, paintCut);
        paintCut.setXfermode(null);
        return bitmap;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        curX = event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                dx = (int) (curX - lastX);
                srcLeft = dx + padding;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                boolean isSuccess = Math.abs(srcLeft - shadowLeft) < 8;
                if (isSuccess) {
                    Toast.makeText(getContext(), "验证成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "验证失败!", Toast.LENGTH_SHORT).show();
                    cutBitmap = createCutBitmap(newBgBitmap);
                    srcLeft = padding;
                    invalidate();
                }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minimumWidth = getSuggestedMinimumWidth();
        mWidth = measureSize(minimumWidth, widthMeasureSpec);
        float scale = mWidth / (float) bgBitmap.getWidth();
        mHeight = (int) (bgBitmap.getHeight() * scale);
        setMeasuredDimension(mWidth, mHeight);

        //scale
        newBgBitmap = scaleBitmap(bgBitmap, mWidth, mHeight);
        //创建被裁剪的
        cutBitmap = createCutBitmap(newBgBitmap);
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = defaultSize;
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
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //底部图
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        canvas.drawBitmap(newBgBitmap, null, rectF, paintCut);
        //圆心高
        int cy = mHeight / 2;
        //画裁剪阴影部分
        canvas.drawCircle(shadowLeft + cutRadius, cy, cutRadius, bgPaint);
        //画中间挖孔留白
        canvas.drawCircle(shadowLeft + cutRadius, cy, cutRadius, paintCutShadow);

        //裁剪填充部分
        Rect rect = new Rect(srcLeft, cy - cutRadius, cutRadius * 2 + srcLeft, cy + cutRadius);
        canvas.drawCircle(srcLeft + cutRadius, cy, cutRadius, bgPaint);
        canvas.drawBitmap(cutBitmap, null, rect, paintCut);
    }
}
