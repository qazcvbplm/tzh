package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.School;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SchoolMapper extends BaseMapper<School> {

    School selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(School record);

    School findByLoginName(String loginName);

    List<School> find(School school);

    School login(School school);

    int endOrder(Map<String, Object> map);

    int sendertx(Map<String, Object> map);

    int tx(Map<String, Object> map);

    int charge(Map<String, Object> map);

    int chargeUse(Map<String, Object> map);

    /**
     * @date:   2019/7/22 17:19
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.School
     * @param   school
     * @Desc:   desc 根据学校id查询单个
     */
    School findOneBySCId(School school);

    /**
     * @date:   2019/8/5 11:18
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   school
     * @Desc:   desc 增加user_charge，user_bell_all，user_charge_send，by id
     */
    Integer rechargeScChargeSendBellByModel(School school);

    /**
     * @date:   2019/8/5 18:41
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   payPrice
     * @param   payFoodCoupon
     * @param   schoolId
     * @Desc:   desc 扣除学校余额数据和粮票余额
     */
    Integer disScUserBellAllAndUserSBellByScId(@Param("payPrice") BigDecimal payPrice, @Param("payFoodCoupon") BigDecimal payFoodCoupon, @Param("schoolId") Integer schoolId);

    /**
     * @date:   2019/8/19 14:55
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   payPrice
     * @param   payFoodCoupon
     * @param   schoolId
     * @Desc:   desc
     */
    Integer disScUserBellAllAndUserSBellByScIdCan0(@Param("payPrice") BigDecimal payPrice, @Param("payFoodCoupon") BigDecimal payFoodCoupon, @Param("schoolId") Integer schoolId);
}