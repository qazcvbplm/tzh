package ops.school.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.qcloudsms.httpclient.HTTPException;
import jdk.nashorn.internal.scripts.JS;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.RedisConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.constants.WechatConfigConstants;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.api.dto.print.ShopPrintResultDTO;
import ops.school.api.entity.Orders;
import ops.school.api.entity.School;
import ops.school.api.entity.Shop;
import ops.school.api.service.OrderProductService;
import ops.school.api.service.OrdersService;
import ops.school.api.service.SchoolService;
import ops.school.api.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.awt.peer.ChoicePeer;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/12
 * 20:22
 * #
 */

@Component
@Service(value = "printTask")
public class PrintTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private SchoolService schoolService;

    private static CountDownLatch countDownLatch = new CountDownLatch(10);
    private final int orderOutTime = 1;


    /**
     * 每隔1分钟查询打印一次
     */
    @Scheduled(cron = "* */1 * * * ?")
    public void doPrintAndAcceptOrder() {
        Set<Integer> set = new HashSet();
        List<PrintDataDTO> sendMsgList = new ArrayList<>();
        List<PrintDataDTO> timeYesPrintList = new ArrayList<>();
        while (true) {
            Long redisSize = stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").size();
            if (redisSize < NumConstants.INT_NUM_1) {
                //先去把时间还在之内的数据重新入队
                if (timeYesPrintList.size() > NumConstants.INT_NUM_0) {
                    for (PrintDataDTO dataDTO : timeYesPrintList) {
                        dataDTO.setCycleRedisCount(dataDTO.getCycleRedisCount().intValue() + NumConstants.INT_NUM_1);
                        Long index = stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").leftPush(JSON.toJSONString(dataDTO));
                        if (index == NumConstants.INT_NUM_0){
                            LoggerUtil.logError("云打印消息丢失-原因-未超时订单轮询重新入队失败——"+dataDTO.toString());
                        }
                    }
                }
                //先发送微信消息
                //发送所有的流水号
                Map<Integer,List<PrintDataDTO>> schoolSendMap = PublicUtilS.listforEqualKeyListMap(sendMsgList
                        //sendMsgList
                        ,"ourSchoolId");
                for (Integer schoolId : schoolSendMap.keySet()) {
                    List<PrintDataDTO> schoolSendList = schoolSendMap.get(schoolId);
                    if (schoolSendList.size() < NumConstants.INT_NUM_1){
                        continue;
                    }
                    Set<String> shopNameSet = new HashSet<>();
                    Set<String> shopPhoneSet = new HashSet<>();
                    Map<Integer,List<Integer>> waterAllByShopMap = new HashMap<>();
                    for (PrintDataDTO sendDTO : schoolSendList) {
                        shopNameSet.add(sendDTO.getRealOrder().getShopName());
                        shopPhoneSet.add(sendDTO.getRealOrder().getShopPhone());
                        if (waterAllByShopMap.get(sendDTO.getOurShopId()) == null){
                            waterAllByShopMap.put(sendDTO.getOurShopId(),Arrays.asList(sendDTO.getWaterNumber()));
                        }else {
                            List<Integer> waterAll = new ArrayList<>();
                            waterAll.addAll(waterAllByShopMap.get(sendDTO.getOurShopId()));
                            waterAll.add(sendDTO.getWaterNumber());
                            waterAllByShopMap.put(sendDTO.getOurShopId(),waterAll);
                        }
                    }
                    StringBuilder param3waterArray = new StringBuilder();
                    param3waterArray.append("的流水号分别是:");
                    for (Integer shopId : waterAllByShopMap.keySet()) {
                        param3waterArray.append(waterAllByShopMap.get(shopId).toString());
                    }
                    String[] params = new String[3];
                    params[0] = shopNameSet.toString();
                    params[1] = shopPhoneSet.toString();
                    params[2] = param3waterArray.toString();
                    try {
                        Util.qqSmsNoConfig(schoolSendList.get(0).getSchoolLeaderPhone(), WechatConfigConstants.Tencent_Message_NOT_Print_Template, params);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后发送负责人短信失败-" + schoolSendList.get(0).getOurOrderId());
                    }
                }//流程结束

                int waitTime = 60;
                try {
                    //没有订单等xs
                    sendMsgList.clear();
                    timeYesPrintList.clear();
                    set.clear();
                    Thread.sleep(waitTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LoggerUtil.log(e.getMessage());
                }
                System.out.println("doPrintAndAcceptOrder 5 分钟队列没有订单循环{}s等待" + waitTime);
                continue;
            }//需要返回
            //redis 有订单了
            String findOrder = stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").rightPop();
            if (findOrder == null) {
                return;
            }
            PrintDataDTO printDataDTO = JSONObject.parseObject(findOrder, PrintDataDTO.class);
            /**
             *按照订单时间5分钟计算是否查询和提醒
             */
            System.out.println("执行了");
            Date nowTime = new Date();
            long differTimeMinutes = TimeUtilS.dateDiffMinutes(printDataDTO.getCreatTime(), nowTime);
            if (differTimeMinutes < orderOutTime) {
                timeYesPrintList.add(printDataDTO);
                continue;
            }
            Orders orders = null;
            if (printDataDTO != null && printDataDTO.getRealOrder() != null) {
                orders = printDataDTO.getRealOrder();
            } else {
                //订单为空，如果不能发送，跳过,放入失败队列
                LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后-订单缓存为空-订单和打印id" + printDataDTO.getOurOrderId() + "-" + printDataDTO.getPlatePrintOrderId());
                printDataDTO.setCycleRedisCount(printDataDTO.getCycleRedisCount() + NumConstants.INT_NUM_1);
                Long listIndex = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).leftPush(JSON.toJSONString(printDataDTO));
                Long outTime = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).getExpire();
                if (outTime != null && outTime.intValue() > 0) {
                    stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).expireAt(TimeUtilS.getNextDayNextTime(new Date(), 1, 10, 0, 0));
                }
                continue;
            }
            if (printDataDTO.getPrintBrand().intValue() != ShopPrintConfigConstants.PRINT_BRAND_DB_FEI_E) {
                LoggerUtil.logError("系统记录-定时器查询打印机-此订单是飞鹅打印不能处理-信息-" + printDataDTO.toString());
                continue;
            }
            //查询飞鹅打印
            ShopPrintResultDTO<Boolean> printResult = ShopPrintUtils.feiEGetPrintStatusYes(printDataDTO.getPlatePrintOrderId());
            //如果已打印不管
            if (printResult != null && printResult.isSuccess()) {
                continue;
            }
            //没有打印给负责人发消息
            //1-先查学校
            School school = schoolService.findById(orders.getSchoolId());
            if (school.getPhone() == null || orders.getShopName() == null || orders.getShopPhone() == null) {
                LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后发送负责人短信失败-订单" + orders.getId() + "负责人电话" + school.getPhone() + "店铺名称" + orders.getShopName() + "店铺电话" + orders.getShopPhone());
                printDataDTO.setCycleRedisCount(printDataDTO.getCycleRedisCount() + NumConstants.INT_NUM_1);
                Long listIndex = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).leftPush(JSON.toJSONString(printDataDTO));
                Long outTime = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).getExpire();
                if (outTime != null && outTime.intValue() > 0) {
                    stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).expireAt(TimeUtilS.getNextDayNextTime(new Date(), 1, 10, 0, 0));
                }
                continue;
            }
            //3参数在发送拼装
            printDataDTO.setSchoolLeaderPhone(school.getMessagePhone());
            sendMsgList.add(printDataDTO);
        }//true
    }

    private List<PrintDataDTO> buildOrderDTO() {
        String a1 = "{\"creatTime\":1568266392923,\"cycleRedisCount\":0,\"ourOrderId\":\"201909121333122541299619669\",\"ourSchoolId\":22,\"ourShopId\":215,\"platePrintSn\":\"test\",\"printBrand\":1,\"realOrder\":{\"addressDetail\":\"椰子大学,楼栋一,201\",\"addressName\":\"陈顺波\",\"addressPhone\":\"18857818257\",\"afterDiscountPrice\":15.00,\"appId\":1,\"boxPrice\":0.00,\"couponFullAmount\":0.00,\"couponId\":0,\"couponUsedAmount\":0.00,\"createTime\":\"2019-09-12 13:33:12\",\"destination\":0,\"discountPrice\":6.00,\"discountType\":\"商品折扣\",\"endTime\":\"\",\"evaluateFlag\":0,\"floorId\":104,\"fullAmount\":0.00,\"fullCutId\":0,\"fullUsedAmount\":0.00,\"id\":\"201909121333122541299619669\",\"op\":[{\"attributeName\":\"A1\",\"attributePrice\":10.00,\"id\":12266,\"orderId\":\"201909121333122541299619669\",\"productCount\":2,\"productDiscount\":6.0000,\"productId\":10276,\"productImage\":\"http://ops.chuyinkeji.cn/5fab35f4-270e-4737-ba15-53fa1009b0d2\",\"productName\":\"A1\",\"totalPrice\":4.0000}],\"openId\":\"oWP5G42cNW2gghAZiqvfOFDQ--0A\",\"originalPrice\":21.00,\"payFoodCoupon\":0.00,\"payPrice\":15.00,\"payTime\":\"2019-09-12 13:33:12\",\"payment\":\"\",\"productPrice\":20.00,\"remark\":\"无\",\"reseverTime\":\"14:13\",\"schoolId\":22,\"schoolTopDownPrice\":1.00,\"sendAddCountPrice\":0.00,\"sendAddDistancePrice\":0.00,\"sendBasePrice\":1.00,\"sendGetFlag\":0,\"sendPrice\":1.00,\"senderId\":0,\"shopAcceptTime\":\"\",\"shopAddress\":\"太和殿\",\"shopId\":215,\"shopImage\":\"http://ops.chuyinkeji.cn/5e499ede-ea15-4dcb-b7b5-435f4ed6ed98\",\"shopName\":\"十点见（本店装修ing）\",\"shopPhone\":\"18857818257\",\"status\":\"商家已接手\",\"typ\":\"外卖订单\",\"waterNumber\":2},\"sendMsg3Params\":[null,null,null],\"waterNumber\":2,\"yesPrintTrue\":1,\"schoolLeaderPhone\":17671464410}";
        String a2 = "{\"creatTime\":1568266335430,\"cycleRedisCount\":1,\"ourOrderId\":\"201909121331584106811382611\",\"ourSchoolId\":22,\"ourShopId\":214,\"platePrintKey\":\"0\",\"platePrintOrderId\":\"930511693_20190912133214_990025854\",\"platePrintSn\":\"930511693\",\"printBrand\":1,\"realOrder\":{\"addressDetail\":\"椰子大学,楼栋一,201\",\"addressName\":\"陈顺波\",\"addressPhone\":\"18857818257\",\"afterDiscountPrice\":7.05,\"appId\":1,\"boxPrice\":5.00,\"couponFullAmount\":0.00,\"couponId\":0,\"couponUsedAmount\":0.00,\"createTime\":\"2019-09-12 13:32:14\",\"destination\":0,\"discountPrice\":3.95,\"discountType\":\"商品折扣\",\"endTime\":\"\",\"evaluateFlag\":0,\"floorId\":104,\"fullAmount\":0.00,\"fullCutId\":0,\"fullUsedAmount\":0.00,\"id\":\"201909121331584106811382611\",\"op\":[{\"attributeName\":\"标准\",\"attributePrice\":1.00,\"id\":12263,\"orderId\":\"201909121331584106811382611\",\"productCount\":5,\"productDiscount\":3.9500,\"productId\":9119,\"productImage\":\"http://ops.sunwou.com/042e963f-856e-4de9-85b1-644344ff6d5b\",\"productName\":\"牛骨头\",\"totalPrice\":-2.9500}],\"openId\":\"oWP5G42cNW2gghAZiqvfOFDQ--0A\",\"originalPrice\":11.00,\"payFoodCoupon\":0.00,\"payPrice\":7.05,\"payTime\":\"2019-09-12 13:32:14\",\"payment\":\"\",\"productPrice\":5.00,\"remark\":\"无\",\"reseverTime\":\"14:11\",\"schoolId\":22,\"schoolTopDownPrice\":1.00,\"sendAddCountPrice\":0.00,\"sendAddDistancePrice\":0.00,\"sendBasePrice\":1.00,\"sendGetFlag\":0,\"sendPrice\":1.00,\"senderId\":0,\"shopAcceptTime\":\"\",\"shopAddress\":\"午门点位\",\"shopId\":214,\"shopImage\":\"http://ops.chuyinkeji.cn/c5ac4077-991c-433c-a261-aff3372c8b0c\",\"shopName\":\"熊猫来2\",\"shopPhone\":\"18857818257\",\"status\":\"商家已接手\",\"typ\":\"外卖订单\",\"waterNumber\":1},\"sendMsg3Params\":[null,null,null],\"waterNumber\":1,\"yesPrintTrue\":1,\"schoolLeaderPhone\":17671464410}";
        String a3 = "{\"creatTime\":1568266376892,\"cycleRedisCount\":0,\"ourOrderId\":\"201909121332561991296769637\",\"ourSchoolId\":27,\"ourShopId\":215,\"platePrintSn\":\"test\",\"printBrand\":1,\"realOrder\":{\"addressDetail\":\"椰子大学,楼栋一,201\",\"addressName\":\"陈顺波\",\"addressPhone\":\"18857818257\",\"afterDiscountPrice\":15.00,\"appId\":1,\"boxPrice\":0.00,\"couponFullAmount\":0.00,\"couponId\":0,\"couponUsedAmount\":0.00,\"createTime\":\"2019-09-12 13:32:56\",\"destination\":0,\"discountPrice\":6.00,\"discountType\":\"商品折扣\",\"endTime\":\"\",\"evaluateFlag\":0,\"floorId\":104,\"fullAmount\":0.00,\"fullCutId\":0,\"fullUsedAmount\":0.00,\"id\":\"201909121332561991296769637\",\"op\":[{\"attributeName\":\"A1\",\"attributePrice\":10.00,\"id\":12265,\"orderId\":\"201909121332561991296769637\",\"productCount\":2,\"productDiscount\":6.0000,\"productId\":10276,\"productImage\":\"http://ops.chuyinkeji.cn/5fab35f4-270e-4737-ba15-53fa1009b0d2\",\"productName\":\"A1\",\"totalPrice\":4.0000}],\"openId\":\"oWP5G42cNW2gghAZiqvfOFDQ--0A\",\"originalPrice\":21.00,\"payFoodCoupon\":0.00,\"payPrice\":15.00,\"payTime\":\"2019-09-12 13:32:56\",\"payment\":\"\",\"productPrice\":20.00,\"remark\":\"无\",\"reseverTime\":\"14:12\",\"schoolId\":27,\"schoolTopDownPrice\":1.00,\"sendAddCountPrice\":0.00,\"sendAddDistancePrice\":0.00,\"sendBasePrice\":1.00,\"sendGetFlag\":0,\"sendPrice\":1.00,\"senderId\":0,\"shopAcceptTime\":\"\",\"shopAddress\":\"太和殿\",\"shopId\":215,\"shopImage\":\"http://ops.chuyinkeji.cn/5e499ede-ea15-4dcb-b7b5-435f4ed6ed98\",\"shopName\":\"十点见（本店装修ing）\",\"shopPhone\":\"18857818257\",\"status\":\"商家已接手\",\"typ\":\"外卖订单\",\"waterNumber\":1},\"sendMsg3Params\":[null,null,null],\"waterNumber\":1,\"yesPrintTrue\":1,\"schoolLeaderPhone\":27}";
        String a4 = "{\"creatTime\":1568266335430,\"cycleRedisCount\":1,\"ourOrderId\":\"201909121331584106811382611\",\"ourSchoolId\":27,\"ourShopId\":214,\"platePrintKey\":\"0\",\"platePrintOrderId\":\"930511693_20190912133214_990025854\",\"platePrintSn\":\"930511693\",\"printBrand\":1,\"realOrder\":{\"addressDetail\":\"椰子大学,楼栋一,201\",\"addressName\":\"陈顺波\",\"addressPhone\":\"18857818257\",\"afterDiscountPrice\":7.05,\"appId\":1,\"boxPrice\":5.00,\"couponFullAmount\":0.00,\"couponId\":0,\"couponUsedAmount\":0.00,\"createTime\":\"2019-09-12 13:32:14\",\"destination\":0,\"discountPrice\":3.95,\"discountType\":\"商品折扣\",\"endTime\":\"\",\"evaluateFlag\":0,\"floorId\":104,\"fullAmount\":0.00,\"fullCutId\":0,\"fullUsedAmount\":0.00,\"id\":\"201909121331584106811382611\",\"op\":[{\"attributeName\":\"标准\",\"attributePrice\":1.00,\"id\":12263,\"orderId\":\"201909121331584106811382611\",\"productCount\":5,\"productDiscount\":3.9500,\"productId\":9119,\"productImage\":\"http://ops.sunwou.com/042e963f-856e-4de9-85b1-644344ff6d5b\",\"productName\":\"牛骨头\",\"totalPrice\":-2.9500}],\"openId\":\"oWP5G42cNW2gghAZiqvfOFDQ--0A\",\"originalPrice\":11.00,\"payFoodCoupon\":0.00,\"payPrice\":7.05,\"payTime\":\"2019-09-12 13:32:14\",\"payment\":\"\",\"productPrice\":5.00,\"remark\":\"无\",\"reseverTime\":\"14:11\",\"schoolId\":22,\"schoolTopDownPrice\":1.00,\"sendAddCountPrice\":0.00,\"sendAddDistancePrice\":0.00,\"sendBasePrice\":1.00,\"sendGetFlag\":0,\"sendPrice\":1.00,\"senderId\":0,\"shopAcceptTime\":\"\",\"shopAddress\":\"午门点位\",\"shopId\":214,\"shopImage\":\"http://ops.chuyinkeji.cn/c5ac4077-991c-433c-a261-aff3372c8b0c\",\"shopName\":\"熊猫来2\",\"shopPhone\":\"18857818257\",\"status\":\"商家已接手\",\"typ\":\"外卖订单\",\"waterNumber\":1},\"sendMsg3Params\":[null,null,null],\"waterNumber\":1,\"yesPrintTrue\":1,\"schoolLeaderPhone\":27}";
        String a5 = "{\"creatTime\":1568266335430,\"cycleRedisCount\":1,\"ourOrderId\":\"201909121331584106811382611\",\"ourSchoolId\":28,\"ourShopId\":214,\"platePrintKey\":\"0\",\"platePrintOrderId\":\"930511693_20190912133214_990025854\",\"platePrintSn\":\"930511693\",\"printBrand\":1,\"realOrder\":{\"addressDetail\":\"椰子大学,楼栋一,201\",\"addressName\":\"陈顺波\",\"addressPhone\":\"18857818257\",\"afterDiscountPrice\":7.05,\"appId\":1,\"boxPrice\":5.00,\"couponFullAmount\":0.00,\"couponId\":0,\"couponUsedAmount\":0.00,\"createTime\":\"2019-09-12 13:32:14\",\"destination\":0,\"discountPrice\":3.95,\"discountType\":\"商品折扣\",\"endTime\":\"\",\"evaluateFlag\":0,\"floorId\":104,\"fullAmount\":0.00,\"fullCutId\":0,\"fullUsedAmount\":0.00,\"id\":\"201909121331584106811382611\",\"op\":[{\"attributeName\":\"标准\",\"attributePrice\":1.00,\"id\":12263,\"orderId\":\"201909121331584106811382611\",\"productCount\":5,\"productDiscount\":3.9500,\"productId\":9119,\"productImage\":\"http://ops.sunwou.com/042e963f-856e-4de9-85b1-644344ff6d5b\",\"productName\":\"牛骨头\",\"totalPrice\":-2.9500}],\"openId\":\"oWP5G42cNW2gghAZiqvfOFDQ--0A\",\"originalPrice\":11.00,\"payFoodCoupon\":0.00,\"payPrice\":7.05,\"payTime\":\"2019-09-12 13:32:14\",\"payment\":\"\",\"productPrice\":5.00,\"remark\":\"无\",\"reseverTime\":\"14:11\",\"schoolId\":28,\"schoolTopDownPrice\":1.00,\"sendAddCountPrice\":0.00,\"sendAddDistancePrice\":0.00,\"sendBasePrice\":1.00,\"sendGetFlag\":0,\"sendPrice\":1.00,\"senderId\":0,\"shopAcceptTime\":\"\",\"shopAddress\":\"午门点位\",\"shopId\":214,\"shopImage\":\"http://ops.chuyinkeji.cn/c5ac4077-991c-433c-a261-aff3372c8b0c\",\"shopName\":\"熊猫来2\",\"shopPhone\":\"18857818257\",\"status\":\"商家已接手\",\"typ\":\"外卖订单\",\"waterNumber\":1},\"sendMsg3Params\":[null,null,null],\"waterNumber\":1,\"yesPrintTrue\":1,\"schoolLeaderPhone\":28}";
        List<PrintDataDTO> dtoList = new ArrayList<>();
        List<String> stringList = Arrays.asList(a1,a2,a3,a4,a5);
        for (String s : stringList) {
            dtoList.add(JSONObject.parseObject(s,PrintDataDTO.class));
        }
        return  dtoList;
    }


    /**
     * @param printDataDTO
     * @date: 2019/8/13 11:56
     * @author: QinDaoFang
     * @version:version
     * @return: void
     * @Desc: desc 把订单状态改成已接手
     */
    private Boolean changeOrderStatusToJS(PrintDataDTO printDataDTO) {
        //不做空判断，前面一定判断
        //先改数据库
        Orders update = new Orders();
        update.setWaterNumber(printDataDTO.getWaterNumber());
        update.setId(printDataDTO.getOurOrderId());
        Integer updateNum = ordersService.shopAcceptOrderById(update);
        if (updateNum != NumConstants.INT_NUM_1) {
            //如果失败
            return false;
        }
        //再改redis
        Long delNum = stringRedisTemplate.boundHashOps("SHOP_DJS" + printDataDTO.getOurShopId()).delete(printDataDTO.getOurOrderId());
        if (delNum.intValue() < NumConstants.INT_NUM_1) {
            //如果失败 只是记录日志
            LoggerUtil.logError("定时器-把订单状态改成已接手-changeOrderStatusToJS-删除缓存失败-" + "SHOP_DJS" + printDataDTO.getOurShopId() + printDataDTO.getOurOrderId());
        }
        return true;
    }


}

