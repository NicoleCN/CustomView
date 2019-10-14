package com.example.customview.gallerybanner;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/***
 * @date 2019-09-29 13:41
 * @author BoXun.Zhao
 * @description
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private List<GalleryBean> mList;

    public GalleryAdapter(List<GalleryBean> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryViewHolder(new View(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        holder.bindData(mList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(GalleryBean galleryBean, int position){

        }
    }
}
