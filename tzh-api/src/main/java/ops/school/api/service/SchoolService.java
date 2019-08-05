package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.School;

import java.util.List;
import java.util.Map;

public interface SchoolService extends IService<School> {

    void add(School school) throws Exception;

    List<School> find(School school);

    int update(School school);

    School findById(Integer schoolId);

    School login(String loginName, String enCode);

    void chargeUse(Map<String, Object> map);

    int sendertx(Map<String, Object> map);

    void charge(Map<String, Object> map2);

    int tx(Map<String, Object> map);

    Integer endOrder(Map<String, Object> map);

    /**
     * @date:   2019/8/5 11:15
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   rechargeChargeSendBell
     * @Desc:   desc 增加user_charge，user_bell_all，user_charge_send，by id
     */
    Integer rechargeScChargeSendBellByModel(School rechargeChargeSendBell);
}
