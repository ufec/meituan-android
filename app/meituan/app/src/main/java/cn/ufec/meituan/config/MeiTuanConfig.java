package cn.ufec.meituan.config;

public class MeiTuanConfig {
    public static final String BASE_URL = "";

    public static class Tags {
        /**
         * 用于标记某接口需要 Token 头信息
         * 如果没有办法得到合法的 Token，则跳转登录
         */
        public static final String TOKEN = "TOKEN";

        /**
         * 用于标记某接口是否开启自动加载框功能
         */
        public static final String LOADING = "LOADING";
    }
}
