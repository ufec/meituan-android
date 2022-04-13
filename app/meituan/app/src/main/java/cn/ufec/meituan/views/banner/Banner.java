package cn.ufec.meituan.views.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.ufec.meituan.views.banner.adapter.BannerAdapter;
import cn.ufec.meituan.views.banner.config.BannerConfig;
import cn.ufec.meituan.views.banner.listener.OnBannerClickListener;
import cn.ufec.meituan.views.banner.utils.BannerUtils;

/**
 * 自定义 Banner 控件 继承自 RelativeLayout 所以 Banner 控件本质也是一个 RelativeLayout
 */
public class Banner<T, BA extends BannerAdapter<T, ? extends RecyclerView.ViewHolder>> extends RelativeLayout {
    private ViewPager2 mViewPager2;
    private BannerOnPageChangeCallBack mBannerOnPageChangeCallBack;
    private CompositePageTransformer mCompositePageTransformer;
    private BA mAdapter;
    private BannerAutoLoopTask mAutoLoopTask; // 自动循环播放任务
    private int mStartPosition = 1; // 默认起始位置
    private boolean mIsAutoLoop = BannerConfig.IS_AUTO_LOOP;
    private int mAutoScrollTimeInterval = BannerConfig.AUTO_SCROLL_TIME_INTERVAL; // 自动滚动时间间隔

