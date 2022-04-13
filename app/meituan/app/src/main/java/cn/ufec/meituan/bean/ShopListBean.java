package cn.ufec.meituan.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class ShopListBean {
    @JSONField(name = "code")
    private int code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "data")
    private List<ShopItemBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ShopItemBean> getData() {
        return data;
    }

    public void setData(List<ShopItemBean> data) {
        this.data = data;
    }

    public static final class ShopItemBean {
        @JSONField(name = "shop_id")
        private String  shopId; // 商店ID

        @JSONField(name = "shop_name")
        private String shopName; // 店铺名称

        @JSONField(name = "shop_thumbnail")
        private String shopThumbnail; // 缩略图Url

        @JSONField(name = "shop_month_sales")
        private String shopMonthSales; // 月销量

        @JSONField(name = "shipping_fee")
        private String shippingFee; // 配送费

        @JSONField(name = "min_price")
        private String minPrice; // 最低多少起送

        @JSONField(name = "recommend_reason")
        private String recommendReason; // 推荐理由

        @JSONField(name = "delivery_time")
        private String deliveryTime; // 配送时间

        @JSONField(name = "is_brand")
        private String isBrand; // 品牌商图标（有图标链接就是品牌商，没有就不是）

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopThumbnail() {
            return shopThumbnail;
        }

        public void setShopThumbnail(String shopThumbnail) {
            this.shopThumbnail = shopThumbnail;
        }

        public String getShippingFee() {
            return shippingFee;
        }

        public void setShippingFee(String shippingFee) {
            this.shippingFee = shippingFee;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(String minPrice) {
            this.minPrice = minPrice;
        }

        public String getRecommendReason() {
            return recommendReason;
        }

        public void setRecommendReason(String recommendReason) {
            this.recommendReason = recommendReason;
        }

        public String getShopMonthSales() {
            return shopMonthSales;
        }

        public void setShopMonthSales(String shopMonthSales) {
            this.shopMonthSales = shopMonthSales;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getIsBrand() {
            return isBrand;
        }

        public void setIsBrand(String isBrand) {
            this.isBrand = isBrand;
        }
    }
}
