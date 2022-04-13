package cn.ufec.meituan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.ufec.meituan.R;
import cn.ufec.meituan.bean.ShopListBean;
import cn.ufec.meituan.holder.ShopListViewHolder;

/**
 * 店铺列表适配器
 */
public class ShopListAdapter extends RecyclerView.Adapter<ShopListViewHolder> {
    private List<ShopListBean.ShopItemBean> mShopList = new CopyOnWriteArrayList<>();
    private OnShopItemClickListener<ShopListBean.ShopItemBean> mOnClickListener;

    public ShopListAdapter(List<ShopListBean.ShopItemBean> ShopItem) {
        mShopList = ShopItem;
    }

    public ShopListAdapter() {
    }

    @NonNull
    @Override
    public ShopListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new ShopListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListViewHolder holder, int position) {
        if (mShopList != null) {
            holder.itemView.setOnClickListener(view -> {
                mOnClickListener.OnClickListener(mShopList.get(position), position);
            });
            holder.setData(mShopList, position);
        }
    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }

    public void setOnClickListener(OnShopItemClickListener<ShopListBean.ShopItemBean> onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnShopItemClickListener<T> {
        void OnClickListener(T data, int position);
    }

    public void setShopList(List<ShopListBean.ShopItemBean> shopList) {
        mShopList = shopList;
    }
}
