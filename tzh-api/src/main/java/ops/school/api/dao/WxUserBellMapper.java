package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.BackBellDTO;
import ops.school.api.entity.WxUserBell;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
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

    int updatePhone(Map<String,Object> map);

    int txUpdate(Map<String,Object> map);

    /**
     * @date:   2019/8/4 21:38
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   senderGet
     * @param   wxUserId
     * @Desc:   desc
     */
    Integer addSenderMoneyByWXId(@Param("senderGet") BigDecimal senderGet, @Param("wxUserId") Long wxUserId);

    /**
     * @date:   2019/8/5 17:46
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   source
     * @param   wxUserId
     * @Desc:   desc
     */
    Integer addSourceByWxId(@Param("source") Integer source, @Param("wxUserId") Long wxUserId);

    int updatePhoneById(@Param("phone") String updatePhone, @Param("id") Long id);

    Integer countOldData();

    List<BackBellDTO> findOldAllList();

    int updateOldMoneyTo0(String phone);

    BigDecimal beforeUpdateMoney();

    Integer beforeUpdateSource();

    List<BackBellDTO> findOldAllListSource();

    int updateOldSourceTo0(String phone);
}