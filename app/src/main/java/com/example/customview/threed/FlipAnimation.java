package com.example.customview.threed;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/***
 * @date 2019-10-14 10:39
 * @author BoXun.Zhao
 * @description
 */
public class FlipAnimation extends Animation {
    private float fromDegree;
    private float toDegree;
    private float flingDegree = Integer.MAX_VALUE;
    private float endDegree = Integer.MAX_VALUE;
    private float centerX = 0;
    private float centerY = 0;
    private Camera camera = new Camera();
    private float toRatio;
    private float flingRatio;
    private boolean needFling;
    private float scale;

    FlipAnimation(Context context, float fromDegree, float toDegree) {
        this.fromDegree = fromDegree;
        this.toDegree = toDegree;
        scale = context.getResources().getDisplayMetrics().density;
    }

    FlipAnimation(Context context, float fromDegree, float toDegree, float flingDegree, float endDegree) {
        this.fromDegree = fromDegree;
        this.toDegree = toDegree;
        this.flingDegree = flingDegree;
        this.endDegree = endDegree;
        needFling = flingDegree != Integer.MAX_VALUE && endDegree != Integer.MAX_VALUE;
        if (needFling) {
            float toDiff = Math.abs(toDegree - fromDegree);
            float flingDiff = Math.abs(flingDegree - toDegree);
            float endDiff = Math.abs(endDegree - flingDegree);
            float totalDiff = toDiff + flingDiff + endDiff;
            toRatio = toDiff / totalDiff;
            flingRatio = (flingDiff + toDiff) / totalDiff;
        }
        scale = context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width >> 1;
        centerY = height >> 1;
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degree = 0;
        if (needFling) {
            if (interpolatedTime <= toRatio) {
                degree = fromDegree + (toDegree - fromDegree) * interpolatedTime;
            } else if (interpolatedTime <= flingRatio) {
                degree = toDegree + (flingDegree - toDegree) * interpolatedTime;
            } else {
                degree = flingDegree + (endDegree - flingDegree) * interpolatedTime;
            }
        } else {
            degree = fromDegree + (toDegree - fromDegree) * interpolatedTime;
        }

        Matrix matrix = t.getMatrix();
        camera.save();
        camera.rotateY(degree);
        camera.getMatrix(matrix);
        camera.restore();

        float[] mValues = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        matrix.getValues(mValues);
        mValues[6] = mValues[6] / scale / 2;
        matrix.setValues(mValues);

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
