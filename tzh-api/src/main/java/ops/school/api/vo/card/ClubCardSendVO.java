package ops.school.api.vo.card;


import com.baomidou.mybatisplus.annotation.TableName;
import ops.school.api.entity.card.ClubCardSend;
import ops.school.api.enums.IEnumS;

import java.math.BigDecimal;
import java.lang.Long;
import java.util.Date;
import java.lang.String;
import java.lang.Byte;
import java.lang.Integer;
import java.io.Serializable;


@TableName(value = "club_card_send")
public class ClubCardSendVO extends ClubCardSend implements Serializable{
      
    private static final long serialVersionUID = 1L;
    /**卡类型，1-配送费卡*/
    public enum Type implements IEnumS<Byte> {

        ;
        private Byte value;
        private String name;

        Type(Integer value,String name) {
            this.value = value.byteValue();
            this.name = name;
        }

        @Override
        public Byte getValue() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value.intValue();
        }
    }
    /**卡的状态，0-冻结不可用，1-可用*/
    public enum Status implements IEnumS<Byte> {

        YES(1,"启用"),
        NO(0,"禁用"),
        ;
        private Byte value;
        private String name;

        Status(Integer value,String name) {
            this.value = value.byteValue();
            this.name = name;
        }

        @Override
        public Byte getValue() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value.intValue();
        }
    }
    /**是否删除 0：未删除 1：已删除*/
    public enum IsDelete implements IEnumS<Byte> {
        HAS_DELETED(1,"已删除"),
        NO_DELETED(0,"未删除"),

        ;
        private Byte value;
        private String name;

        IsDelete(Integer value,String name) {
            this.value = value.byteValue();
            this.name = name;
        }

        @Override
        public Byte getValue() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value.intValue();
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ClubCardSendVO{");
        String father = super.toString();
        sb.append(father);
        sb.append('}');
        return sb.toString();
    }
}



