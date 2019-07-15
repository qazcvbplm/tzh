package ops.school.service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface TCommonService {

    // 商家和配送员申请提现审核
    @Transactional
    int txApply(BigDecimal amount,String sourceId, String userId);

    // 审核商家和配送员提现
    @Transactional
    int txAudit(BigDecimal amount,Integer txId, Integer status,String sourceId, String userId);
}
