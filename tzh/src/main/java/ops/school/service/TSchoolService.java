package ops.school.service;

import ops.school.api.util.ResponseObject;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface TSchoolService {
    @Transactional
    String tx(int schoolId, BigDecimal amount, String openId);

    /**
     * @date:   2019/9/22 15:03
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   schoolId
     * @param   amount
     * @param   openId
     * @Desc:   desc
     */
    ResponseObject schoolLeaderDoTX(int schoolId, BigDecimal amount, String openId);
}
