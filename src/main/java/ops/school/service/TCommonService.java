package ops.school.service;

public interface TCommonService {

    // 商家和配送员申请提现审核
    public int txApply(String sourceId, String userId);

    // 审核商家和配送员提现
    public int txAudit();
}
