package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;


/**
 * @author: QinDaoFang
 * @date:   2019/7/27 16:09
 * @desc:   自主分页，不能给mybatis用，会报错
 */
public class PageQueryDTO implements Serializable {


    private static final long serialVersionUID = 1L;

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


    public Integer getPage() {
        if (this.page == null || this.page < 0 ){
            return 0;
        }
        return (this.page - 1) * this.getSize();
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        if (this.size == null || this.size  < 0) {
            return 10;
        }
        return this.size ;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
