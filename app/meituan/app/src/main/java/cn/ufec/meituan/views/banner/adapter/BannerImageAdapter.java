package cn.ufec.meituan.views.banner.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.ufec.meituan.views.banner.holder.BannerImageHolder;

/**
 * 默认的图片适配器
 */
public abstract class BannerImageAdapter<T> extends BannerAdapter<T, BannerImageHolder>{
    public BannerImageAdapter(List<T> adapterData) {
        super(adapterData);
    }

    @Override
    public BannerImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerImageHolder(imageView);
    }
}
