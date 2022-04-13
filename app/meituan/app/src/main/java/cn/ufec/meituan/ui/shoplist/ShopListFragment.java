package cn.ufec.meituan.ui.shoplist;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.ejlchina.okhttps.OkHttps;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.ufec.meituan.R;
import cn.ufec.meituan.adapter.BannerImageAdapter;
import cn.ufec.meituan.adapter.ShopListAdapter;
import cn.ufec.meituan.bean.ShopListBean;
import cn.ufec.meituan.config.MeiTuanConfig;
import cn.ufec.meituan.databinding.FragmentShopListBinding;
import cn.ufec.meituan.views.banner.Banner;

public class ShopListFragment extends Fragment {
    private FragmentShopListBinding mShopListBinding;
    private Toolbar mToolbar;
    private AppCompatActivity mFragmentActivity;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRCVShopList;
    private ShopListAdapter mShopListAdapter;
    private final int mBannerLength = 4;
    private List<ShopListBean.ShopItemBean> mShopList = new CopyOnWriteArrayList<>();
    private BannerImageAdapter mBannerImageAdapter;
    private Banner<ShopListBean.ShopItemBean, BannerImageAdapter> mAdBanner;
    private LinearLayoutManager mRCVLinearLayoutManager;
    private boolean mIsLoadingMore = false;
    private final String TAG = "ShopListFragment";


    @SuppressWarnings("unchecked")
    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        loadData(0);
        // 初始化 viewBinding
        mShopListBinding = FragmentShopListBinding.inflate(inflater, container, false);
        // 移除 FLAG_TRANSLUCENT_STATUS 因为在 ShopDetailsFragment 设置了，返回时会保留，所以在创建视图时清除就好了
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 绑定下拉刷新布局
        mRefreshLayout = mShopListBinding.swipeRefreshShopList;
        // 绑定 RecyclerView 布局
        mRCVShopList = mShopListBinding.rcvShopList;

        // 实例化一个适配器
        mShopListAdapter = new ShopListAdapter();
        // 为 RecyclerView 列表中每个 Item 设置点击事件监听器 来自 OnShopItemClickListener 接口
        mShopListAdapter.setOnClickListener((data, position) -> navigateToShopDetail());
        // 为 RecyclerView 设置适配器
        mRCVShopList.setAdapter(mShopListAdapter);

        // 绑定 ToolBar
        mToolbar = mShopListBinding.toolbarShopList;
        // 绑定Banner
        mAdBanner = (Banner<ShopListBean.ShopItemBean, BannerImageAdapter>) mShopListBinding.bannerAdBanner;

        mBannerImageAdapter = new BannerImageAdapter();// 实例化 banner 适配器
        mBannerImageAdapter.setOnClickListener((data, position) -> navigateToShopDetail());
        mAdBanner.setAdapter(mBannerImageAdapter);

        // 下拉刷新
        mRefreshLayout.setOnRefreshListener(() -> {
            mAdBanner.stopLoop();
            mShopList.clear(); // 清空原数据
            new Handler().postDelayed((Runnable) () -> {
                loadData(0); // 重新加载数据
                mRefreshLayout.setRefreshing(false);
                mAdBanner.startLoop();
            }, 2000);
        });

        // 上拉加载
        mRCVShopList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("TAG", "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mRCVLinearLayoutManager.findLastVisibleItemPosition() == mShopListAdapter.getItemCount() - 1) {
                    if (mRefreshLayout.isRefreshing()) {
                        mShopListAdapter.notifyItemRemoved(mShopListAdapter.getItemCount());
                        return;
                    }
                    if (!mIsLoadingMore) {
                        mIsLoadingMore = true;
                        new Handler().postDelayed((Runnable) () -> {
                            loadData(mShopList.size()); // 重新加载数据
                            Log.d("TAG", "下拉加载");
                            mIsLoadingMore = false;
                        }, 2000);
                    }
                }
                Log.d("TAG", "onScrolled: dx -> " + dx + " dy -> " + dy + " lastPosition -> " + mRCVLinearLayoutManager.findLastVisibleItemPosition());
            }
        });

        // 设置 RecyclerView 的布局方式 有 LinearLayoutManager(线性布局)、GridLayoutManager(宫格布局)、StaggeredGridLayoutManager(瀑布流布局)
        mRCVLinearLayoutManager = new LinearLayoutManager(this.getContext());
        mRCVShopList.setLayoutManager(mRCVLinearLayoutManager);
        mFragmentActivity = (AppCompatActivity) requireActivity();
        mFragmentActivity.setSupportActionBar(mToolbar);
        return mShopListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
        mShopListBinding = null;
        mBannerImageAdapter = null;
        mAdBanner = null;
        mShopList = new CopyOnWriteArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadData(int start) {
        OkHttps.async("/shoplist")
                .bind(this)
                .addUrlPara("start", start)
                .tag(MeiTuanConfig.Tags.LOADING)
                .setOnResponse(res -> {
                    ShopListBean shopList = res.getBody().toBean(ShopListBean.class);
                    mShopList.addAll(start, shopList.getData());
                    if (mBannerImageAdapter != null) {
                        mBannerImageAdapter.setData(mShopList.subList(0, mBannerLength));
                        mBannerImageAdapter.notifyDataSetChanged();
                    }

                    if (mShopListAdapter != null) {
                        mShopListAdapter.setShopList(mShopList.subList(mBannerLength, mShopList.size()));
                        mShopListAdapter.notifyDataSetChanged();
                    }
                })
                .get();
    }

    private void navigateToShopDetail() {
        NavHostFragment.findNavController(ShopListFragment.this).navigate(R.id.action_shopListFragment_to_shopDetailsFragment);
    }
}