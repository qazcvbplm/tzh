package ops.school.api.serviceimple;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.SchoolMapper;
import ops.school.api.entity.School;
import ops.school.api.service.SchoolService;
import ops.school.api.util.SpringUtil;
import ops.school.api.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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
            case "ops":
                school.setQuery("*");
                break;
            default:
                return null;
        }
        return schoolMapper.find(school);
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


}
