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
        // ????????? viewBinding
        mShopListBinding = FragmentShopListBinding.inflate(inflater, container, false);
        // ?????? FLAG_TRANSLUCENT_STATUS ????????? ShopDetailsFragment ????????????????????????????????????????????????????????????????????????
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // ????????????????????????
        mRefreshLayout = mShopListBinding.swipeRefreshShopList;
        // ?????? RecyclerView ??????
        mRCVShopList = mShopListBinding.rcvShopList;

        // ????????????????????????
        mShopListAdapter = new ShopListAdapter();
        // ??? RecyclerView ??????????????? Item ??????????????????????????? ?????? OnShopItemClickListener ??????
        mShopListAdapter.setOnClickListener((data, position) -> navigateToShopDetail());
        // ??? RecyclerView ???????????????
        mRCVShopList.setAdapter(mShopListAdapter);

        // ?????? ToolBar
        mToolbar = mShopListBinding.toolbarShopList;
        // ??????Banner
        mAdBanner = (Banner<ShopListBean.ShopItemBean, BannerImageAdapter>) mShopListBinding.bannerAdBanner;

        mBannerImageAdapter = new BannerImageAdapter();// ????????? banner ?????????
        mBannerImageAdapter.setOnClickListener((data, position) -> navigateToShopDetail());
        mAdBanner.setAdapter(mBannerImageAdapter);

        // ????????????
        mRefreshLayout.setOnRefreshListener(() -> {
            mAdBanner.stopLoop();
            mShopList.clear(); // ???????????????
            new Handler().postDelayed((Runnable) () -> {
                loadData(0); // ??????????????????
                mRefreshLayout.setRefreshing(false);
                mAdBanner.startLoop();
            }, 2000);
        });

        // ????????????
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
                            loadData(mShopList.size()); // ??????????????????
                            Log.d("TAG", "????????????");
                            mIsLoadingMore = false;
                        }, 2000);
                    }
                }
                Log.d("TAG", "onScrolled: dx -> " + dx + " dy -> " + dy + " lastPosition -> " + mRCVLinearLayoutManager.findLastVisibleItemPosition());
            }
        });

        // ?????? RecyclerView ??????????????? ??? LinearLayoutManager(????????????)???GridLayoutManager(????????????)???StaggeredGridLayoutManager(???????????????)
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