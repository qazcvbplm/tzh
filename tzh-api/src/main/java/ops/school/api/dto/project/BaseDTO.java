package ops.school.api.dto.project;


import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/01/15
 * 10:29
 * #
 */
public class BaseDTO {

    /**
     * limit 开始的位置
     */
    private Integer startNum;

    /**
     * limit 条数
     */
    private Integer sizeNum;

    private List<Long> selectIds;

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getSizeNum() {
        return sizeNum;
    }

    public void setSizeNum(Integer sizeNum) {
        this.sizeNum = sizeNum;
    }

    public List<Long> getSelectIds() {
        return selectIds;
    }

    public void setSelectIds(List<Long> selectIds) {
        this.selectIds = selectIds;
    }
}
