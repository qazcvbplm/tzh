package ops.school.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.config.Server;
import ops.school.api.dao.OrdersMapper;
import ops.school.api.dto.ShopTj;
import ops.school.api.dto.wxgzh.Message;
import ops.school.api.entity.*;
import ops.school.api.exception.YWException;
import ops.school.api.service.*;
import ops.school.api.util.RedisUtil;
import ops.school.api.wx.refund.RefundUtil;
import ops.school.api.wxutil.AmountUtils;
import ops.school.service.TOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Wrapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TOrdersServiceImpl implements TOrdersService {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private FloorService floorService;
    @Autowired
    private ProductAttributeService productAttributeService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private FullCutService fullCutService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil cache;

    @Transactional
    @Override
    public void addTakeout(Integer[] productIds, Integer[] attributeIndex, Integer[] counts, @Valid Orders orders) {
        WxUser wxUser = wxUserService.findById(orders.getOpenId());
        School school = schoolService.findById(wxUser.getSchoolId());
        Shop shop = shopService.getById(productService.getById(productIds[0]).getShopId());
        Floor floor = floorService.getById(orders.getFloorId());
        Product pt;
        ProductAttribute pa;
        int totalcount = 0;
        int boxcount = 0;
        boolean isDiscount = false;
        for (int i = 0; i < productIds.length; i++) {
            totalcount += counts[i];
            pt = productService.getById(productIds[i]);
            pa = productAttributeService.getById(attributeIndex[i]);
            OrderProduct op = new OrderProduct(productIds[i], pt.getProductName(), pt.getProductImage(), counts[i],
                    pt.getDiscount(), orders.getId(), pa.getName(), pa.getPrice());
            orderProductService.save(op);
            orders.setProductPrice(
                    orders.getProductPrice().add(op.getAttributePrice().multiply(new BigDecimal(counts[i]))));
            // 查看餐盒费
            if (pt.getBoxPriceFlag() == 1) {
                boxcount += counts[i];
            }
            // 计算商品折扣
            if (pa.getIsDiscount() == 1) {
                isDiscount = true;
                orders.setDiscountType("商品折扣");
                BigDecimal DiscountPrice = (pa.getPrice().subtract(op.getAttributePrice()))
                        .multiply(new BigDecimal(counts[i]));
                orders.setDiscountPrice(orders.getDiscountPrice().add(DiscountPrice));
            }
        }
        orders.takeoutinit1(wxUser, school, shop, floor, totalcount, isDiscount, fullCutService.findByShopId(shop.getId()),
                boxcount);
        ordersService.save(orders);
    }

    @Transactional
    @Override
    public int pay(Orders orders) {
        Shop shop = shopService.getById(orders.getShopId());
        School school = schoolService.findById(shop.getSchoolId());
        Application application = applicationService.getById(school.getAppId());
        if (application.getVipTakeoutDiscountFlag() == 1) {
            orders.setPayPrice((orders.getPayPrice().multiply(application.getVipTakeoutDiscount())).setScale(2,
                    BigDecimal.ROUND_HALF_DOWN));
        }

        Map<String, Object> map = new HashMap<>();
        WxUser user = wxUserService.findById(orders.getOpenId());
        map.put("phone", user.getOpenId() + "-" + user.getPhone());
        map.put("amount", orders.getPayPrice());
        if (wxUserBellService.pay(map) == 1) {
            if (paySuccess(orders.getId(), "余额支付") == 0) {
                throw new YWException("订单状态异常");
            }
            WxUser wxUser = wxUserService.findById(orders.getOpenId());
            WxUserBell userbell = wxUserBellService.getById(wxUser.getOpenId() + "-" + wxUser.getPhone());
            wxUserService.sendWXGZHM(wxUser.getPhone(), new Message(null, "JlaWQafk6M4M2FIh6s7kn30yPdy2Cd9k2qtG6o4SuDk"
                    , school.getWxAppId(), "pages/mine/payment/payment", " 您的会员帐户余额有变动！", "暂无", "-" + orders.getPayPrice(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "消费",
                    userbell.getMoney() + "", null, null,
                    null, null, "如有疑问请在小程序内联系客服人员！"));
            return 1;
        } else {
            throw new YWException("余额不足");
        }
    }

    @Transactional
    @Override
    public int paySuccess(String orderId, String payment) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("payment", payment);
        map.put("payTimeLong", System.currentTimeMillis());
        Orders orders = ordersService.findById(orderId);
        int rs = ordersService.paySuccess(map);
        if (rs == 1) {
            cache.takeoutCountadd(orders.getSchoolId());
            String ordersStr = JSON.toJSONString(orders);
            orders.setStatus("待接手");
            stringRedisTemplate.boundHashOps("SHOP_DJS" + orders.getShopId()).put(orderId, JSON.toJSONString(orders));
            stringRedisTemplate.boundHashOps("ALL_DJS").put(orderId, JSON.toJSONString(orders));
            stringRedisTemplate.convertAndSend(Server.SOCKET, ordersStr);
        }
        return rs;
    }

    @Transactional
    @Override
    public int cancel(String id) {
        Orders orders = ordersService.findById(id);
        if (System.currentTimeMillis() - orders.getPayTimeLong() < 5 * 60 * 1000) {
            return 2;
        }
        if (ordersService.cancel(id) == 1) {
            if (orders.getPayment().equals("微信支付")) {
                School school = schoolService.findById(orders.getSchoolId());
                String fee = AmountUtils.changeY2F(orders.getPayPrice().toString());
                int result = RefundUtil.wechatRefund1(school.getWxAppId(), school.getWxSecret(), school.getMchId(),
                        school.getWxPayId(), school.getCertPath(), orders.getId(), fee, fee);
                if (result != 1) {
                    throw new YWException("退款失败联系管理员");
                } else {
                    return orders.getShopId();
                }
            }
            if (orders.getPayment().equals("余额支付")) {
                Map<String, Object> map = new HashMap<>();
                WxUser user = wxUserService.findById(orders.getOpenId());
                map.put("phone", user.getOpenId() + "-" + user.getPhone());
                map.put("amount", orders.getPayPrice());
                // 取消订单时,将余额支付时的订单金额退回学校余额内
                Map<String,Object> map1 = new HashMap<>();
                map1.put("schoolId",user.getSchoolId());
                map1.put("charge",orders.getPayPrice());
                schoolService.charge(map1);
                if (wxUserBellService.charge(map) == 1) {
                    return orders.getShopId();
                } else {
                    throw new YWException("退款失败联系管理员");
                }
            }
        }
        return 0;
    }

    @Transactional
    @Override
    public int shopAcceptOrderById(String orderId) {
        Orders orders = ordersService.findById(orderId);
        synchronized (orders.getShopId()) {
            Orders update = new Orders();
            update.setShopId(orders.getShopId());
            update.setId(orderId);
            update.setPayTime(orders.getCreateTime().toString().substring(0, 10) + "%");
            synchronized (update.getShopId()) {
                int water = ordersService.waterNumber(update);
                update.setWaterNumber(water + 1);
            }
            if (ordersService.shopAcceptOrderById(update) == 1) {
                if (orders.getTyp().equals("堂食订单") || orders.getTyp().equals("自取订单")) {
                    stringRedisTemplate.opsForValue().set("tsout," + orderId, "1", 2, TimeUnit.HOURS);
                }
                WxUser wxUser = wxUserService.findById(orders.getOpenId());
                School school = schoolService.findById(wxUser.getSchoolId());
                wxUserService.sendWXGZHM(wxUser.getPhone(), new Message(null, "AFavOESyzBju1s8Wjete1SNVUvJr-YixgR67v6yMxpg",
                        school.getWxAppId(), "pages/order/orderDetail/orderDetail?orderId="
                        + orders.getId() + "&typ=" + orders.getTyp(),
                        "您的订单已被商家接手!", orderId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                        null, null, null, null, null, null,
                        null, " 商家正火速给您备餐中，请耐心等待"));
                return orders.getShopId();
            }
            return 0;
        }
    }


    @Override
    public ShopTj shopstatistics(Integer shopId, String beginTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", shopId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        List<Orders> list = ordersService.shopsta(map);
        if (list.size() > 0) {
            Orders temp = list.get(0);
            ShopTj rs = new ShopTj(Integer.valueOf(temp.getRemark()), temp.getFloorId(), temp.getPayPrice(), temp.getComplete(), temp.getBoxPrice(), temp.getSendPrice());
            return rs;
        }
        return new ShopTj(0, 0, new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
    }

}
