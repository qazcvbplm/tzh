package ops.school.api.dto.project;


import ops.school.api.entity.IndexShopProduct;
import ops.school.api.entity.Product;
import ops.school.api.entity.Shop;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class IndexShopProductRedisDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 学校id
     */
    private Long schoolId;
    /**
     * 创建人id
     */
    private Long createId;

    private List<IndexShopProduct> indexShopProductList;

    private List<Shop> shopList;

    private List<Product> productList;

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public List<IndexShopProduct> getIndexShopProductList() {
        return indexShopProductList;
    }

    public void setIndexShopProductList(List<IndexShopProduct> indexShopProductList) {
        this.indexShopProductList = indexShopProductList;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}