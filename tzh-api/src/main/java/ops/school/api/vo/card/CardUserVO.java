package ops.school.api.vo.card;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import ops.school.api.entity.card.CardUser;
import ops.school.api.enums.IEnumS;

import java.math.BigDecimal;
import java.lang.Long;
import java.util.Date;
import java.lang.Byte;
import java.lang.Integer;
import java.io.Serializable;


@TableName(value = "card_user")
public class CardUserVO extends CardUser implements Serializable{

    @TableField(exist = false)
    private ClubCardSendVO clubCardSendVO;
      
    private static final long serialVersionUID = 1L;

    public ClubCardSendVO getClubCardSendVO() {
        return clubCardSendVO;
    }

    public void setClubCardSendVO(ClubCardSendVO clubCardSendVO) {
        this.clubCardSendVO = clubCardSendVO;
    }

    /**卡类型，1-配送费卡*/
    public enum CardType implements IEnumS<Byte> {

        ;
        private Byte value;
        private String name;

        CardType(Integer value,String name) {
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
}



