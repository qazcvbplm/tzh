package ops.school.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.WxUserBell;

import java.math.BigDecimal;
import java.util.Map;

public interface WxUserBellService extends IService<WxUserBell> {

    Integer charge(Map<String, Object> map);

    int pay(Map<String, Object> map);

    Integer addSource(Map<String, Object> map2);

    int paySource(Map<String, Object> map);

    /**
     * 增加粮票
     *
     * @param id     id
     * @param amount 金额
     * @return
     */
    Boolean addFoodCoupon(String id, BigDecimal amount);

    /**
     * 使用粮票
     *
     * @param id
     * @param amount 金额
     * @return
     */
    Boolean useFoodCoupon(String id, BigDecimal amount);
}
