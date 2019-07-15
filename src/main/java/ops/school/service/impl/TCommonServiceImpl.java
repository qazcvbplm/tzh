package ops.school.service.impl;

import ops.school.api.entity.*;
import ops.school.api.service.*;
import ops.school.service.TCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TCommonServiceImpl implements TCommonService {

    @Autowired
    private SenderService senderService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private TxLogService txLogService;

    /**
     * @param sourceId 配送员/商家提现来源账户（senderId, shopId）
     * @param userId   提现指定账户
     * @return
     */
    @Override
    public int txApply(String sourceId, String userId) {
        Pattern pt = Pattern.compile("[0-9]*");
        // 提现指定账户
        WxUser wxUser = wxUserService.findById(userId);
        TxLog log = new TxLog();
        // 满足以下提现的是商家提现
        if (!sourceId.isEmpty() && pt.matcher(sourceId).matches()) {
            // 商家信息
            Shop shop = shopService.getById(Integer.valueOf(sourceId));
            log = new TxLog(shop.getId(), "商家提现", null, shop.getTxAmount(), "", shop.getSchoolId(), wxUser.getAppId());
        } else if (!sourceId.isEmpty()) {
            // 配送员提现
            Sender sender = senderService.check(sourceId);
            // 提现来源账户
            WxUser senderUser = wxUserService.findById(sourceId);
            WxUserBell wxUserBell = wxUserBellService
                    .getById(senderUser.getOpenId() + "-" + senderUser.getPhone());
            log = new TxLog(sender.getId(), "配送员提现", null, wxUserBell.getMoney(), "", sender.getSchoolId(),
                    wxUser.getAppId());
        }
        // 申请提现设置isTx为0
        log.setIsTx(0);
        boolean result = txLogService.save(log);
        if (result) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int txAudit() {
        return 0;
    }
}
