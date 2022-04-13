package cn.ufec.meituan.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;

import java.util.List;

import cn.ufec.meituan.GlideApp;
import cn.ufec.meituan.bean.ShopListBean;
import cn.ufec.meituan.holder.BannerViewHolder;
import cn.ufec.meituan.views.banner.adapter.BannerAdapter;

public class BannerImageAdapter extends BannerAdapter<ShopListBean.ShopItemBean, BannerViewHolder> {

    public BannerImageAdapter(List<ShopListBean.ShopItemBean> adapterData) {
        super(adapterData);
    }

    public BannerImageAdapter() {
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, ShopListBean.ShopItemBean data, int position, int size) {
        GlideApp.with(holder.itemView)
                .load(data.getShopThumbnail())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(holder.mImageView);
    }

    public void setData(List<ShopListBean.ShopItemBean> adapterData) {
        super.setData(adapterData);
    }
}
