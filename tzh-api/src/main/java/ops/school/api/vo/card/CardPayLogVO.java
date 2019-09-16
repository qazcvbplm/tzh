package ops.school.api.vo.card;


import com.baomidou.mybatisplus.annotation.TableName;
import ops.school.api.entity.card.CardPayLog;
import ops.school.api.enums.IEnumS;

import java.math.BigDecimal;
import java.lang.Long;
import java.util.Date;
import java.lang.String;
import java.lang.Byte;
import java.io.Serializable;


@TableName(value = "card_pay_log")
public class CardPayLogVO extends CardPayLog implements Serializable{
      
    private static final long serialVersionUID = 1L;
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
}



