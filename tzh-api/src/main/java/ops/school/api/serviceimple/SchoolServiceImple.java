package ops.school.api.serviceimple;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.SchoolMapper;
import ops.school.api.entity.School;
import ops.school.api.exception.Assertions;
import ops.school.api.service.SchoolService;
import ops.school.api.util.SpringUtil;
import ops.school.api.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SchoolServiceImple extends ServiceImpl<SchoolMapper, School> implements SchoolService {

    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void add(@Valid School school) throws Exception {
        if (schoolMapper.findByLoginName(school.getLoginName()) == null) {
            school.setLoginPassWord(Util.EnCode(school.getLoginPassWord()));
            school.setSort(System.currentTimeMillis());
            schoolMapper.insert(school);
        } else {
            throw new Exception("账号已存在请重新输入");
        }
    }

    @Override
    public List<School> find(School school) {
        school.setQuery("");
        switch (school.getQueryType()) {
            case "wxuser":
                school.setQuery("id,name");
                break;
            case "school":
                school.setQuery("*");
                break;
            default:
                return null;
        }
//        Assertions.notNull(school,school.getQueryType());
//        if ("one".equals(school.getQueryType())){
//            return schoolMapper.findOneBySCId(school);
//        }else if ("wxuser".equals(school.getQueryType())){
//            return schoolMapper.find(school);
//        }
        if (school.getPage() != null){
            school.setPage(school.getPage()-1);
        }
        List<School> schoolList = schoolMapper.find(school);
        return schoolList;
    }


    @Override
    public int update(School school) {
        int i = schoolMapper.updateByPrimaryKeySelective(school);
        if (i > 0 && SpringUtil.redisCache()) {
            stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
        }
        return i;
    }

    @Override
    public School findById(Integer schoolId) {
        if (SpringUtil.redisCache()) {
            String school = stringRedisTemplate.opsForValue().get("SCHOOL_ID_" + schoolId);
            if (school != null) {
                return JSON.parseObject(school, School.class);
            } else {
                School rs = schoolMapper.selectByPrimaryKey(schoolId);
                stringRedisTemplate.opsForValue().set("SCHOOL_ID_" + schoolId, JSON.toJSONString(rs));
                return rs;
            }
        }
        return schoolMapper.selectByPrimaryKey(schoolId);
    }

    @Override
    public School login(String loginName, String enCode) {
        School school = new School();
        school.setLoginName(loginName);
        school.setLoginPassWord(enCode);
        school = schoolMapper.login(school);
        return school;
    }



    @Override
    public void chargeUse(Map<String, Object> map) {
        schoolMapper.chargeUse(map);
    }

    @Override
    public int sendertx(Map<String, Object> map) {
        return schoolMapper.sendertx(map);
    }

    @Override
    public void charge(Map<String, Object> map2) {
        schoolMapper.charge(map2);
    }

    @Override
    public int tx(Map<String, Object> map) {
        return schoolMapper.tx(map);
    }

    @Override
    public Integer endOrder(Map<String, Object> map) {
        return schoolMapper.endOrder(map);
    }

    /**
     * @date:   2019/8/5 11:16
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   school
     * @Desc:   desc 增加user_charge，user_bell_all，user_charge_send，by id
     */
    @Override
    public Integer rechargeScChargeSendBellByModel(School school) {
        Assertions.notNull(school.getId(),school.getUserCharge(),school.getUserBellAll(),school.getUserChargeSend());
        Integer updateNum = schoolMapper.rechargeScChargeSendBellByModel(school);
        stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
        return updateNum;
    }

    /**
     * @date:   2019/8/5 18:40
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   payPrice
     * @param   payFoodCoupon
     * @param   schoolId
     * @Desc:   desc 扣除学校余额数据和粮票余额
     */
    @Override
    public Integer disScUserBellAllAndUserSBellByScId(BigDecimal payPrice, BigDecimal payFoodCoupon, Integer schoolId) {
        Assertions.notNull(payPrice,payFoodCoupon,schoolId);
        Integer disSCNum = schoolMapper.disScUserBellAllAndUserSBellByScId(payPrice,payFoodCoupon,schoolId);
        stringRedisTemplate.delete("SCHOOL_ID_" + schoolId);
        return disSCNum;
    }

    /**
     * @date:   2019/8/19 14:54
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   payPrice
     * @param   payFoodCoupon
     * @param   id
     * @Desc:   desc
     */
    @Override
    public Integer disScUserBellAllAndUserSBellByScIdCan0(BigDecimal payPrice, BigDecimal payFoodCoupon, Integer schoolId) {
        Assertions.notNull(payPrice,payFoodCoupon,schoolId);
        Integer disSCNum = schoolMapper.disScUserBellAllAndUserSBellByScIdCan0(payPrice,payFoodCoupon,schoolId);
        stringRedisTemplate.delete("SCHOOL_ID_" + schoolId);
        return disSCNum;
    }
}
