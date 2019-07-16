package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.WxUserBell;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Map;

public interface WxUserBellMapper extends BaseMapper<WxUserBell> {


    WxUserBell selectByPrimaryKey(String phone);


    int pay(Map<String, Object> map);


    int charge(Map<String, Object> map);


    int paySource(Map<String, Object> map);


    int addSource(Map<String, Object> map);


    int findByPhone(String string);

    @Update("update wx_user_bell set food_coupon = food_coupon+#{amount} where phone=#{id}")
    Integer addFoodCoupon(@Param("id") String id, @Param("amount") BigDecimal amount);

    @Update("update wx_user_bell set food_coupon = food_coupon-#{amount} where phone=#{id} and food_coupon>=#{amount}")
    Integer useFoodCoupon(@Param("id") String id, @Param("amount") BigDecimal amount);

}