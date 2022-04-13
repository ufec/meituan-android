package cn.ufec.meituan.views.banner.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 默认的ImageViewHolder
 * @author xuyi
 */
public class BannerImageHolder extends RecyclerView.ViewHolder {

    public ImageView mImageView;

    public BannerImageHolder(@NonNull View itemView) {
        super(itemView);
        this.mImageView = (ImageView) itemView;
    }
}
