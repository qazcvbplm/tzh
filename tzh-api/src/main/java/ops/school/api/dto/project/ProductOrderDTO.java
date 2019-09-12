package ops.school.api.dto.project;

public class ProductOrderDTO {

    private Integer productId;

    private Integer attributeId;

    private Integer count;


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProductOrderDTO{");
        sb.append("productId=").append(productId);
        sb.append(", attributeId=").append(attributeId);
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
