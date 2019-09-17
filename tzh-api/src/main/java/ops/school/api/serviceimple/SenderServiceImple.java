package ops.school.api.serviceimple;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.SenderMapper;
import ops.school.api.dao.WxUserMapper;
import ops.school.api.entity.Sender;
import ops.school.api.entity.WxUser;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.exception.YWException;
import ops.school.api.service.SenderService;
import ops.school.api.util.RedisUtil;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SenderServiceImple extends ServiceImpl<SenderMapper, Sender> implements SenderService {

    @Autowired
    private SenderMapper senderMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil cache;

    @Autowired
    private WxUserMapper wxUserMapper;

    @Override
    public Sender findById(Integer id) {
        if (SpringUtil.redisCache()) {
            Object rs = stringRedisTemplate.boundHashOps("SENDER_LIST").get(id.toString());
            if (rs != null) {
                return JSON.parseObject(rs.toString(), Sender.class);
            } else {
                Sender sender = senderMapper.selectByPrimaryKey(id);
                QueryWrapper<WxUser> wrapper = new QueryWrapper<>();
                if (sender != null && sender.getOpenId() != null){
                    wrapper.eq("open_id",sender.getOpenId());
                    WxUser wxUser = wxUserMapper.selectOne(wrapper);
                    sender.setWxUser(wxUser);
                }
                stringRedisTemplate.boundHashOps("SENDER_LIST").put(id.toString(), JSON.toJSONString(sender));
                return sender;
            }
        }
        Sender sender = senderMapper.selectByPrimaryKey(id);
        QueryWrapper<WxUser> wrapper = new QueryWrapper<>();
        if (sender != null && sender.getOpenId() != null){
            wrapper.eq("open_id",sender.getOpenId());
            WxUser wxUser = wxUserMapper.selectOne(wrapper);
            sender.setWxUser(wxUser);
        }
        return sender;
    }

    @Override
    public void add(Sender sender) {
        if (senderMapper.check(sender.getOpenId()) == null) {
            senderMapper.insert(sender);
        } else {
            throw new YWException("请勿重复申请");
        }
    }

    @Override
    public List<Sender> find(Sender sender) {

        return senderMapper.find(sender);
    }

    @Override
    public int update(Sender sender) {
        if (SpringUtil.redisCache()) {
            stringRedisTemplate.boundHashOps("SENDER_LIST").delete(sender.getId().toString());
        }
        return senderMapper.updateByPrimaryKeySelective(sender);
    }

    @Override
    public Sender findByPhone(String phone) {
        Sender query = new Sender();
        query.setPhone(phone);
        List<Sender> list = senderMapper.find(query);
        if (list.size() > 0) {
            /* if(list.get(0).getExam().equals("审核通过")) */
            return list.get(0);
        }
        return null;
    }











    /*
     * public void addsource(String openid,int source){ //增加积分 WxUser
     * wxUser=wxUserMapper.selectByPrimaryKey(openid); Map<String,Object>
     * map2=new HashMap<>(); map2.put("phone",
     * wxUser.getOpenId()+"-"+wxUser.getPhone()); map2.put("source", source);
     * wxUserBellMapper.addSource(map2); }
     */

   /* public void senderAddMoney(BigDecimal amount, Integer senderId) {
        if (senderId == 0) {
            return;
        }
        Sender sender = findById(senderId);
        amount = amount.multiply(new BigDecimal(1).subtract(sender.getRate())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        WxUser wxUser = wxUserService.findById(sender.getOpenId());
        Map<String, Object> map = new HashMap<>();
        map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map.put("amount", amount);
        if (wxUserBellMapper.charge(map) == 0) {
            LoggerUtil.log("配送员送达订单增加余额失败：" + amount.toString());
        }
    }*/


    @Override
    public int count(Sender sender) {
        return senderMapper.count(sender);
    }

   /* @Transactional
    public int tx(String senderId, BigDecimal amount) {
        Sender sender = senderMapper.check(senderId);
        WxUser query = new WxUser();
        query.setSchoolId(sender.getSchoolId());
        query.setPhone(sender.getPhone());
        WxUser user = wxUserService.findByschoolAndPhone(query);
        School school = schoolService.findById(sender.getSchoolId());
        if (user == null) {
            throw new YWException("提现失败，请返回" + school.getName() + "绑定该手机号码");
        }
        Map<String, Object> map = new HashMap();
        map.put("phone", senderId + "-" + sender.getPhone());
        map.put("amount", amount);
        map.put("schoolId", sender.getSchoolId());
        String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if (this.wxUserBellMapper.pay(map) == 1) {
            try {
                TxLog log = new TxLog(sender.getId(), "配送员提现", null, amount, "", sender.getSchoolId(), user.getAppId());
                if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                        school.getCertPath(), payId, "127.0.0.1", amount, user.getOpenId(), log) == 1) {
                    txLogMapper.insert(log);
                    if (schoolService.sendertx(map) == 0) {
                        LoggerUtil.log("配送员提现学校减少金额失败:" + senderId + ":" + amount);
                    }
                    return 1;
                }
            } catch (Exception e) {
                LoggerUtil.log(senderId + ":" + amount + e.getMessage());
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 2;
        }
        throw new YWException("余额不足");
    }*/

    @Override
    public int finddsh(int schoolId) {
        return senderMapper.finddsh(schoolId);
    }

    @Override
    public Sender check(String senderId) {
        return senderMapper.check(senderId);
    }

    @Override
    public List<Integer> findSenderIdBySchoolId(Integer schoolId) {
        return senderMapper.findSenderIdBySchoolId(schoolId);
    }


    /**
     * @date:   2019/8/28 23:12
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   sender
     * @Desc:   desc
     */
    @Override
    public ResponseObject findSenderByFree(Sender sender) {
        Assertions.notNull(sender, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(sender.getPhone(), ResponseViewEnums.SENDER_NEED_PHONE);
        QueryWrapper<Sender> wrapper = new QueryWrapper<>(sender);
        Sender senderFind = null;
        try {
            senderFind = senderMapper.selectOne(wrapper);
        }catch (Exception ex){
            ex.printStackTrace();
            DisplayException.throwMessage("查询异常，每个手机号只能注册一个配送员");
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS).push("sender",senderFind);
    }
}
