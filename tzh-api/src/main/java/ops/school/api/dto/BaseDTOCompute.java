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
        if (startNum == null){
            startNum = 0;
        }
        if (this.sizeNum == null){
            this.sizeNum = 0;
        }

        this.startNum = (startNum - 1)*this.sizeNum;
    }

    public Integer getSizeNum() {
        if (sizeNum == null){
            return null;
        }
        return sizeNum * this.startNum;
    }

    public void setSizeNum(Integer sizeNum) {
        if (sizeNum == null){
            sizeNum = 0;
        }
        if (this.startNum == null){
            this.startNum = 0;
        }
        this.sizeNum = sizeNum;
    }

    public List<Long> getSelectIds() {
        return selectIds;
    }

    public void setSelectIds(List<Long> selectIds) {
        this.selectIds = selectIds;
    }
}
