package ops.school.api.vo.card;


import com.baomidou.mybatisplus.annotation.TableName;
import ops.school.api.entity.card.CardBuyLog;

import java.math.BigDecimal;
import java.lang.Long;
import java.util.Date;
import java.lang.String;
import java.io.Serializable;


@TableName(value = "card_buy_log")
public class CardBuyLogVO extends CardBuyLog implements Serializable{
      
    private static final long serialVersionUID = 1L;
}



