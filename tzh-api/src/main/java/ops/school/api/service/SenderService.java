package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Sender;
import ops.school.api.util.ResponseObject;

import javax.validation.Valid;
import java.util.List;

public interface SenderService extends IService<Sender> {

    Sender findById(Integer id);

    void add(@Valid Sender sender);

    List<Sender> find(Sender sender);

    int update(Sender sender);

    Sender findByPhone(String phone);

    int count(Sender sender);


    int finddsh(int schoolId);

    Sender check(String senderId);

    List<Integer> findSenderIdBySchoolId(Integer schoolId);

    /**
     * @date:   2019/8/28 23:12
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   sender
     * @Desc:   desc
     */
    ResponseObject findSenderByFree(Sender sender);
}
