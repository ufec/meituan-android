package cn.ufec.meituan.ui.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import cn.ufec.meituan.bean.ShoppingCartItemBean;

public class OrderModel extends ViewModel {

    private final MutableLiveData<List<ShoppingCartItemBean>> mShoppingCartList = new MutableLiveData<>();

    public OrderModel() {
        super();
    }

    public MutableLiveData<List<ShoppingCartItemBean>> getShoppingCartList() {
        return mShoppingCartList;
    }

    public void setShoppingCartList(List<ShoppingCartItemBean> shoppingCartList) {
        mShoppingCartList.setValue(shoppingCartList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
