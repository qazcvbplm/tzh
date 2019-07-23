package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.Transient;
import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
public class Base implements Serializable {

    @TableField(exist = false)
    private Integer page;

    @TableField(exist = false)
    private Integer size;

    @TableField(exist = false)
    private String orderBy;

    @TableField(exist = false)
    private Integer total;

    @TableField(exist = false)
    private String query;

    @TableField(exist = false)
    private String queryType;

    @Transient
    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    @Transient
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Transient
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Transient
    public Integer getPage() {
        if (page == null)
            return null;
        else
            return (page - 1) * this.size;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Transient
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Transient
    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
