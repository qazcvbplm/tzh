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
}
