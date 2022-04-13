package cn.ufec.meituan.views.banner.utils;

/**
 * Banner 控件工具类
 */
public class BannerUtils {

    /**
     * 获取真实位置
     * @param isLoop 是否开启了无限循环
     * @param position 当前位置
     * @param realCount 真实的数据数量
     * @return 真实的位置
     */
    public static int getRealPosition(boolean isLoop, int position, int realCount) {
        // 没有开启无限循环 直接返回当前位置即可
        if (!isLoop) {
            return position;
        }
        // 开启了无限循环(伪无限循环)
        // 第一个数据前增加一个数据 (复制最后一个数据)，最后一个数据后增加一个数据 (复制第一个数据)
        // 初始设置为从下标为1的数据开始轮播，这样就能往左滑动
        // 如果有3个数据，realCount就是5 position 取值范围就是 [0, 1, 2, 3, 4]，其中0对应的是原数据中最后一个数据，4对应的是原数据中第一个数据
        if (position == 0) {
            // 从原数据第一个往左滑(处理左边界) 我们应该返回原数据中的最后一个数据下标 例子中的4
            return realCount - 1;
        }else if (position == realCount + 1) {
            // 从原数据最后一个往右滑(处理右越界) 我们应该返回原数据中的第一个数据下标 这里直接
            return 0;
        }else{
            return position - 1;
        }
    }
}
