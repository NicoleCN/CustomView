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
        /**
         * cosx -sinx translateX
         * sinx cosx  translateY
         * 0      0   scale
         */
        float[] mValues = {
                0, 0, 0,
                0, 0, 0,
                0, 0, 0};

        matrix.getValues(mValues);
        mValues[6] = mValues[6] / scale / 2;
        matrix.setValues(mValues);

        //pre其实执行的就是右乘的
        //setScale前
        //matrix操作理解的话 把正方向和android坐标系反一下 好理解
        matrix.preTranslate(-centerX, -centerY);
        //post执行的就是左乘的操作
        //setScale后
        matrix.postTranslate(centerX, centerY);
        //post是后乘，当前的矩阵乘以参数给出的矩阵。可以连续多次使用post，来完成所需的整个变换。例如，要将一个图片旋转30度，然后平移到(100,100)的地方，那么可以这样做:Matrix m =  new  Matrix();  m.postRotate(30 );  m.postTranslate(100 ,  100 ); 这样就达到了想要的效果。
        //pre是前乘，参数给出的矩阵乘以当前的矩阵。所以操作是在当前矩阵的最前面发生的。例如上面的例子，如果用pre的话，就要这样:Matrix m =  new  Matrix(); m.setTranslate(100 ,  100 );  m.preRotate(30 );
    }
}
