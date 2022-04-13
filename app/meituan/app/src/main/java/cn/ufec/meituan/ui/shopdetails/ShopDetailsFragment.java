package cn.ufec.meituan.ui.shopdetails;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ejlchina.okhttps.OkHttps;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.ufec.meituan.R;
import cn.ufec.meituan.adapter.ProductListAdapter;
import cn.ufec.meituan.adapter.ShoppingCarAdapter;
import cn.ufec.meituan.bean.ProductItemBean;
import cn.ufec.meituan.bean.ShoppingCartItemBean;
import cn.ufec.meituan.config.MeiTuanConfig;
import cn.ufec.meituan.databinding.FragmentShopDetailsBinding;
import cn.ufec.meituan.ui.order.OrderModel;

public class ShopDetailsFragment extends Fragment{
    private Bundle mArguments;
    private FragmentShopDetailsBinding mShopDetailsBinding;
    private Toolbar mToolbar;
    private static final String TAG = "ShopDetailsFragment";
    private final List<ProductItemBean.ProductBean> mProductList = new ArrayList<>(); // 商品列表
    private ProductListAdapter mProductListAdapter; // 商品(菜品列表Adapter)
    private FragmentActivity mFragmentActivity; // 当前FragmentActivity
    private DisplayMetrics mDisplayMetrics; // 设备屏幕相关
    private View mShoppingCarListView; // 购物车列表视图

    private final List<ShoppingCartItemBean> mShoppingCartList = new CopyOnWriteArrayList<>(); // 购物车数据
    private ShoppingCarAdapter mShoppingCarAdapter; // 购物车适配器

