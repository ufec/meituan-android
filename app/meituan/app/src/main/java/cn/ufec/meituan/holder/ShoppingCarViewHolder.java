package cn.ufec.meituan.holder;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import cn.ufec.meituan.R;
import cn.ufec.meituan.bean.ProductItemBean;
import cn.ufec.meituan.bean.ShoppingCartItemBean;

public class ShoppingCarViewHolder extends RecyclerView.ViewHolder {
    public MaterialTextView mTVProductName, mTotalPrice, mProductCount; // 商品名称，商品总价，商品数量
    public ImageFilterButton mBTNDecProduct, mBTNIncProduct; // 减少增加按钮

    public ShoppingCarViewHolder(@NonNull View itemView) {
        super(itemView);
        mTVProductName = itemView.findViewById(R.id.tv_shopping_car_item_product_name);
        mProductCount = itemView.findViewById(R.id.tv_shopping_car_item_product_count);
        mTotalPrice = itemView.findViewById(R.id.tv_shopping_car_item_product_total_price);
        mBTNDecProduct = itemView.findViewById(R.id.btn_shopping_car_item_inc_product);
        mBTNIncProduct = itemView.findViewById(R.id.btn_shopping_car_item_dec_product);
    }

    @SuppressLint("SetTextI18n")
    public void setData(ShoppingCartItemBean shoppingCartItem) {
        ProductItemBean.ProductBean product = shoppingCartItem.getProduct();
        mTVProductName.setText(product.getName());
        mProductCount.setText("" + shoppingCartItem.getProductCount());
        mTotalPrice.setText("" + (shoppingCartItem.getProductCount() * product.getPrice()));
    }
}
