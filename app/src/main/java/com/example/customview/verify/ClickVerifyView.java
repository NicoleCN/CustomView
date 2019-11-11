package com.example.customview.verify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.customview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * @date 2019-10-31 13:58
 * @author BoXun.Zhao
 * @description
 */
public class ClickVerifyView extends View {
    private int DEFAULT_TEXT_NUMBER = 4;
    private int mWidth;
    private int mHeight;
    private Bitmap srcBitmap;
    /**
     * 调整大小的bitmap
     */
    private Bitmap bgBitmap;

    /**
     * 验证码文字画笔
     */
    private Paint textPaint;
    private Paint selectPaint;
    private Paint bgPaint;
    private Paint selectTextPaint;
    private Random random;
    private RectF bgRectF;
    /**
     * 是否是生成随机验证码状态
     */
    private boolean isRandomProduce = true;

    private String mVerifyText;
    private List<Point> tapPoints = new ArrayList<>();
    private List<Integer> tapIndex = new ArrayList<>();
    /**
     * 文字
     */
    private List<Point> textPoints = new ArrayList<>();
    /**
     * 倾斜角度
     */
    private List<Integer> degrees = new ArrayList<>();
    /**
     * 文字范围,touch和判断文字范围是否重复用
     */
    private List<Region> regions = new ArrayList<>();
    private int textNumber = DEFAULT_TEXT_NUMBER;


    public ClickVerifyView(Context context) {
        this(context, null);
    }

    public ClickVerifyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickVerifyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.xxjy);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setFilterBitmap(true);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(Color.BLUE);
        textPaint.setShadowLayer(3, 2, 2, Color.RED);
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));

        selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setColor(Color.WHITE);

        selectTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        random = new Random();
        setVerifyText("我要努力");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minimumWidth = getSuggestedMinimumWidth();
        mWidth = measureSize(minimumWidth, widthMeasureSpec);
        float scale = mWidth / (float) srcBitmap.getWidth();
        mHeight = (int) (srcBitmap.getHeight() * scale);
        bgBitmap = scaleBitmap(srcBitmap, mWidth, mHeight);
        bgRectF = new RectF(0, 0, mWidth, mHeight);
        //左右要+1 判断距离
        textPaint.setTextSize(mWidth / (textNumber + 2));
        setMeasuredDimension(mWidth, mHeight);
    }

    private Bitmap scaleBitmap(Bitmap srcBitmap, int newWidth, int newHeight) {
        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
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
        //画背景
        canvas.drawBitmap(bgBitmap, null, bgRectF, bgPaint);
        //画文字
        if (isRandomProduce) {
            //随机生成4字成语状态
            dealProductStatus(canvas);
        } else {
            //用户点击时状态
            dealUserClickStatus(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Log.e("zbx", "ACTION_DOWN");
            for (Region region : regions) {
                if (region.contains(x, y)) {
                    Log.e("zbx", "点在范围内");
                    isRandomProduce = false;
                    int index = regions.indexOf(region);
                    if (!tapIndex.contains(index)) {
                        Log.e("zbx", "记录在选择顺序list中");
                        tapIndex.add(index);
                        tapPoints.add(new Point(x, y));
                    }
                    if (tapIndex.size() == mVerifyText.length()) {
                        StringBuilder s = new StringBuilder();
                        for (Integer i : tapIndex) {
                            s.append(i);
                        }
                        Log.e("zbx", "s.toString()" + s.toString());
                        int result = Integer.parseInt(s.toString());
                        //争取顺序是0123
                        if (result == 3210) {
                            Toast.makeText(getContext(), "验证成功!", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            Toast.makeText(getContext(), "验证失败!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
            invalidate();
        }
        return false;
    }

    private void dealUserClickStatus(Canvas canvas) {
        if (TextUtils.isEmpty(mVerifyText)) {
            throw new RuntimeException("mVerifyText can not be null!");
        }
        for (int i = 0; i < mVerifyText.length(); i++) {
            String s = String.valueOf(mVerifyText.charAt(mVerifyText.length() - i - 1));
            int textSize = (int) textPaint.measureText(s);
            canvas.save();
            int x = textPoints.get(i).x;
            int y = textPoints.get(i).y;
            int degree = degrees.get(i);
            canvas.translate(x, y);
            canvas.rotate(degree);
            canvas.drawText(s, 0, textSize, textPaint);
            regions.add(new Region(x, y, textSize + x, textSize + y));
            canvas.restore();
        }
        //绘制点击顺序
        for (Point point : tapPoints) {
            int index = tapPoints.indexOf(point) + 1;
            String s = index + "";
            //数字为文字的3分之一
            int textSize = mWidth / (textNumber + 2) / 3;
            selectTextPaint.setTextSize(textSize);
            canvas.drawCircle(point.x, point.y, textSize, selectPaint);
            Rect rect = new Rect();
            selectTextPaint.getTextBounds(s, 0, 1, rect);
            int textWidth = rect.width();
            int textHeight = rect.height();
            canvas.drawText(s, point.x - textWidth / 2, point.y + textHeight / 2, selectTextPaint);
        }
    }

    private void dealProductStatus(Canvas canvas) {
        if (TextUtils.isEmpty(mVerifyText)) {
            throw new RuntimeException("mVerifyText can not be null!");
        }
        textPoints.clear();
        degrees.clear();
        tapIndex.clear();
        tapPoints.clear();
        for (int i = 0; i < mVerifyText.length(); i++) {
            //为了后面的验证方便 所以倒着写
            String s = String.valueOf(mVerifyText.charAt(mVerifyText.length() - i - 1));
            int textSize = (int) textPaint.measureText(s);
            canvas.save();
            //random范围去掉边 要去掉左右两个文字的距离
            //再去掉两个像素防止重复
            int x = random.nextInt(mWidth - textSize - 1);
            int y = random.nextInt(mHeight - textSize - 1);

            // TODO: 2019-11-11  这里以后改下 最好不要用死循环
            while (checkPointLegal(x, y) || checkPointLegal(x, y + textSize) || checkPointLegal(x + textSize, y) || checkPointLegal(x + textSize, y + textSize)) {
                x = random.nextInt(mWidth - textSize);
                y = random.nextInt(mHeight - textSize);
            }

            textPoints.add(new Point(x, y));
            canvas.translate(x, y);

            //文字随机倾斜
            int degree = random.nextInt(30);
            degrees.add(degree);
            canvas.rotate(degree);

            canvas.drawText(s, 0, textSize, textPaint);
            regions.add(new Region(x, y, textSize + x, textSize + y));
            canvas.restore();
        }
    }

    //文字不能碰到边
    private boolean checkPointLegal(int x, int y) {
        for (Region region : regions) {
            if (region.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据textNumber来
     * 一般取四字成语,这里就不准确判断了 默认大于等于4
     */
    public void setVerifyText(String str) {
        if (TextUtils.isEmpty(str) || str.length() < textNumber) {
            return;
        }
        mVerifyText = str;
    }

    public void setTextNumber(int textNumber) {
        this.textNumber = textNumber;
    }

    public void startVerify() {
        invalidate();
    }
}
