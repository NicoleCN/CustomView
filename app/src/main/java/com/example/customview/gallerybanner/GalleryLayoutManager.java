package com.example.customview.gallerybanner;

import androidx.recyclerview.widget.RecyclerView;

/***
 * @date 2019-09-29 13:59
 * @author BoXun.Zhao
 * @description
 */
public class GalleryLayoutManager extends RecyclerView.LayoutManager {
    /**
     * scroll time
     */
    private float mInterval;
    /**
     * itemSpace
     */
    private int mItemSpace;


    public void setItemSpace(int itemSpace) {
        mItemSpace = itemSpace;
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }
}
