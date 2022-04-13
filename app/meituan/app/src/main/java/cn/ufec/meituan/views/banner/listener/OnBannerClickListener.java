package cn.ufec.meituan.views.banner.listener;

public interface OnBannerClickListener<T> {
    /**
     * 点击 Banner
     * @param data 点击的数据
     * @param position 点击的位置
     */
    void OnBannerClick(T data, int position);
}
