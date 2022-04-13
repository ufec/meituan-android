package cn.ufec.meituan.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 购物车 Item bean
 */
public class ShoppingCartItemBean {
    @JSONField(name = "productCount")
    private int productCount; // 商品数量
    @JSONField(name = "product")
    private ProductItemBean.ProductBean product; // 商品

    public ShoppingCartItemBean(int productCount, ProductItemBean.ProductBean product) {
        this.productCount = productCount;
        this.product = product;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public ProductItemBean.ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductItemBean.ProductBean product) {
        this.product = product;
    }
}
