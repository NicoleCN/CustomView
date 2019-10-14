package com.example.customview.threed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.customview.R;

/***
 * @date 2019-10-14 10:30
 * @author BoXun.Zhao
 * @description
 */
public abstract class FlipLayout extends LinearLayout {
    private static final int ANIM_DURATION = 500;
    private static final int ANIM_DEGREE = 90;
    private static final int FLING_FRONT_DEGREE = 30;
    private static final int FLING_BACK_DEGREE = -45;
    private LinearLayout mBackLinearLayout;
    private LinearLayout mFrontLinearLayout;
    private FlipAnimation mFrontAnim;
    private FlipAnimation mBackAnim;
    private boolean isBack;
    private boolean isFling = true;

    public FlipLayout(Context context) {
        this(context, null);
    }

    public FlipLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlipLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.flip_layout, this);
        initView();
    }

    private void initView() {
        mFrontLinearLayout = findViewById(R.id.flip_front_LinearLayout);
        mBackLinearLayout = findViewById(R.id.flip_back_LinearLayout);
        mFrontLinearLayout.addView(getFrontChildView());
        mBackLinearLayout.addView(getBackChildView());
        int distance = 100;
        float scale = getContext().getResources().getDisplayMetrics().density * distance;
        mFrontLinearLayout.setCameraDistance(scale);
        mBackLinearLayout.setCameraDistance(scale);
    }

    protected abstract View getBackChildView();

    protected abstract View getFrontChildView();

    public void flip() {
        if (isBack) {
            back2Front();
        } else {
            front2Back();
        }
    }

    /**
     * 正->反
     */
    private void front2Back() {
        mFrontAnim = new FlipAnimation(getContext(), 0, ANIM_DEGREE);
        if (isFling) {
            mBackAnim = new FlipAnimation(getContext(), -ANIM_DEGREE, FLING_FRONT_DEGREE);
        } else {
            mBackAnim = new FlipAnimation(getContext(), -ANIM_DEGREE, 0);
        }
        mFrontAnim.setDuration(ANIM_DURATION);
        mBackAnim.setDuration(ANIM_DURATION);
        mFrontAnim.setInterpolator(new LinearInterpolator());
        mBackAnim.setInterpolator(new LinearInterpolator());
        mBackAnim.setFillAfter(true);
        mFrontAnim.setAnimationListener(new AnimationListerImp() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mFrontLinearLayout.setVisibility(GONE);
                mBackLinearLayout.setVisibility(VISIBLE);
                mBackLinearLayout.startAnimation(mBackAnim);
                isBack = true;
            }
        });
        if (isFling) {
            mBackAnim.setAnimationListener(new AnimationListerImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    FlipAnimation flipAnimation = new FlipAnimation(getContext(), FLING_FRONT_DEGREE, FLING_FRONT_DEGREE, FLING_BACK_DEGREE, 0);
                    flipAnimation.setInterpolator(new AccelerateInterpolator());
                    flipAnimation.setDuration(ANIM_DURATION);
                    mBackLinearLayout.startAnimation(flipAnimation);
                }
            });
        }
        mFrontLinearLayout.startAnimation(mFrontAnim);
    }

    /**
     * 反->正
     */
    private void back2Front() {
        if (isFling) {
            mFrontAnim = new FlipAnimation(getContext(), -ANIM_DEGREE, FLING_FRONT_DEGREE);
        } else {
            mFrontAnim = new FlipAnimation(getContext(), -ANIM_DEGREE, 0);
        }
        mBackAnim = new FlipAnimation(getContext(), 0, ANIM_DEGREE);
        mFrontAnim.setDuration(ANIM_DURATION);
        mBackAnim.setDuration(ANIM_DURATION);
        mFrontAnim.setInterpolator(new LinearInterpolator());
        mBackAnim.setInterpolator(new LinearInterpolator());
        mFrontAnim.setFillAfter(true);
        mBackAnim.setAnimationListener(new AnimationListerImp() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mBackLinearLayout.setVisibility(GONE);
                mFrontLinearLayout.setVisibility(VISIBLE);
                mFrontLinearLayout.startAnimation(mFrontAnim);
                isBack = false;
            }

        });
        if (isFling) {
            mFrontAnim.setAnimationListener(new AnimationListerImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    FlipAnimation flipAnimation = new FlipAnimation(getContext(), FLING_FRONT_DEGREE, FLING_FRONT_DEGREE, FLING_BACK_DEGREE, 0);
                    flipAnimation.setInterpolator(new AccelerateInterpolator());
                    flipAnimation.setDuration(ANIM_DURATION);
                    mFrontLinearLayout.startAnimation(flipAnimation);
                }
            });
        }
        mBackLinearLayout.startAnimation(mBackAnim);
    }

    private class AnimationListerImp implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
