package cn.ufec.meituan.holder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.ufec.meituan.GlideApp;
import cn.ufec.meituan.R;
import cn.ufec.meituan.bean.ShopListBean;

/**
 * 每个店铺的ViewHolder(规定了每个店铺的外观(由哪些View构成))
 */
public class ShopListViewHolder extends RecyclerView.ViewHolder {
    public ImageView mThumbnail; // 店铺缩略图
    public TextView mShopName; // 店铺名
    public TextView mShopSales; // 店铺销量
    public TextView mDeliveryCondition; // 配送条件
    public TextView mRegionalRanking; // 地区排名
    public TextView mDeliveryDuration; // 配送时间

    public ShopListViewHolder(@NonNull View itemView) {
        super(itemView);
        // 绑定视图
        mThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        mShopName = itemView.findViewById(R.id.tv_shop_title);
        mShopSales = itemView.findViewById(R.id.tv_sales);
        mDeliveryCondition = itemView.findViewById(R.id.tv_delivery_condition);
        mRegionalRanking = itemView.findViewById(R.id.tv_regional_ranking);
        mDeliveryDuration = itemView.findViewById(R.id.tv_delivery_duration);
    }

    @SuppressLint("SetTextI18n")
    public void setData(List<ShopListBean.ShopItemBean> shopItemData, int position) {
        ShopListBean.ShopItemBean bean = shopItemData.get(position);
        GlideApp.with(this.itemView).load(bean.getShopThumbnail()).into(this.mThumbnail);
        mShopName.setText(bean.getShopName());
        mShopSales.setText(bean.getShopMonthSales());
        mDeliveryDuration.setText(bean.getDeliveryTime());
        mRegionalRanking.setText(bean.getRecommendReason());
        mDeliveryCondition.setText(bean.getMinPrice() + " | " + bean.getShippingFee());
    }
}
