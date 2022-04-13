package cn.ufec.meituan.views.banner.holder;

import android.view.ViewGroup;

/**
 * 此接口用于规范适配器
 * @param <T> 适配器支持的数据类型
 * @param <VH> ViewHolder
 */
public interface IViewHolder<T, VH> {

    /**
     * 创建ViewHolder
     * @return XViewHolder
     */
    VH onCreateHolder(ViewGroup parent, int viewType);

    /**
     * 绑定布局数据
     *
     * @param holder   XViewHolder
     * @param data     数据实体
     * @param position 当前位置
     * @param size     总数
     */
    void onBindView(VH holder, T data, int position, int size);
}