    public Banner(Context context) {
        // 最终会调用 Banner(Context context, AttributeSet attrs, int defStyleAttr)
        this(context, null);
    }
    public Banner(Context context, AttributeSet attrs) {
        // 最终会调用 Banner(Context context, AttributeSet attrs, int defStyleAttr)
        this(context, attrs, 0);
    }
    /**
     * 统一的入口
     * @param context      上下文
     * @param attrs        参数
     * @param defStyleAttr
     */
    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    /**
     * 初始化banner
     * @param context 上下文
     */
    private void initViews(Context context) {
        // 实例化一个ViewPager2 控件
        mViewPager2 = new ViewPager2(context);
        // 实例化一个AutoLoopTask
        mAutoLoopTask = new BannerAutoLoopTask(this);
        // 实例化一个 继承自 ViewPager2.OnPageChangeCallback 的 页面切换回调
        mBannerOnPageChangeCallBack = new BannerOnPageChangeCallBack();
        // 实例化一个 CompositePageTransformer 让自定义控件 Banner 支持同时添加多个PageTransformer
        mCompositePageTransformer = new CompositePageTransformer();
        // 让 ViewPager2 占满父控件 Banner
        mViewPager2.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // 为ViewPager2 注册一个页面切换回调
        mViewPager2.registerOnPageChangeCallback(mBannerOnPageChangeCallBack);
        // 为 ViewPager2 设置 页面切换样式
        mViewPager2.setPageTransformer(mCompositePageTransformer);
        // 为 ViewPager2 设置 预加载的页面个数（如果页面总数是3，在第一个页面的时候，会同时预加载后面剩余两个页面）
        mViewPager2.setOffscreenPageLimit(2);
        // 将 ViewPager2 加入到父控件 Banner 中
        addView(mViewPager2);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 页面切换回调类
     */
    private class BannerOnPageChangeCallBack extends ViewPager2.OnPageChangeCallback {
        // 是否滚动
        private boolean isScrolled;
        // 当前位置
        private int mPosition = -1;

        public BannerOnPageChangeCallBack() {
            super();
        }

        /**
         * 滑动页面回调
         * @param position 滑动时为当前页码，滑动结束为下一页(可左可右)页码
         * @param positionOffset 位置偏移量[0, 1) 手滑一次最多能滑动一页
         * @param positionOffsetPixels 像素偏移 [0, 宽度]
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = BannerUtils.getRealPosition(BannerConfig.INFINITE_LOOP, position, getRealDataCount());
        }

        @Override
        public void onPageSelected(int position) {
            // 滑动后修改当前位置
            if (isScrolled) {
                mPosition = position;
            }
            super.onPageSelected(position);
        }

        /**
         * 监听页面滚动状态改变
         * @param state 页面滚动状态
         * @see ViewPager2#SCROLL_STATE_IDLE  0 表示 ViewPager2 处于空闲、稳定状态。当前页面完全可见，并且没有动画正在进行中。
         * @see ViewPager2#SCROLL_STATE_DRAGGING 1 表示 ViewPager2 当前正在被用户拖动，或者通过虚假拖动功能以编程方式进行拖动。
         * @see ViewPager2#SCROLL_STATE_SETTLING 2 表示 ViewPager2 正在稳定到最终位置。
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
                isScrolled = true;
            }else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                isScrolled = false;
                if (mPosition == 0) {
                    // 滑到0，0放的是最后一张的位置，应该修改当前展示的位置
                    setCurrentItem(getRealDataCount(), false);
                }else if (mPosition == getItemCount() - 1) {
                    // 滑到最后一张，最后一张对应原数据的第一张
                    setCurrentItem(1, false);
                }
            }
        }
    }


    /**
     * Banner 自动循环任务
     */
     private class BannerAutoLoopTask implements Runnable {
        private final WeakReference<Banner<T, BA>> mReference;

        BannerAutoLoopTask(Banner<T, BA> banner) {
            this.mReference = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            Banner<T, BA> banner = this.mReference.get();
            if (banner != null && banner.mIsAutoLoop) {
                int count = banner.getItemCount();
                if (count != 0) {
                    int next = (banner.getCurrentItem() + 1) % count;
                    banner.setCurrentItem(next);
                    banner.postDelayed(banner.mAutoLoopTask, banner.mAutoScrollTimeInterval);
                }
            }
        }
    }

    /**
     * **********************************************************************
     * ------------------------ 对外公开API ---------------------------------*
     * **********************************************************************
     */

    /**
     * 获取当前展示的页面索引
     * @return 当前展示的页面索引
     */
    public int getCurrentItem() {
        return getViewPager2().getCurrentItem();
    }

    /**
     * 设置展示页面
     * @param position 设置展示的位置
     * @return 对应位置的数据
     */
    public Banner<T, BA> setCurrentItem(int position) {
        return setCurrentItem(position, true);
    }

    /**
     * 设置展示页面
     * @param position 设置展示的位置
     * @param smoothScroll 是否平滑过渡 false 为直接跳转
     * @return 对应位置的数据
     */
    @NonNull
    public Banner<T, BA> setCurrentItem(int position, boolean smoothScroll) {
        mViewPager2.setCurrentItem(position, smoothScroll);
        return this;
    }

    /**
     * 获取适配器
     * @return 适配器
     */
    @NonNull
    public BA getAdapter() {
        return mAdapter;
    }

    /**
     * 获取 ViewPager2
     * @return ViewPager2
     */
    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    /**
     * 获取数据真实数量
     * @return 数据数量
     */
    public int getRealDataCount() {
        if (mAdapter != null) {
            return mAdapter.getRealDataCount();
        }else {
            return 0;
        }
    }

    /**
     * 获取banner个数
     * @return banner个数
     */
    public int getItemCount(){
        if (mAdapter != null) {
            return mAdapter.getItemCount();
        }else {
            return 0;
        }
    }

    /**
     * 为 Banner 设置点击事件监听器
     * @param listener 监听器 实现OnBannerClickListener接口
     * @return Banner 链式调用
     */
    public Banner<T, BA> setBannerOnClickListener(OnBannerClickListener<T> listener) {
        if (mAdapter != null) {
            mAdapter.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置 Banner 滚动方向
     * @param orientation 方向
     * @see ViewPager2#ORIENTATION_HORIZONTAL 水平滚动
     * @see ViewPager2#ORIENTATION_VERTICAL 垂直滚动
     * @return Banner
     */
    public Banner<T, BA> setOrientation(@ViewPager2.Orientation int orientation) {
        if (mViewPager2 != null) {
            mViewPager2.setOrientation(orientation);
        }
        return this;
    }

    /**
     * 开始自动循环轮播
     * @return Banner
     */
    public Banner<T, BA> startLoop() {
        mIsAutoLoop = true;
        if (mAutoLoopTask != null) {
            this.postDelayed(this.mAutoLoopTask, this.mAutoScrollTimeInterval);
        }
        return this;
    }

    /**
     * 停止自动循环轮播
     * @return Banner
     */
    public Banner<T, BA> stopLoop() {
        if (mIsAutoLoop && mAutoLoopTask != null){
            mIsAutoLoop = false;
            removeCallbacks(mAutoLoopTask);
        }
        return this;
    }

    /**
     * 设置自动轮播切换时间
     * @param millisecond 毫秒数
     * @return Banner
     */
    public Banner<T, BA> setAutoScrollTimeInterval(int millisecond) {
        this.mAutoScrollTimeInterval = millisecond;
        return this;
    }

    /**
     * 设置其实页面
     * @param position 页面位置
     * @return Banner
     */
    public Banner<T, BA> setStartPosition(int position) {
        if (position > 0 && position < getItemCount()) {
            this.mStartPosition = position;
        }
        return this;
    }

    public Banner<T, BA> setBannerData(List<T> bannerData) {
        if (mAdapter != null) {
            stopLoop();
            mAdapter.setData(bannerData); // 重新设置数据
            setCurrentItem(mStartPosition, false); // 设置起始页
            startLoop();
        }
        return this;
    }

    /**
     * 设置适配器
     * @param adapter 适配器
     * @return Banner
     */
    public Banner<T, BA> setAdapter(@NonNull BA adapter) {
        mAdapter = adapter;
        mViewPager2.setAdapter(adapter);
        // 设置默认起始位置
        mViewPager2.setCurrentItem(mStartPosition, false);
        startLoop();
        // 链式调用
        return this;
    }
}
