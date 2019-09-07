package ops.school.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import ops.school.App;
import ops.school.api.dao.OrdersCompleteMapper;
import ops.school.api.entity.*;
import ops.school.api.service.*;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.TimeUtilS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.*;

import static org.junit.Assert.*;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/9/7
 * 16:15
 * #
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class OrdersServiceTest {
    private Logger logger = LoggerFactory.getLogger(OrdersServiceTest.class);

    @Autowired
    private TOrdersService tOrdersService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrdersCompleteMapper ordersCompleteMapper;

    @Autowired
    private WxUserCouponService wxUserCouponService;

    @Autowired
    private SenderService senderService;

    @Autowired
    private WxUserService wxUserService;

/*    @Test
    public void testOrderComputeData(){
        Integer sum = 0;
        Long startTime = System.currentTimeMillis();
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("status","已完成");
        Date start = TimeUtilS.getDayBegin(TimeUtilS.getDateByEvery(2019,9-1,8,0,0,0),0);
        wrapper.le("end_time",start);
        // wrapper.in("Id","201909071419332971319179688","201909071357281815418855448");
        List<Orders> ordersList = ordersService.list(wrapper);
        //查店铺
        List<Integer> shopIds = PublicUtilS.getValueList(ordersList,"shopId");
        PublicUtilS.removeDuplicate(shopIds);
        Collection<Shop> shopList = shopService.listByIds(shopIds);
        Map<Integer,Shop> shopMap = PublicUtilS.listForMapValueE(shopList,"id");
        //查优惠券
        List<Long> userCouponIds = PublicUtilS.getValueList(ordersList,"couponId");
        PublicUtilS.removeDuplicate(userCouponIds);
        Collection<WxUserCoupon> wxUserCouponList = wxUserCouponService.listByIds(userCouponIds);
        List<Long> couponIds = PublicUtilS.getValueList(wxUserCouponList,"couponId");
        PublicUtilS.removeDuplicate(couponIds);
        Map<Long,Long> userAndCouponIdMap =  PublicUtilS.listForMap(wxUserCouponList,"id","couponId");
        Collection<Coupon> couponList = couponService.listByIds(couponIds);
        Map<Long,Coupon> couponMap = PublicUtilS.listForMapValueE(couponList,"id");
        //查配送员
        List<Integer> senderIds = PublicUtilS.getValueList(ordersList,"senderId");
        PublicUtilS.removeDuplicate(senderIds);
        Collection<Sender> senderList = senderService.listByIds(senderIds);
        Map<Integer,Sender> senderMap = PublicUtilS.listForMapValueE(senderList,"id");
        //senderUser
        List<String> openIds = PublicUtilS.getValueList(ordersList,"openId");
        List<String> openIds2 = PublicUtilS.getValueList(senderList,"openId");
        openIds.addAll(openIds2);
        PublicUtilS.removeDuplicate(openIds);
        Collection<WxUser> wxUserList = wxUserService.listByIds(openIds);
        Map<Integer,WxUser> wxUserListMap = PublicUtilS.listForMapValueE(wxUserList,"openId");

        List<OrdersComplete> completeList = new LinkedList<>();
        for (Orders temp : ordersList) {
            Orders orders = temp;
            if (couponMap.get(userAndCouponIdMap.get(orders.getCouponId())) == null && orders.getCouponId().intValue() != 0){
                logger.error("批量结算订单-优惠券空-订单号-"+orders.getId()+orders.getStatus()+orders.getTyp()+sum);
            }
            if (shopMap.get(orders.getShopId()) == null ){
                logger.error("批量结算订单-店铺空-订单号-"+orders.getId()+orders.getStatus()+orders.getTyp()+sum);
            }
            if (!"外卖订单".equals(orders.getTyp())){
                logger.info("");
            }
            if (orders.getSenderId() != null && senderMap.get(orders.getSenderId()) == null && orders.getSenderId().intValue() != 0 ){
                logger.info("");
            }
            ResponseObject response = null;
            try{
                if (senderMap.get(orders.getSenderId()) == null){
                    Sender sender = new Sender();
                    sender.setOpenId("0");
                    senderMap.put(orders.getSenderId(),sender);
                    logger.info("批量结算订单失败-跑腿信息空-订单号-"+orders.getId()+orders.getStatus()+orders.getTyp()+sum);
                }
                response = tOrdersService.orderComputeDataByOrders(
                        orders,shopMap.get(orders.getShopId()),
                        couponMap.get(userAndCouponIdMap.get(orders.getCouponId())),
                        senderMap.get(orders.getSenderId()),
                        wxUserListMap.get(senderMap.get(orders.getSenderId()).getOpenId()),
                        wxUserListMap.get(orders.getOpenId()));
            }catch (Exception e){
                logger.info(e.getMessage());
            }
            OrdersComplete ordersComplete = new OrdersComplete();
            ordersComplete = (OrdersComplete) response.getParams().get("ordersComplete");
            System.out.print("ordersComplete.getOrderId()"+ordersComplete.getOrderId()+"打印每个ordersComplete");
            logger.info(ordersComplete.getOrderId());
            if (ordersComplete == null){
                ordersComplete.setOrderId(orders.getId());
            }
            if (ordersComplete.getOrderId() == null){
                ordersComplete.setOrderId(orders.getId());
                logger.info("结算错误"+orders.getId()+"sum序号"+sum);
            }
            if (!response.isCode()){
                logger.info("批量结算订单失败-订单号-操作后不是true-"+orders.getId()+orders.getTyp()+sum+"-"+orders.getStatus());
            }else {
                completeList.add((OrdersComplete) response.getParams().get("ordersComplete"));
            }
            sum++;
            logger.info("sum"+sum);
        }
        sum = 0;
        for (OrdersComplete complete : completeList) {
            try{
                ordersCompleteMapper.insert(complete);
            }catch (Exception e){
                logger.info("保存订单失败-订单号-"+sum+"-"+ordersList.get(sum).getId());
            }
            sum++;
            logger.info("sum"+sum);
        }
//        Integer  saveNum = ordersCompleteMapper.batchInsertOrdersComplete(completeList);
        logger.info("查询总订单"+ordersList.size()+"批量结算订单+结算单数"+completeList.size()+"-orderComplete新增的条数"+sum);
        Long stopTime = System.currentTimeMillis();
        logger.info(String.valueOf((stopTime - startTime)/1000/60));
    }*/
}
