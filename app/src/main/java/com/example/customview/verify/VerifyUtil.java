package com.example.customview.verify;

import android.content.Context;

/***
 * @date 2019-10-31 14:02
 * @author BoXun.Zhao
 * @description
 */
public class VerifyUtil {

    public static int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5);
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
