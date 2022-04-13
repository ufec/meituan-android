package cn.ufec.meituan.ui.order;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.ufec.meituan.R;
import cn.ufec.meituan.adapter.ShoppingCarAdapter;
import cn.ufec.meituan.bean.ShoppingCartItemBean;
import cn.ufec.meituan.databinding.FragmentOrderBinding;
import cn.ufec.meituan.util.QRCode;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding mOrderBinding;
    private OrderModel mOrderModel;
    private RecyclerView mRcvOrderList;
    private float totalPrice = .0f;
    private DisplayMetrics mDisplayMetrics;
    private List<String> mOrderInfo = new ArrayList<>();
    private Toolbar mToolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisplayMetrics = this.getResources().getDisplayMetrics();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOrderBinding = FragmentOrderBinding.inflate(inflater, container, false);
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mRcvOrderList = mOrderBinding.rcvOrderList;
        mToolbar = mOrderBinding.toolbarOrder;
        ((AppCompatActivity)requireActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity)requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(view -> {
            requireActivity().onBackPressed();
        });
        return mOrderBinding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOrderModel = new ViewModelProvider(requireActivity()).get(OrderModel.class);
        mOrderModel.getShoppingCartList().observe(getViewLifecycleOwner(), shoppingCartList -> {
            ShoppingCarAdapter shoppingCarAdapter = new ShoppingCarAdapter(shoppingCartList, false);
            mRcvOrderList.setLayoutManager(new LinearLayoutManager(requireActivity()));
            mRcvOrderList.setAdapter(shoppingCarAdapter);
            for (ShoppingCartItemBean shoppingCartItem : shoppingCartList) {
                int count = shoppingCartItem.getProductCount();
                float price = shoppingCartItem.getProduct().getPrice();
                String name = shoppingCartItem.getProduct().getName();
                mOrderInfo.add("菜品名：" + name + "\n单价：" + price + "\n购买" + count + "件, 价钱：" + count * price + "\n");
                totalPrice += count * price;
            }
            mOrderBinding.tvOrderSubtotal.setText("小计 ￥" + totalPrice);
            mOrderBinding.tvOrderSumPrice.setText("总价 ￥" + (totalPrice + 14));
        });
        mOrderBinding.btnOrderPay.setOnClickListener(v -> buildQRCode());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mOrderBinding = null;
        mRcvOrderList = null;
    }

    /**
     * 生成二维码
     */
    private void buildQRCode() {
        int w = (int)(mDisplayMetrics.density * 250);
        Bitmap bitmap = QRCode.createQRCodeBitmap(mOrderInfo.toString(), w, w);
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.qrcode_popup, (ViewGroup) requireView(), false);
        ShapeableImageView imageView = view.findViewById(R.id.iv_qrcode_popup_qrcode_image);
        imageView.setImageBitmap(bitmap);
        PopupWindow popupWindow = new PopupWindow(view, w, w);
        popupWindow.setFocusable(true);
        popupWindow.setOverlapAnchor(false);
        popupWindow.setAttachedInDecor(true);
        popupWindow.showAtLocation(this.getView(), Gravity.CENTER,0, 0);
        popupWindow.setOnDismissListener(() -> this.setBackgroundAlpha(1));
        setBackgroundAlpha(.4f);
    }

    /**
     * 设置背景透明度
     * @param f 透明度
     */
    private void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = requireActivity().getWindow().getAttributes();
        layoutParams.alpha = f;
        requireActivity().getWindow().setAttributes(layoutParams);
    }
}