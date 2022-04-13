package cn.ufec.meituan.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 菜品列表数据 bean
 */
public class ProductItemBean {
    @JSONField(name = "code")
    private int code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "data")
    public List<ProductBean> data;

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

    public List<ProductBean> getData() {
        return data;
    }

    public void setData(List<ProductBean> data) {
        this.data = data;
    }

    public static final class ProductBean{
        @JSONField(name = "name")
        private String name;

        @JSONField(name = "month_sales")
        private String monthSales;

        @JSONField(name = "price")
        private float price;

        @JSONField(name = "picture")
        private String picture;

        @JSONField(name = "recommend_reason")
        private String recommendReason;

        @JSONField(name = "like_ratio_desc")
        private String likeRatioDesc;

        @JSONField(name = "unit")
        private String unit;

        @JSONField(name = "spu_dna_value")
        private String spuDnaValue;

        @JSONField(name = "product_label")
        private String productLabel;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMonthSales() {
            return monthSales;
        }

        public void setMonthSales(String monthSales) {
            this.monthSales = monthSales;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getRecommendReason() {
            return recommendReason;
        }

        public void setRecommendReason(String recommendReason) {
            this.recommendReason = recommendReason;
        }

        public String getLikeRatioDesc() {
            return likeRatioDesc;
        }

        public void setLikeRatioDesc(String likeRatioDesc) {
            this.likeRatioDesc = likeRatioDesc;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getSpuDnaValue() {
            return spuDnaValue;
        }

        public void setSpuDnaValue(String spuDnaValue) {
            this.spuDnaValue = spuDnaValue;
        }

        public String getProductLabel() {
            return productLabel;
        }

        public void setProductLabel(String productLabel) {
            this.productLabel = productLabel;
        }
    }
}
