package ops.school.api.dto.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.entity.Product;
import ops.school.api.entity.ProductAttribute;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/7/20
 * 16:12
 * #
 */
public class ProductAndAttributeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank
    private String productName;
    @NotBlank
    private String productImage;
    @NotNull
    private BigDecimal discount;
    @NotNull
    private Integer boxPriceFlag;
    @NotNull
    private Integer productCategoryId;
    @NotNull
    private Integer shopId;
    @NotNull
    private Integer schoolId;
    @NotNull
    private Integer sale;
    @NotNull
    private Integer isShow;

    private Integer isDelete;


    private Integer stock;

    private Integer stockFlag;

    /**
     * 规格变量
     */
    private String attributeName;

    private Integer attributeId;

    private BigDecimal attributePrice;

    private Integer attributeIsDelete;

    public Product getProduct(){
        Product product = new Product();
        product.setId(this.id);
        product.setProductName(this.productName);
        product.setProductImage(this.productImage);
        product.setDiscount(this.discount);
        product.setBoxPriceFlag(this.boxPriceFlag);
        product.setProductCategoryId(this.productCategoryId);
        product.setShopId(this.shopId);
        product.setShopId(this.schoolId);
        product.setSale(this.sale);
        product.setIsShow(this.isShow);
        product.setIsDelete(this.isDelete);
        product.setStock(this.stock);
        product.setStockFlag(this.stockFlag);
        return product;
    }

    public ProductAttribute getProductAttribute(){
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setId(this.attributeId);
        productAttribute.setProductId(this.id);
        productAttribute.setName(this.productName);
        productAttribute.setPrice(this.attributePrice);
        productAttribute.setIsDelete(this.attributeIsDelete);
        return  productAttribute;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getBoxPriceFlag() {
        return boxPriceFlag;
    }

    public void setBoxPriceFlag(Integer boxPriceFlag) {
        this.boxPriceFlag = boxPriceFlag;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockFlag() {
        return stockFlag;
    }

    public void setStockFlag(Integer stockFlag) {
        this.stockFlag = stockFlag;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public BigDecimal getAttributePrice() {
        return attributePrice;
    }

    public void setAttributePrice(BigDecimal attributePrice) {
        this.attributePrice = attributePrice;
    }

    public Integer getAttributeIsDelete() {
        return attributeIsDelete;
    }

    public void setAttributeIsDelete(Integer attributeIsDelete) {
        this.attributeIsDelete = attributeIsDelete;
    }
}
