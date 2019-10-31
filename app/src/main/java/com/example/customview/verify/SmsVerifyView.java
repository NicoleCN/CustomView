package com.example.customview.verify;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

/***
 * @date 2019-10-31 13:58
 * @author BoXun.Zhao
 * @description
 */
public class SmsVerifyView extends View {
    private static final int MESSAGE_REFRESH = 0x100;
    /**
     * 验证码个数
     */
    private int mSmsNum = 6;
    /**
     * 距离左右边界
     */
    private int margin = 20;

    /**
     * 盒子之间的距离
     */
    private int mBoxMargin = 10;
    private int mTextSize = 24;
    private Paint mBoxPaint;
    private Paint mSelectPaint;
    private Paint mTextPaint;
    private int mBoxColor = 0xff000000;
    private int itemWidth;
    private int mWidth;
    private int mHeight;

    private int selectIndex;
    private boolean isShowSelect;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_REFRESH) {
                postInvalidate();
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, 800);
            }
        }
    };
    private StringBuilder mStrBuilder;
    /**
     * 是否自动隐藏键盘
     */
    private boolean autoHideKeyboard = true;
    private Rect mTextRect;

    public SmsVerifyView(Context context) {
        this(context, null);
    }

    public SmsVerifyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmsVerifyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(VerifyUtil.dp2px(getContext(), 1));
        mBoxPaint.setColor(mBoxColor);

        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSelectPaint.setStrokeWidth(1);
        mSelectPaint.setColor(mBoxColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(VerifyUtil.dp2px(getContext(), mTextSize));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mBoxColor);
        mTextRect = new Rect();
        mTextPaint.getTextBounds("伯勋", 0, 2, mTextRect);

        mStrBuilder = new StringBuilder();
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        int realWidth = mWidth - 2 * VerifyUtil.dp2px(getContext(), margin);
        //这里itemWidth可以写死
        itemWidth = (realWidth - VerifyUtil.dp2px(getContext(), mBoxMargin) * (mSmsNum - 1)) / mSmsNum;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startX = VerifyUtil.dp2px(getContext(), margin);
        int startY = mHeight / 2 - itemWidth / 2;
        int boxMargin = VerifyUtil.dp2px(getContext(), mBoxMargin);
        for (int i = 0; i < mSmsNum; i++) {
            //画框
            canvas.drawRect(startX + i * boxMargin + i * itemWidth, startY, startX + (itemWidth * (i + 1)) + i * boxMargin, startY + itemWidth, mBoxPaint);
            //画字
            int length = mStrBuilder.length();
            if (i < length) {
                String str = mStrBuilder.charAt(i) + "";
                canvas.drawText(str, startX + i * boxMargin + i * itemWidth + itemWidth / 2, mHeight / 2 + mTextRect.height() / 2 - mTextRect
                        .bottom, mTextPaint);
            }
            //画光标
            if (selectIndex == i) {
                //0.1表示上下留有百分之10的间距
                if (isShowSelect) {
                    canvas.drawLine(startX + i * boxMargin + i * itemWidth + itemWidth / 2, startY + itemWidth * 0.1f, startX + i * boxMargin + i * itemWidth + itemWidth / 2, startY + itemWidth * 0.9f, mSelectPaint);
                    isShowSelect = false;
                } else {
                    isShowSelect = true;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        showKeyboard(event);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && mStrBuilder.length() > 0) {
            mStrBuilder.deleteCharAt(mStrBuilder.length() - 1);
            selectIndex--;
            if (selectIndex < 0) {
                selectIndex = 0;
            }
            invalidate();
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9 && mStrBuilder.length() < mSmsNum) {
            mStrBuilder.append(event.getDisplayLabel());
            selectIndex++;
            if (selectIndex > mSmsNum - 1) {
                selectIndex = mSmsNum;
            }
            invalidate();
        }
        if (mStrBuilder.length() >= mSmsNum && autoHideKeyboard) {
            clearFocus();
            hideKeyboard();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        BaseInputConnection fic = new BaseInputConnection(this, false) {
            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
        };
        outAttrs.actionLabel = null;
        outAttrs.inputType = InputType.TYPE_CLASS_PHONE;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_NEXT;
        return fic;
    }

    private void showKeyboard(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAnimation();
    }

    private void startAnimation() {
        removeAnimation();
        mHandler.sendEmptyMessage(MESSAGE_REFRESH);
    }

    private void removeAnimation() {
        mHandler.removeMessages(MESSAGE_REFRESH);
    }
}
