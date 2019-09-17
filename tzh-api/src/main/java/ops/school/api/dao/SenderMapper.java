package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Sender;

import java.util.List;

public interface SenderMapper extends BaseMapper<Sender> {

    Sender selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sender record);

    int updateByPrimaryKey(Sender record);

    List<Sender> find(Sender sender);

    Sender check(String openId);

    int count(Sender sender);

    int finddsh(int schoolId);

    List<Integer> findSenderIdBySchoolId(Integer schoolId);
}