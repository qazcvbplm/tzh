package ops.school.api.dto.project;

import ops.school.api.entity.SecondHand;

import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/23
 * 17:05
 * #
 */
public class SecondHandRedisDTO {

    private SecondHand secondHand;

    private List<SecondHand> secondHandList;

    private Integer total;

    public SecondHand getSecondHand() {
        return secondHand;
    }

    public void setSecondHand(SecondHand secondHand) {
        this.secondHand = secondHand;
    }

    public List<SecondHand> getSecondHandList() {
        return secondHandList;
    }

    public void setSecondHandList(List<SecondHand> secondHandList) {
        this.secondHandList = secondHandList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
