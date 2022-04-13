package cn.ufec.meituan.views.banner.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.ufec.meituan.views.banner.config.BannerConfig;
import cn.ufec.meituan.views.banner.holder.IViewHolder;
import cn.ufec.meituan.views.banner.listener.OnBannerClickListener;
import cn.ufec.meituan.views.banner.utils.BannerUtils;

/**
 * Adapter是一个控制器对象，从模型层获取数据然后提供给RecyclerView显示。负责：
 * 1.创建必要的ViewHolder
 * 2.绑定ViewHolder至模型层数据
 *  首先，RecyclerView调用Adapter的getItemCount方法，询问有多少个对象。
 *  接着，RecyclerView调用Adapter的onCreateViewHolder方法创建ViewHolder。 IViewHolder 接口规范
 *  最后，RecyclerView会传入ViewHolder的位置，调用Adapter的onBindViewHolder方法绑定到 IViewHolder 接口规范
 *
 * 所有 BannerAdapter 的基类
 * 泛型 T 代表 适配器支持的数据类型 资源图片 则是资源ID，网络图片/视频 则是 url
 * 泛型 VH 代表 ViewHolder 它必须是继承自 RecyclerView.ViewHolder
 * 该泛型基类继承自 RecyclerView.Adapter
 */
public abstract class BannerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IViewHolder<T, VH> {
    private List<T> mAdapterData = new CopyOnWriteArrayList<>();
    private VH mViewHolder;
    private OnBannerClickListener<T> mBannerClickListener;

    public BannerAdapter(List<T> adapterData) {
        setData(adapterData);
    }

    public BannerAdapter() {

    }

    /**
     * 创建ViewHolder (所有的资源只会创建一次，所以本函数只会执行 getItemCount() 次)
     * @param parent 父容器
     * @param viewType 试图类型
     * @return ViewHolder
     */
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateHolder(parent, viewType);
    }

    /**
     * RecyclerView会传入ViewHolder及其对应的位置，调用Adapter的onBindViewHolder方法绑定到RecyclerView
     * 由于 RecyclerView 的特性, 消失在页面的数据会自动 "取消绑定"，等到再次预加载时会重新绑定
     * @param holder ViewHolder
     * @param position ViewHolder 位置
     */
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        mViewHolder = holder;
        int realPosition = getRealPosition(position);
        T data = mAdapterData.get(realPosition);
        onBindView(holder, data, realPosition, getRealDataCount());
        if (mBannerClickListener != null){
            holder.itemView.setOnClickListener(view -> mBannerClickListener.OnBannerClick(data, realPosition));
        }
    }

    /**
     * 获取数据个数 (无限循环)
     * @return 数据总数 (该数量决定了ViewPager2的页数)
     */
    @Override
    public int getItemCount() {
        int realDataCount = getRealDataCount();
        if (BannerConfig.INFINITE_LOOP) {
            // 开启了无限循环则在数据两头插入数据
            return realDataCount > 1 ? realDataCount + 2 : realDataCount;
        }else{
            // 没开启自动循环就直接返回
            return realDataCount;
        }
    }

    /**
     * 设置数据实体集合
     * @param adapterData 适配器数据
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<T> adapterData) {
        if (adapterData == null) {
            mAdapterData = new ArrayList<>();
        }else{
            mAdapterData = adapterData;
        }
        // 通知数据集合发生改变
        notifyDataSetChanged();
    }

    /**
     * 获取对应位置的数据
     * @param position 当前 ViewPager2 滑动到第几页
     * @return 对应位置数据 List<T>
     */
    public T getData(int position) {
        return mAdapterData.get(position);
    }

    /**
     * 获取真实位置对应的数据 (开启了无限循环, 位置就不再是原来的位置了)
     * @param position 真实的位置
     * @return 对应位置数据 List<T>
     */
    public T getRealData(int position) {
        return mAdapterData.get(getRealPosition(position));
    }

    /**
     * 获取传入数据的数据量
     * @return 数据量
     */
    public int getRealDataCount() {
        return mAdapterData == null ? 0 : mAdapterData.size();
    }

    /**
     * 获取真实位置(开启了无限循环)
     * @param position 当前位置
     * @return 真实位置
     */
    private int getRealPosition(int position) {
        return BannerUtils.getRealPosition(BannerConfig.INFINITE_LOOP, position, getRealDataCount());
    }

    /**
     * 设置点击事件监听器
     * @param listener 监听器 必须实现 OnBannerClickListener 接口
     */
    public void setOnClickListener(OnBannerClickListener<T> listener) {
        mBannerClickListener = listener;
    }

    /**
     * 获取 ViewHolder
     * @return ViewHolder
     */
    public VH getViewHolder() {
        return mViewHolder;
    }
}
