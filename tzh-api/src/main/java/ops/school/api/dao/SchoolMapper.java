package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.School;

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
}