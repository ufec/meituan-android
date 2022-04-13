package cn.ufec.meituan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.ufec.meituan.R;
import cn.ufec.meituan.bean.ProductItemBean;
import cn.ufec.meituan.holder.ProductListViewHolder;

/**
 * 商品列表适配器
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListViewHolder> {
    private List<ProductItemBean.ProductBean> mProducts;

    // 点击菜品列表监听器
    private OnClickProductItem<ProductItemBean.ProductBean> mOnClickItemListener;

    public ProductListAdapter(List<ProductItemBean.ProductBean> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载视图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        holder.setData(mProducts, position);
        // 相应加入购物车
        holder.mMaterialButtonAddToShoppingCar.setOnClickListener(v -> {
            mOnClickItemListener.onClickProductItem(mProducts.get(position), position, holder.mMaterialButtonAddToShoppingCar);
        });
        // 响应 点击除了购物车之外的其他
        holder.mProductName.setOnClickListener(v -> {
            mOnClickItemListener.onClickProductItem(mProducts.get(position), position, holder.mProductName);
        });
        holder.mProductPrice.setOnClickListener(v -> {
            mOnClickItemListener.onClickProductItem(mProducts.get(position), position, holder.mProductPrice);
        });
        holder.mProductSalesRanking.setOnClickListener(v -> {
            mOnClickItemListener.onClickProductItem(mProducts.get(position), position, holder.mProductSalesRanking);
        });
        holder.mProductThumbnail.setOnClickListener(v -> {
            mOnClickItemListener.onClickProductItem(mProducts.get(position), position, holder.mProductThumbnail);
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    /**
     * 设置点击事件监听器
     * @param onClickListener 点击事件监听器
     */
    public void setOnClickListener(OnClickProductItem<ProductItemBean.ProductBean> onClickListener) {
        mOnClickItemListener = onClickListener;
    }

    public interface OnClickProductItem<T> {
        void onClickProductItem(T product, int position, View widget);
    }
}
