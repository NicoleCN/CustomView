package com.example.customview.gallerybanner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/***
 * @date 2019-09-29 13:59
 * @author BoXun.Zhao
 * @description
 */
public class GallerySnapHelper extends RecyclerView.OnFlingListener {
    private RecyclerView mRecyclerView;

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        return false;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return;
        }
        if (mRecyclerView != null) {
//            destroyCallbacks();
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            /*final LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (!(layoutManager instanceof BannerLayoutManager)) {
                return;
            }

            setupCallbacks();
            mGravityScroller = new Scroller(mRecyclerView.getContext(),
                    new DecelerateInterpolator());

            snapToCenterView((BannerLayoutManager) layoutManager,
                    ((BannerLayoutManager) layoutManager).onPageChangeListener);*/
        }
    }
}
