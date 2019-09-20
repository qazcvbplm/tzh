package ops.school.api.dto;


import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/01/15
 * 10:29
 * #
 */
public class BaseDTOCompute {

    private Integer page = 0;

    private Integer size = 0;

    private List<Long> selectIds;

    private Long opUserId;

    public Integer getPage() {
        if (this.page == null || this.page < 0){
            page = 1;
        }
        return (page - 1) * this.size;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        if (this.size == 0 || this.size == null){
            return 100;
        }
        return size * this.page;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<Long> getSelectIds() {
        return selectIds;
    }

    public void setSelectIds(List<Long> selectIds) {

        this.selectIds = selectIds;
    }

    public Long getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(Long opUserId) {
        this.opUserId = opUserId;
    }
}
