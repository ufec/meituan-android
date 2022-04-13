package cn.ufec.meituan.holder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import cn.ufec.meituan.R;
import cn.ufec.meituan.bean.ProductItemBean;
import cn.ufec.meituan.bean.ShoppingCartItemBean;

public class CheckOutShoppingCartViewHolder extends RecyclerView.ViewHolder {
    public ShapeableImageView mIVProductThumbnail;
    public TextView mTVProductName, mTVProductCount, mProductTotalPrice;

    public CheckOutShoppingCartViewHolder(@NonNull View itemView) {
        super(itemView);
        mIVProductThumbnail = itemView.findViewById(R.id.iv_check_out_shopping_car_item_thumbnail);
        mTVProductName = itemView.findViewById(R.id.tv_check_out_shopping_car_item_product_name);
        mTVProductCount = itemView.findViewById(R.id.tv_check_out_shopping_car_item_product_count);
        mProductTotalPrice = itemView.findViewById(R.id.tv_check_out_shopping_car_item_product_total_price);
    }

    @SuppressLint("SetTextI18n")
    public void setData(ShoppingCartItemBean shoppingCartList) {
        ProductItemBean.ProductBean product = shoppingCartList.getProduct();
        mTVProductCount.setText("x" + shoppingCartList.getProductCount());
        mProductTotalPrice.setText("￥" + (shoppingCartList.getProductCount() * product.getPrice()));
        mTVProductName.setText(product.getName());
    }
}
