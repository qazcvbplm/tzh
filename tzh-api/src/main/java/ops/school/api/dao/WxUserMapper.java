package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.Test;
import ops.school.api.entity.WxUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WxUserMapper extends BaseMapper<WxUser> {

    WxUser selectByPrimaryKey(String openId);

    int updateByPrimaryKeySelective(WxUser record);

    int updateByPrimaryKey(WxUser record);

    List<WxUser> find(WxUser wxUser);

    int findByPhone(String phone);

    WxUser findByschoolAndPhone(WxUser query);

    int countBySchoolId(int schoolId);

    List<WxUser> findByPhoneGZH(String query);

    List<WxUser> findGzh(String phone);

    @Select("select wx_user.*,wx_user_bell.* from wx_user left join wx_user_bell on wx_user_bell.phone=concat(wx_user.open_id,'-',wx_user.phone) limit 10")
    List<Test> test();

    /**
     * @date:   2019/7/19 10:40
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.WxUser
     * @param   userId
     * @Desc:   desc 根据用户id查询用户
     */
    WxUser selectOneByUserId(Long userId);
}