    private TextView mTVTotalPrice, mTVProductTotalCount, mBtnCheckOut; // 所有商品总价、商品总个数 结算按钮
    private float mTotalPrice;
    private OrderModel mOrderModel;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mDisplayMetrics = this.getResources().getDisplayMetrics();
        // 加载购物车列表view
        mShoppingCarListView = LayoutInflater.from(this.getContext()).inflate(R.layout.shopping_car_list, (ViewGroup) this.getView(), false);
        mOrderModel = new ViewModelProvider(requireActivity()).get(OrderModel.class);
        mOrderModel.setShoppingCartList(mShoppingCartList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (getArguments() != null) {
            mArguments = getArguments();
        }
        mFragmentActivity = requireActivity();
        // 需要沉浸式布局
        mFragmentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ImmersiveStatusBar();
        mShopDetailsBinding = FragmentShopDetailsBinding.inflate(inflater, container, false); // viewBind
        mToolbar = mShopDetailsBinding.toolbarShopDetails; // 标题栏
        mCollapsingToolbarLayout = mShopDetailsBinding.collapsingToolbarShopDetails;
        ((AppCompatActivity)requireActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity)requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(view -> {
            requireActivity().onBackPressed();
        });

        mTVTotalPrice = mShopDetailsBinding.tvShopDetailTotalPrice; // 所有商品总价
        mTVProductTotalCount = mShopDetailsBinding.tvShopDetailProductTotalCount; // 所有商品总数
        mBtnCheckOut = mShopDetailsBinding.btnCheckoutCart; // 结算按钮

        // 点击结算按钮
        mShopDetailsBinding.btnCheckoutCart.setOnClickListener(view -> navigateToOrder());
        // 点击购物车图标
        mShopDetailsBinding.cvShopDetailShoppingCar.setOnClickListener(view -> showShoppingCarList());


        RecyclerView rcvProductList = mShopDetailsBinding.rcvProductList; // 菜品列表
        mProductListAdapter = new ProductListAdapter(mProductList); // 实例化一个菜品adapter
        mProductListAdapter.setOnClickListener(this::onClickProductItem); // 设置点击事件监听器

        rcvProductList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rcvProductList.setAdapter(mProductListAdapter);
        updateDetailBottomBar();
        if (mTVProductTotalCount.getVisibility() == View.GONE && !mShoppingCartList.isEmpty()){
            mTVProductTotalCount.setVisibility(View.VISIBLE);
        }
        return mShopDetailsBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        loadData();
        mShoppingCarAdapter = new ShoppingCarAdapter(mShoppingCartList, true); // 实例化购物车列表Adapter
        // 点击增加按钮
        mShoppingCarAdapter.setOnClickIncListener((data, position) -> {
            data.setProductCount(data.getProductCount() + 1);
            mShoppingCarAdapter.notifyDataSetChanged();
            updateDetailBottomBar();
            if (mTVProductTotalCount.getVisibility() == View.GONE){
                mTVProductTotalCount.setVisibility(View.VISIBLE);
            }
        });
        // 点击减少按钮
        mShoppingCarAdapter.setOnClickDecListener((data, position) -> {
            if (data.getProductCount() - 1 != 0) {
                data.setProductCount(data.getProductCount() - 1);
            }else{
                mShoppingCartList.remove(position);
            }
            updateDetailBottomBar();
            mShoppingCarAdapter.notifyDataSetChanged();
            if (mTVProductTotalCount.getVisibility() == View.GONE){
                mTVProductTotalCount.setVisibility(View.VISIBLE);
            }
        });

        RecyclerView shoppingCartList = mShoppingCarListView.findViewById(R.id.rcv_shopping_cart_list);
        shoppingCartList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        shoppingCartList.setAdapter(mShoppingCarAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mShopDetailsBinding = null;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        OkHttps.async("/product")
                .bind(this)
                .tag(MeiTuanConfig.Tags.LOADING)
                .setOnResponse(res -> {
                    ProductItemBean bean = res.getBody().toBean(ProductItemBean.class);
                    mProductList.addAll(bean.data);
                    mProductListAdapter.notifyDataSetChanged();
                })
                .get();
    }

    /**
     * 跳转到订单结算页面
     */
    private void navigateToOrder() {
        if (mTotalPrice >= 15) {
            NavHostFragment.findNavController(ShopDetailsFragment.this).navigate(R.id.action_shopDetailsFragment_to_orderFragment);
        }
    }

    /**
     * 展示购物车列表
     */
    @SuppressLint("NotifyDataSetChanged")
    private void showShoppingCarList() {
        // 清空购物车按钮
        TextView emptyCart = mShoppingCarListView.findViewById(R.id.tv_shopping_car_list_empty_cart);
        PopupWindow popupWindow = new PopupWindow(mShoppingCarListView, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels / 4);
        popupWindow.setFocusable(true);//获取焦点
        popupWindow.setOverlapAnchor(false);
        popupWindow.setAttachedInDecor(true);

        // 点击清空购物车
        emptyCart.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(mFragmentActivity)
                    .setMessage("确定清空购物车吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        mShoppingCartList.clear();
                        mShoppingCarAdapter.notifyDataSetChanged();
                        mTVTotalPrice.setText("0");
                        updateDetailBottomBar();
                        mTVProductTotalCount.setVisibility(View.GONE);
                    })
                    .show();
        });
        // 监听弹出层消失
        popupWindow.setOnDismissListener(() -> this.setBackgroundAlpha(1) );
        // 设置背景透明度
        this.setBackgroundAlpha(.4f);
        // 展示购物车列表
        popupWindow.showAsDropDown(mShopDetailsBinding.shopDetailBottomBar, 0, 0);
    }

    /**
     * 展示菜品详情
     * @param product 点击的哪一个产品
     * @param position 第几个产品
     */
    public void showProductDetail(ProductItemBean.ProductBean product, int position) {
        View productDetailPopupView = LayoutInflater.from(this.getContext()).inflate(R.layout.product_detail_popup, (ViewGroup) getView(), false);
        PopupWindow popupWindow = new PopupWindow(productDetailPopupView, (int)(220 * mDisplayMetrics.density), (int)(240 * mDisplayMetrics.density));
        popupWindow.setFocusable(true);
        popupWindow.setOverlapAnchor(false);
        popupWindow.setAttachedInDecor(true);
        popupWindow.showAtLocation(this.getView(), Gravity.CENTER,0, 0);
        popupWindow.setOnDismissListener(() -> this.setBackgroundAlpha(1));
        setBackgroundAlpha(.4f);
    }

    /**
     * 添加到购物车
     * @param product 产品
     * @param position 哪一个产品
     */
    @SuppressLint("NotifyDataSetChanged")
    public void addToShoppingCar(ProductItemBean.ProductBean product, int position) {
        // 购物车列表为空直接添加
        if (mShoppingCartList.isEmpty()) {
            mShoppingCartList.add(new ShoppingCartItemBean(1, product));
        }else{
            synchronized (mShoppingCartList) {
                boolean flag = false;
                for (ShoppingCartItemBean shoppingCartItem: mShoppingCartList) {
                    // 购物车列表已经存在该产品 数量加1
                    if (shoppingCartItem.getProduct().getName().equals(product.getName())) {
                        flag = true;
                        shoppingCartItem.setProductCount(shoppingCartItem.getProductCount() + 1);
                    }
                }
                // 不存在则新增一项
                if (!flag) {
                    mShoppingCartList.add(new ShoppingCartItemBean(1, product));
                }
            }
        }
        // 通知适配器数据更新
        mShoppingCarAdapter.notifyDataSetChanged();
        updateDetailBottomBar();
        if (mTVProductTotalCount.getVisibility() == View.GONE){
            mTVProductTotalCount.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 响应点击商品列表事件
     * @param product 点击的商品
     * @param position 列表第几个
     * @param widget 点击的是那个控件
     */
    public void onClickProductItem(ProductItemBean.ProductBean product, int position, View widget) {
        if (widget instanceof MaterialButton) {
            this.addToShoppingCar(product, position);
        }else{
            this.showProductDetail(product, position);
        }
    }

    /**
     * 设置背景透明度
     * @param f 透明度
     */
    private void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = mFragmentActivity.getWindow().getAttributes();
        layoutParams.alpha = f;
        mFragmentActivity.getWindow().setAttributes(layoutParams);
    }

    /**
     * 更新底部
     */
    @SuppressLint("SetTextI18n")
    public void updateDetailBottomBar() {
        mTotalPrice = .0f;
        int totalCount = 0;
        for (ShoppingCartItemBean shoppingCartItem: mShoppingCartList) {
            totalCount += shoppingCartItem.getProductCount();
            mTotalPrice += shoppingCartItem.getProduct().getPrice() * shoppingCartItem.getProductCount();
        }
        if (totalCount > 999) {
            mTVTotalPrice.setText("999+");
        }else{
            mTVTotalPrice.setText("￥"+ mTotalPrice);
        }
        mTVProductTotalCount.setText(""+totalCount);
        checkoutBtnStatus(mTotalPrice);
        mOrderModel.setShoppingCartList(mShoppingCartList);
    }

    @SuppressLint({ "SetTextI18n", "ResourceAsColor" })
    public void checkoutBtnStatus(float price) {
        if (price >= 15) {
            mBtnCheckOut.setText("去结算");
            mBtnCheckOut.setBackgroundResource(R.color.primary);
        }else{
            mBtnCheckOut.setBackgroundResource(R.color.dark_grey);
            mBtnCheckOut.setText("还差￥" + (15 - price) + "起送");
        }
    }

    private void ImmersiveStatusBar() {

    }
}