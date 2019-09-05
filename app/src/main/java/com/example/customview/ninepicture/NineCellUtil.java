package com.example.customview.ninepicture;

import android.content.res.Resources;

/***
 * @date 2019/9/5 9:41
 * @author BoXun.Zhao
 * @description
 */
public class NineCellUtil {
    public static float getDisplayDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }
}
