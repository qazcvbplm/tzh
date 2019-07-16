package ops.school.service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface TCommonService {

    // 商家和配送员申请提现审核
    @Transactional
    int txApply(BigDecimal amount,String senderId, String dzOpenid,Integer shopId);

    // 审核商家和配送员提现
    @Transactional
    int txAudit(Integer txId, Integer status);
}
