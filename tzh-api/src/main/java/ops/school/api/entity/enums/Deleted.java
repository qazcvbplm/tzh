package ops.school.api.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum Deleted implements IEnum<Integer> {

    DELETED(1), NO_DELETED(0);

    public void setValue(Integer value) {
        this.value = value;
    }

    private Integer value;

    Deleted(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
