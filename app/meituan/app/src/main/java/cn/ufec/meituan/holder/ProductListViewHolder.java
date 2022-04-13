package cn.ufec.meituan.holder;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import cn.ufec.meituan.R;
import cn.ufec.meituan.bean.ProductItemBean;

/**
 * 店铺商品列表 ViewHolder
 */
public class ProductListViewHolder extends RecyclerView.ViewHolder {
    public ShapeableImageView mProductLabel; // 菜品标签
    public ShapeableImageView mProductThumbnail; // 菜品缩略图

    public MaterialTextView mProductName; // 菜品标题
    public MaterialTextView mProductSalesRanking; // 菜品销量排名()
    public MaterialTextView mProductSales; // 菜品销量、好评度
    public MaterialTextView mProductPrice; // 菜品价格

    public MaterialButton mMaterialButtonAddToShoppingCar;// 加入购物车按钮


    public ProductListViewHolder(@NonNull View itemView) {
        super(itemView);
        mProductLabel = itemView.findViewById(R.id.iv_product_label);
        mProductThumbnail = itemView.findViewById(R.id.iv_product_thumbnail);
        mProductName = itemView.findViewById(R.id.tv_product_name);
        mProductSalesRanking = itemView.findViewById(R.id.tv_product_sales_ranking);
        mProductSales = itemView.findViewById(R.id.tv_product_sales);
        mProductPrice = itemView.findViewById(R.id.tv_product_price);
        mMaterialButtonAddToShoppingCar = itemView.findViewById(R.id.btn_add_to_shopping_car);
    }

    @SuppressLint("SetTextI18n")
    public void setData(List<ProductItemBean.ProductBean> productBean, int position) {
        ProductItemBean.ProductBean product = productBean.get(position);
        mProductName.setText(product.getName()); // 商品名
        mProductSales.setText(product.getMonthSales()); // 月销量
        mProductSalesRanking.setText(product.getRecommendReason() + " " + product.getLikeRatioDesc()); // 推荐理由（排名）
        mProductPrice.setText("￥" + product.getPrice());
    }
}
