package cn.ufec.meituan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.ufec.meituan.R;
import cn.ufec.meituan.bean.ShoppingCartItemBean;
import cn.ufec.meituan.holder.CheckOutShoppingCartViewHolder;
import cn.ufec.meituan.holder.ShoppingCarViewHolder;

/**
 * 购物车列表适配器
 */
public class ShoppingCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final boolean mIsShoppingCart;
    private OnClickShoppingCartItem<ShoppingCartItemBean> mIncListener, mDecListener;

    private final List<ShoppingCartItemBean> mShoppingCartList;

    public ShoppingCarAdapter(List<ShoppingCartItemBean> shoppingCartList, boolean isShoppingCart) {
        mShoppingCartList = shoppingCartList;
        mIsShoppingCart = isShoppingCart;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mIsShoppingCart) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_car_item, parent, false);
            return new ShoppingCarViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_out_shopping_car_item, parent, false);
            return new CheckOutShoppingCartViewHolder(view);
        }
    }

    /**
     * 绑定购物车数据视图
     * @param holder 购物车所在view
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ShoppingCarViewHolder) {
            // 购物车
            ShoppingCarViewHolder holder1 = (ShoppingCarViewHolder) holder;
            // 减少按钮
            holder1.mBTNDecProduct.setOnClickListener(view -> {
                mDecListener.onClick(mShoppingCartList.get(position), position);
            });
            // 增加按钮
            holder1.mBTNIncProduct.setOnClickListener(view -> {
                mIncListener.onClick(mShoppingCartList.get(position), position);
            });
            // 设置数据
            holder1.setData(mShoppingCartList.get(position));
        }else if (holder instanceof CheckOutShoppingCartViewHolder) {
            CheckOutShoppingCartViewHolder holder2 = (CheckOutShoppingCartViewHolder) holder;
            holder2.setData(mShoppingCartList.get(position));
        }
    }

    /**
     * 获取购物车数据
     * @return 购物车有多少项
     */
    @Override
    public int getItemCount() {
        return mShoppingCartList.size();
    }

    public void setOnClickIncListener(OnClickShoppingCartItem<ShoppingCartItemBean> listener) {
        mIncListener = listener;
    }

    public void setOnClickDecListener(OnClickShoppingCartItem<ShoppingCartItemBean> listener) {
        mDecListener = listener;
    }

    public interface OnClickShoppingCartItem<T> {
        void onClick(T data, int position);
    }
}
