package ops.school.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.*;
import ops.school.api.dao.OrdersMapper;
import ops.school.api.dao.SchoolMapper;
import ops.school.api.dao.WxUserBellMapper;
import ops.school.api.dto.ShopTj;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.api.dto.print.ShopPrintResultDTO;
import ops.school.api.dto.project.OrderTempDTO;
import ops.school.api.dto.project.ProductAndAttributeDTO;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.*;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.exception.YWException;
import ops.school.api.service.*;
import ops.school.api.serviceimple.ShopPrintServiceIMPL;
import ops.school.api.util.*;
import ops.school.api.wx.refund.RefundUtil;
import ops.school.api.wxutil.AmountUtils;
import ops.school.service.*;
import ops.school.api.wxutil.WxMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TOrdersServiceImpl implements TOrdersService {

    private final static Logger logger = LoggerFactory.getLogger(TOrdersServiceImpl.class);

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private TShopCouponService tShopCouponService;
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
    private OrdersMapper ordersMapper;
    @Autowired
    private TRunOrdersService tRunOrdersService;
    @Autowired
    private RunOrdersService runOrdersService;
    @Autowired
    private FullCutService fullCutService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil cache;
    @Autowired
    private TShopFullCutService tShopFullCutService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private WxUserCouponService wxUserCouponService;

    @Autowired
    private TWxUserCouponService tWxUserCouponService;

    @Autowired
    private TCouponService tCouponService;
    @Autowired
    private OrderCompleteService orderCompleteService;
    @Autowired
    private SenderService senderService;
    @Autowired
    private ShopFullCutService shopFullCutService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WxUserBellMapper wxUserBellMapper;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ShopPrintService shopPringService;

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
        for (int i = 0; i < productIds.length; i++) {
            totalcount += counts[i];
            BigDecimal discountPrice = new BigDecimal(0);
            // 商品信息
            pt = productService.getById(productIds[i]);
            // 商品规格信息
            pa = productAttributeService.getById(attributeIndex[i]);
            // Todo
            // 计算商品折扣
            // 商品表里有discount字段，需要判断discount是否小于1，小于1即开启
            if (pt.getDiscount().compareTo(new BigDecimal(1)) == -1) {
                orders.setDiscountType("商品折扣");
                // 优惠金额(1-折扣) * 规格价格
                discountPrice = pa.getPrice().multiply(new BigDecimal(1).subtract(pt.getDiscount()));
                orders.setDiscountPrice(discountPrice);
            } /*else if(){

            }*/
            OrderProduct op = new OrderProduct(productIds[i], pt.getProductName(), pt.getProductImage(), counts[i],
                    pt.getDiscount(), orders.getId(), pa.getName(), pa.getPrice());
            orderProductService.save(op);
            orders.setProductPrice(
                    orders.getProductPrice().add(op.getAttributePrice().multiply(new BigDecimal(counts[i]))));
            // 查看餐盒费
            if (pt.getBoxPriceFlag() == 1) {
                boxcount += counts[i];
            }
//            // 计算商品折扣
//            if (pa.getIsDiscount() == 1) {
//                orders.setDiscountType("商品折扣");
//            }
        }
        orders.takeoutinit1(wxUser, school, shop, floor, totalcount, false, fullCutService.findByShopId(shop.getId()),
                boxcount);
        ordersService.save(orders);
    }

    /**
     * @param productOrderDTOS --> 商品ID productId 商品规格ID attributeId 商品数量 count
     * @param orders           包含微信用户openid，学校id，店铺id，楼栋id，订单类型type,
     *                         配送费sendPrice, 餐盒费boxPrice, 用户优惠券ID wxUserCouponId, 实付款金额payPrice
     * @date: 2019/7/19 18:16
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @Desc: desc 用户提交订单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObject addOrder2(List<ProductOrderDTO> productOrderDTOS, @Valid Orders orders) {
//        Long startOrderTime = System.currentTimeMillis();
        //判断商品为空
        Assertions.notEmpty(productOrderDTOS, ResponseViewEnums.ORDER_DONT_HAVE_PRODUCT);
        //判断用户有
        WxUser wxUser = wxUserService.findById(orders.getOpenId());
        //根据用户的openid和phone查找用户和用户余额 todo
        // WxUser wxUser1 = wxUserService.findUserAndBellOrCache(orders.getOpenId(),orders.getWxUserPhone());
        Assertions.notNull(wxUser, ResponseViewEnums.WX_USER_NO_EXIST);
        //判断学校是否是有，并且是当前学校
        School school = schoolService.findById(wxUser.getSchoolId());
        Assertions.notNull(school, ResponseViewEnums.SCHOOL_HAD_CHANGE);
        //判断店铺有，暂时不做
        Shop shop = shopService.getById(orders.getShopId());
        Assertions.notNull(shop, ResponseViewEnums.SHOP_HAD_CHANGE);
        //楼栋判断
        Floor floor = floorService.getById(orders.getFloorId());
        Assertions.notNull(floor, ResponseViewEnums.FLOOR_SELECT_NULL);
        //判断商品有并且库存够，批量id查询
        Map pIdAndAIdMap = PublicUtilS.listForMap(productOrderDTOS, "attributeId", "productId");
        //根据商品id和商品规格id批量查询商品及规格
        List<ProductAndAttributeDTO> productAndAttributeS = productService.batchFindProdAttributeByIdS(pIdAndAIdMap);
        Map proAttributeSelectMap = PublicUtilS.listForMapValueE(productAndAttributeS, "attributeId");
        //假如前端传3个商品，查出来两个，有一个就没有，报错
        if (productAndAttributeS.size() < productOrderDTOS.size()) {
            //报错 商品信息变化
            DisplayException.throwMessageWithEnum(ResponseViewEnums.PRODUCT_HAD_CHANGE);
        }
        // 判断订单备注是否有表情内容
        String remarkOrder = CheckUtils.checkEmoji(orders.getRemark());
        if (remarkOrder != null) {
            orders.setRemark(remarkOrder);
        }
        //生成订单id
        String generatorOrderId = Util.GenerateOrderId();
        /**
         * 支付金额计算逻辑
         */
        /**
         * 变量
         */
        // 订单内所有商品的规格价格+配送费+餐盒费之和
        BigDecimal originalPrice = BigDecimal.ZERO;
        // 订单内所有商品的商品折扣之后的价格+配送费+餐盒费之和（如果没有商品折扣，则与规格价格之和相等）
        BigDecimal afterDiscountPrice = BigDecimal.ZERO;
        // 优惠券使用时的金额折后价格+餐盒费
        BigDecimal beforeCouponPrice = BigDecimal.ZERO;
        // 所有菜的原菜价
        BigDecimal productPrice = BigDecimal.ZERO;
        // 订单优惠了的价格 100元7折或者满100减30，这个是30
        BigDecimal discountPrice = BigDecimal.ZERO;
        // 餐盒费
        BigDecimal boxPrice = BigDecimal.ZERO;
        // 配送费
        BigDecimal sendPrice = BigDecimal.ZERO;
        // 额外距离配送费
        BigDecimal sendAddDistancePrice = BigDecimal.ZERO;
        // 额外件数配送费
        BigDecimal sendAddCountPrice = BigDecimal.ZERO;
        // 店铺满减总金额
        BigDecimal fullAmount = BigDecimal.ZERO;
        // 店铺满减可使用金额
        BigDecimal fullUsedAmount = BigDecimal.ZERO;
        // 优惠券满减额度
        BigDecimal couponFullAmount = BigDecimal.ZERO;
        // 优惠券使用额度
        BigDecimal couponUsedAmount = BigDecimal.ZERO;
        // 订单实付金额
        BigDecimal payPrice = BigDecimal.ZERO;
        // 粮票金额
        BigDecimal payFoodCoupon = new BigDecimal(0.00);
        // 优惠折扣是否使用
        Boolean isDiscount = false;
        // 订单内商品总数
        int totalcount = 0;
        // 餐盒总数（用于计算餐盒费的数量）
        int boxcount = 0;
        ProductAttribute productAttribute = null;
        Product product = null;
        //用于保存orderProduct
        List<OrderProduct> orderProductSaveList = new ArrayList<>();
        /**
         * 变量
         */
        // 新建一个Orders实体类
        Orders ordersSaveTemp = new Orders();
        //订单id
        ordersSaveTemp.setId(generatorOrderId);
        //校验商品
        //检查库存
        Map paramProductIdCountMap = PublicUtilS.listForMap(productOrderDTOS, "productId", "count");
        // 标识商品信息是否错误（库存不够），是否需要抛异常
        Boolean throwErrorNoStockYes = false;
        // 标识商品信息库存是否需要去修改），是否需要抛异常
        Boolean needToDisProductStockYes = false;
        // 库存不够抛异常的信息
        String noStockProdctNames = "";
        //用于扣库存，在计算金额就要减去库存
        List<Product> productDisStockList = new ArrayList<>();
        // 用于扣库存 临时存储product
        Product updateStockTempProduct = null;
        ProductAndAttributeDTO productAndAttributeDTOTemp = null;
        for (ProductOrderDTO productOrder : productOrderDTOS) {
            /**
             * 商品校验逻辑
             */
            productAndAttributeDTOTemp = (ProductAndAttributeDTO) proAttributeSelectMap.get(productOrder.getAttributeId());
            product = productAndAttributeDTOTemp.getProduct();
            productAttribute = productAndAttributeDTOTemp.getProductAttribute();
            Assertions.notNull(product, ResponseViewEnums.ORDER_DONT_HAVE_PRODUCT);
            Assertions.notNull(productAttribute, ResponseViewEnums.ORDER_DONT_HAVE_PRODUCT);
            //如果 商品id 属性id 计数不能空或者计数不能0 商品不能空
            if (productOrder.getProductId() == null || productOrder.getAttributeId() == null || productOrder.getCount() == null || productOrder.getCount() == 0) {
                DisplayException.throwMessageWithEnum(ResponseViewEnums.ORDER_PARAM_ERROR);
            }
//            假如前端传3个商品，查出来两个，有一个就没有，报错
            if (paramProductIdCountMap.get(product.getId()) == null) {
                throwErrorNoStockYes = true;
                noStockProdctNames += product.getProductName();
                //跳出循环，这个商品就不做逻辑处理
                continue;
                //如果参数查询的product的id在参数map中（一定在），并且商品开启库存
            } else if (product.getStockFlag().intValue() == ProductConstants.PRODUCT_STOCK_FLAG_YES) {
                needToDisProductStockYes = true;
                //那么比较库存够不够，不够
                if (product.getStock() < (Integer) paramProductIdCountMap.get(product.getId())) {
                    throwErrorNoStockYes = true;
                    noStockProdctNames += product.getProductName();
                    //跳出循环，这个商品就不做逻辑处理
                    continue;
                } else {
                    // 库存够，需要取修改库存
                    updateStockTempProduct = product;
                    updateStockTempProduct.setStock(product.getStock() - (Integer) paramProductIdCountMap.get(product.getId()));
                    //添加到list
                    productDisStockList.add(updateStockTempProduct);
                }
            }
            /**
             * 商品校验逻辑
             */
            // 订单内同一商品的数量
            Integer count = productOrder.getCount();
            /**
             * 计算订单内所有商品商品规格价格+配送费+餐盒费之和
             * 订单内所有商品的商品折扣之后的价格+配送费+餐盒费之和
             */
            if (productAttribute != null && count != 0) {
                originalPrice = originalPrice.add(productAttribute.getPrice().multiply(new BigDecimal(count)));
                afterDiscountPrice = afterDiscountPrice.add(productAttribute.getPrice().multiply(new BigDecimal(count)));
                productPrice = productPrice.add(productAttribute.getPrice().multiply(new BigDecimal(count)));
                if (product != null) {
                    // 订单商品表
                    OrderProduct orderProduct = new OrderProduct();
                    // 如果商品折扣小于1，即商品有折扣
                    if (product.getDiscount().compareTo(new BigDecimal(1)) == -1) {
                        ordersSaveTemp.setDiscountType("商品折扣");
                        // 优惠折扣已使用，店铺满减无法再使用
                        isDiscount = true;
                        // 使用商品折扣时的优惠价格
                        discountPrice = discountPrice.add(productAttribute.getPrice()
                                .multiply(new BigDecimal(1).subtract(product.getDiscount()))
                                .multiply(new BigDecimal(count)));
                        // 商品折扣之后的价格(原价-商品折扣价)
                        afterDiscountPrice = afterDiscountPrice.subtract(productAttribute.getPrice()
                                .multiply(new BigDecimal(1).subtract(product.getDiscount()))
                                .multiply(new BigDecimal(count)));
                        // 如果有折扣的话
                        orderProduct.setProductDiscount(discountPrice);
                        orderProduct.setTotalPrice(productAttribute.getPrice().subtract(discountPrice));
                    }
                    // 商品总数累加
                    totalcount += count;
                    // 餐盒数累加
                    if (product.getBoxPriceFlag() == 1) {
                        boxcount += count;
                    }
                    // 修改商品销量 + count
                    product.setSale(product.getSale() + count);
                    boolean rs = productService.updateById(product);
                    if (!rs) {
                        logger.error("商品销量更新失败，商品id为：" + product.getId());
                    }
                    orderProduct.setOrderId(generatorOrderId);
                    orderProduct.setAttributeName(productAttribute.getName());
                    orderProduct.setAttributePrice(productAttribute.getPrice());
                    orderProduct.setProductId(product.getId());
                    orderProduct.setProductName(product.getProductName());
                    orderProduct.setProductImage(product.getProductImage());
                    orderProduct.setProductCount(count);
                    orderProductSaveList.add(orderProduct);
                }
            }
        }
        //校验商品完成判断是否需要抛异常
        if (throwErrorNoStockYes) {
            DisplayException.throwMessage(noStockProdctNames + "卖完啦！");
        }
        /**
         * 餐盒费之和
         */
        // 餐盒费之和
        if (orders.getTyp().equals("外卖订单") || orders.getTyp().equals("自取订单")) {
            boxPrice = boxPrice.add(shop.getBoxPrice().multiply(new BigDecimal(boxcount)));
        } else {
            //其他的（堂食订单）数值还是要置为0
            boxPrice = new BigDecimal(0);
        }
        /**
         * 配送费
         */
        // 配送费-->按物品件数增加配送费
        if (orders.getTyp().equals("外卖订单")) {
            if (shop.getSendPriceAddByCountFlag() == 1) {
                sendAddCountPrice = sendAddCountPrice.add(new BigDecimal((totalcount - 1)).multiply(shop.getSendPriceAdd()));
            }
            // 配送费-->判断配送距离增加配送费
            int distance = BaiduUtil.DistanceAll(floor.getLat() + "," + floor.getLng(), shop.getLat() + "," + shop.getLng());
            if (distance > school.getSendMaxDistance()) {
                int per = ((distance - school.getSendMaxDistance()) / school.getSendPerOut()) + 1;
                sendAddDistancePrice = new BigDecimal(per).multiply(school.getSendPerMoney());
            }
            // 最终配送费-->基础配送费+额外距离配送费+额外件数配送费
            sendPrice = sendPrice.add(shop.getSendPrice()).add(sendAddCountPrice).add(sendAddDistancePrice);
        } else {
            // 其他的（堂食订单，自取订单）数值还是要置为0
            sendPrice = new BigDecimal(0);
        }
        // 如果商品折扣未使用-->店铺满减
        if (!isDiscount) {
            // 查询商家所有满减规则（从最大满减额度开始）
            List<FullCut> fullCuts = fullCutService.findByShopId(orders.getShopId());
            if (fullCuts.size() != 0) {
                for (FullCut shopFullCut : fullCuts) {
                    // 当原菜价满足店铺满减条件时 （>=）todo
                    if (originalPrice.compareTo(new BigDecimal(shopFullCut.getFull())) != -1) {
                        // 店铺满减之后的优惠价格 --> 如果原菜价 >= 折扣价格时
                        if (originalPrice.subtract(new BigDecimal(shopFullCut.getCut())).compareTo(BigDecimal.ZERO) != -1) {
                            // 优惠之后的折后价格
                            afterDiscountPrice = afterDiscountPrice.subtract(new BigDecimal(shopFullCut.getCut()));
                            discountPrice = discountPrice.add(new BigDecimal(shopFullCut.getCut()));
                        } else {
                            afterDiscountPrice = BigDecimal.ZERO;
                            discountPrice = discountPrice.add(originalPrice);
                        }
                        fullAmount = fullAmount.add(new BigDecimal(shopFullCut.getFull()));
                        fullUsedAmount = fullUsedAmount.add(new BigDecimal(shopFullCut.getCut()));
                        // 店铺满减表id
                        ordersSaveTemp.setFullCutId(Long.valueOf(shopFullCut.getId()));
                        ordersSaveTemp.setDiscountType("满减优惠");
                        break;
                    }
                }
            }
        }
        /**
         * 优惠券
         */
        // 折后价格+餐盒费-->优惠券使用时的价格
        beforeCouponPrice = beforeCouponPrice.add(afterDiscountPrice).add(boxPrice);
        // 在没有优惠券的时候，支付价格为折后价格
        payPrice = beforeCouponPrice;
        //优惠券id是wx的wxCouponId
        WxUserCoupon wxUserCoupon = null;
        if (orders.getCouponId() != null && orders.getCouponId() != 0) {
            wxUserCoupon = wxUserCouponService.getById(orders.getCouponId());
            // 当前时间戳
            Long currentTime = System.currentTimeMillis();
            // 用户优惠券失效 >= 当前时间
            if (wxUserCoupon != null && wxUserCoupon.getIsInvalid() == 0 && wxUserCoupon.getFailureTime().getTime() >= currentTime) {
                //注释
                Coupon coupon = couponService.getById(wxUserCoupon.getCouponId());
                //判断优惠券类型是
                if (coupon.getCouponType().intValue() == CouponConstants.COUPON_TYPE_SHOP || coupon.getCouponType().intValue() == CouponConstants.COUPON_TYPE_HOME) {
                    // 判断优惠卷只能在某个店铺使用
                    List<ShopCoupon> shopCouponList = tShopCouponService.findShopCouponBySIdAndCId(orders.getShopId(), coupon.getId());
                    if (shopCouponList == null || shopCouponList.size() == 0) {
                        DisplayException.throwMessageWithEnum(ResponseViewEnums.COUPON_CANT_USE_THIS_SHOP);
                    }
                }
                if (coupon != null) {
                    // 折后价格+餐盒费 >= 优惠券满减使用条件
                    if (beforeCouponPrice.compareTo(new BigDecimal(coupon.getFullAmount())) != -1) {
                        //  并且 折后价格+餐盒费 >= 优惠券满减金额时
                        if (beforeCouponPrice.subtract(new BigDecimal(coupon.getCutAmount())).compareTo(BigDecimal.ZERO) != -1) {
                            payPrice = payPrice.subtract(new BigDecimal(coupon.getCutAmount()));
                        } else {
                            payPrice = new BigDecimal(0.00);
                        }
                        couponFullAmount = couponUsedAmount.add(new BigDecimal(coupon.getFullAmount()));
                        couponUsedAmount = couponUsedAmount.add(new BigDecimal(coupon.getCutAmount()));
                        // 否则 payPrice = BigDecimal.ZERO
                        // 优惠券用完之后修改状态为已使用
                        wxUserCoupon.setIsInvalid(1);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            // 优惠券使用时间
                            wxUserCoupon.setUseTime(df.parse(df.format(new Date())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // tWxUserCouponService.updateIsInvalid(wxUserCoupon);
                    }
                }
            }
        }
        // 订单原价-->原菜价+配送费+餐盒费
        originalPrice = originalPrice.add(sendPrice).add(boxPrice);
        // 订单折扣之后的价格-->折后菜价+配送费+餐盒费
        afterDiscountPrice = afterDiscountPrice.add(sendPrice).add(boxPrice);
        // 折后价格-优惠券+餐盒费+配送费
        payPrice = payPrice.add(sendPrice);
        // 减去粮票
        QueryWrapper<WxUserBell> query = new QueryWrapper<>();
        query.lambda().eq(WxUserBell::getPhone, wxUser.getOpenId() + "-" + wxUser.getPhone());
        WxUserBell wxUserBell = wxUserBellService.getOne(query);
        if (wxUserBell != null) {
            if (wxUserBell.getFoodCoupon().compareTo(BigDecimal.ZERO) == 1) {
                // 折后价格-优惠券+餐盒费+配送费 >= 粮票
                if (payPrice.subtract(wxUserBell.getFoodCoupon()).compareTo(BigDecimal.ZERO) == -1) {
                    // 支付价格（除粮票外）小于 粮票--> 支付价格为  0
                    payPrice = new BigDecimal(0);
                    // 用户粮票修改为 --> 用户粮票余额 - 支付金额（除粮票外）
                    wxUserBell.setFoodCoupon(wxUserBell.getFoodCoupon().subtract(payPrice));
                    /**
                     * 消费粮票金额
                     */
                    payFoodCoupon = payFoodCoupon.add(payPrice);
                } else {
                    // 最终的实付款  --> 订单原菜价（原菜价+配送费+餐盒费）-粮票-优惠券-满减/折扣
                    payPrice = payPrice.subtract(wxUserBell.getFoodCoupon());
                    payFoodCoupon = payFoodCoupon.add(wxUserBell.getFoodCoupon());
                    wxUserBell.setFoodCoupon(BigDecimal.ZERO);
                }
            }
        }
        // 前端传来的数据对象
        OrderTempDTO tempDTO = new OrderTempDTO(orders.getSendPrice(), orders.getBoxPrice(), orders.getPayPrice(), orders.getProductPrice(),
                orders.getDiscountPrice(), orders.getPayFoodCoupon());
        // 后端计算的数据对象
        OrderTempDTO orderTempDTO = new OrderTempDTO(sendPrice, boxPrice, payPrice.setScale(2, BigDecimal.ROUND_HALF_UP),
                productPrice, discountPrice.setScale(2, BigDecimal.ROUND_HALF_UP), payFoodCoupon.setScale(2, BigDecimal.ROUND_HALF_UP));
        if (!tempDTO.thisCompareToTempTrue(orderTempDTO)) {
            return new ResponseObject(false, "订单金额有问题，请负责人进行核实!");
        }
        ordersSaveTemp.setDiscountPrice(discountPrice);
        ordersSaveTemp.setAddressDetail(orders.getAddressDetail());
        ordersSaveTemp.setAddressName(orders.getAddressName());
        ordersSaveTemp.setAddressPhone(orders.getAddressPhone());
        ordersSaveTemp.setAppId(school.getAppId());
        ordersSaveTemp.setBoxPrice(boxPrice);
        ordersSaveTemp.setCouponFullAmount(couponFullAmount);
        ordersSaveTemp.setCouponId(orders.getCouponId());
        ordersSaveTemp.setCouponUsedAmount(couponUsedAmount);
        ordersSaveTemp.setFloorId(orders.getFloorId());
        ordersSaveTemp.setFullAmount(fullAmount);
        ordersSaveTemp.setFullUsedAmount(fullUsedAmount);
        ordersSaveTemp.setOpenId(orders.getOpenId());
        ordersSaveTemp.setOriginalPrice(originalPrice);
        ordersSaveTemp.setPayFoodCoupon(orders.getPayFoodCoupon());
        ordersSaveTemp.setPayPrice(payPrice);
        ordersSaveTemp.setSendAddCountPrice(sendAddCountPrice);
        ordersSaveTemp.setSendAddDistancePrice(sendAddDistancePrice);
        ordersSaveTemp.setSendBasePrice(shop.getSendPrice());
        ordersSaveTemp.setSchoolId(school.getId());
        ordersSaveTemp.setSchoolTopDownPrice(school.getTopDown());
        ordersSaveTemp.setSendPrice(sendPrice);
        ordersSaveTemp.setTyp(orders.getTyp());
        ordersSaveTemp.setProductPrice(productPrice);
        ordersSaveTemp.setRemark(orders.getRemark());
        ordersSaveTemp.setReseverTime(orders.getReseverTime());
        ordersSaveTemp.setShopId(shop.getId());
        ordersSaveTemp.setShopAddress(shop.getShopAddress());
        ordersSaveTemp.setShopImage(shop.getShopImage());
        ordersSaveTemp.setShopName(shop.getShopName());
        ordersSaveTemp.setShopPhone(shop.getShopPhone());
        ordersSaveTemp.setAfterDiscountPrice(afterDiscountPrice);
        ordersSaveTemp.setCreateTime(new Date());
        ordersSaveTemp.setOp(orderProductSaveList);
        /**
         * 支付金额计算逻辑
         */

        //保存订单逻辑
        boolean saveOrderSuccess = ordersService.save(ordersSaveTemp);
        if (!saveOrderSuccess) {
            DisplayException.throwMessageWithEnum(ResponseViewEnums.ORDER_SAVE_ERROR);
        }
        // 修改用户粮票余额
        boolean rs = wxUserBellService.updateById(wxUserBell);
        if (!rs) {
            logger.error("修改用户粮票金额失败，用户手机号为:" + wxUser.getPhone());
        }
        //保存订单商品逻辑，不行就要自己写接口
        boolean saveOPSuccess = orderProductService.saveBatch(orderProductSaveList);
        if (!saveOPSuccess) {
            logger.error("订单商品保存失败，商品信息：" + PublicUtilS.getCollectionToString(orderProductSaveList));
        }
        //下单完成后扣库存
        if (needToDisProductStockYes) {
            boolean disProductStockSuccess = productService.saveOrUpdateBatch(productDisStockList);
            if (!disProductStockSuccess) {
                // 这里想的扣库存失败还是可以下单
                logger.error("商品扣库存失败，商品信息：" + PublicUtilS.getCollectionToString(productDisStockList));
            }
        }
        //下单后领优惠券 新页面新接口
        //用户优惠券失效逻辑,如果用户的优惠券是生效的IsInvalid=0
        if (wxUserCoupon != null) {
            // 优惠券
            Coupon coupon = couponService.getById(wxUserCoupon.getCouponId());
            if (coupon != null && coupon.getIsInvalid() != 1) {
                wxUserCoupon.setIsInvalid(NumConstants.DB_TABLE_IS_INVALID_NO);
                int updateUserCouponNum = tWxUserCouponService.updateIsInvalid(wxUserCoupon);
                if (updateUserCouponNum != NumConstants.INT_NUM_1) {
                    logger.error("修改优惠券失效失败，用户优惠券id" + orders.getCouponId());
                }
            }
        }
        Map resultMap = new HashMap();
        resultMap.put("orderId", generatorOrderId);
        resultMap.put("createTime",ordersSaveTemp.getCreateTime());
        //订单商品存缓存
        stringRedisTemplate.boundHashOps(RedisConstants.Shop_Need_Pay_Product+ ordersSaveTemp.getShopId()).put(ordersSaveTemp.getId(), JSON.toJSONString(ordersSaveTemp));
        //当天晚上缓存过期
        stringRedisTemplate.boundHashOps(RedisConstants.Shop_Need_Pay_Product+ ordersSaveTemp.getShopId()).expireAt(TimeUtilS.getDayEnd());
//        Long endOrderTime = System.currentTimeMillis();
//        System.out.println(endOrderTime - startOrderTime);
        return new ResponseObject(true, "创建订单成功！", resultMap);
    }

    @Transactional
    @Override
    public int pay(Orders orders, String formid) {
        Shop shop = shopService.getById(orders.getShopId());
        School school = schoolService.findById(shop.getSchoolId());
        // 余额支付，用户数据更新
        Map<String, Object> map = new HashMap<>();
        WxUser user = wxUserService.findById(orders.getOpenId());
        map.put("phone", user.getOpenId() + "-" + user.getPhone());
        map.put("amount", orders.getPayPrice());
        if (wxUserBellService.pay(map) == 1) {
            //扣除学校余额数据和粮票余额
            Integer disSCNum = schoolService.disScUserBellAllAndUserSBellByScIdCan0(orders.getPayPrice(), orders.getPayFoodCoupon(), school.getId());
            if (disSCNum != NumConstants.INT_NUM_1) {
                logger.error("系统异常- {},订单id{}，支付价格{}，粮票{}，学校id{}",ResponseViewEnums.PAY_ERROR_SCHOOL_FAILED.getErrorMessage(),orders.getId(),orders.getPayPrice(), orders.getPayFoodCoupon(), school.getId());

            }
            if (paySuccess(orders.getId(), "余额支付") == 0) {
                stringRedisTemplate.boundHashOps("SHOP_DJS" + orders.getShopId()).delete(orders.getId().toString());
                stringRedisTemplate.boundHashOps("ALL_DJS").delete(orders.getId().toString());
                //删除订单product
                Long delNum = stringRedisTemplate.boundHashOps(RedisConstants.Shop_Need_Pay_Product + orders.getShopId().toString()).delete(orders.getId());
                throw new YWException("订单状态异常");
            }
            String[] formIds = formid.split(",");
            if (formIds.length < 1) {
                LoggerUtil.logError("order pay formid为空" + orders.getId());
            }
            stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
            stringRedisTemplate.boundListOps("FORMID" + orders.getId()).leftPushAll(formIds);
            stringRedisTemplate.expire("FORMID" + orders.getId(), 1, TimeUnit.DAYS);
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
            orders.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            //获取订单op
            String shopDjs = (String) stringRedisTemplate.boundHashOps(RedisConstants.Shop_Need_Pay_Product + orders.getShopId().toString()).get(orderId);
            Orders ordersTemp = null;
            if (shopDjs != null){
                ordersTemp = JSONObject.parseObject(shopDjs,Orders.class);
                Long delNum = stringRedisTemplate.boundHashOps(RedisConstants.Shop_Need_Pay_Product + orders.getShopId().toString()).delete(orderId);
            }
            if (ordersTemp.getOp() != null && ordersTemp.getOp().size() > 0){
                orders.setOp(ordersTemp.getOp());
            }
            stringRedisTemplate.boundHashOps("SHOP_DJS" + orders.getShopId()).put(orderId, JSON.toJSONString(orders));
            stringRedisTemplate.boundHashOps("ALL_DJS").put(orderId, JSON.toJSONString(orders));
        }
        return rs;
    }

    @Transactional
    @Override
    public int cancel(String id) {
        Orders orders = ordersService.findById(id);
        // 查询订单商品表信息
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderProduct::getOrderId, id);
        List<OrderProduct> orderProducts = orderProductService.list(queryWrapper);
        WxUser user = wxUserService.findById(orders.getOpenId());
        School school = schoolService.findById(user.getSchoolId());
        List<Product> productDisStockList = new ArrayList<>();
        if (!orders.getStatus().equals("待付款")) {
            if (System.currentTimeMillis() - orders.getPayTimeLong() < 5 * 60 * 1000) {
                throw new YWException("需要至少5分钟才能退款");
            }
        }
        String temp = orders.getStatus();
        if (ordersService.cancel(id) == 1) {
            stringRedisTemplate.boundHashOps("SHOP_DJS" + orders.getShopId()).delete(id);
            stringRedisTemplate.boundHashOps("ALL_DJS").delete(id);
            if (orderProducts != null || orderProducts.size() > 0) {
                for (OrderProduct orderProduct : orderProducts) {
                    Product product = productService.getById(orderProduct.getProductId());
                    if (product.getStockFlag() == 1) {
                        product.setStock(product.getStock() + orderProduct.getProductCount());
                        productDisStockList.add(product);
                    }
                }
            }
            // 当订单内粮票额度不等于0时
            if (orders.getPayFoodCoupon().compareTo(new BigDecimal(0)) != 0) {
                // 订单消费的粮票要退回用户粮票内
                wxUserBellService.addFoodCoupon(user.getOpenId() + "-" + user.getPhone(), orders.getPayFoodCoupon());
            }
            // 如果订单使用优惠券 --> 退还优惠券
            if (orders.getCouponId() != null && orders.getCouponId() != 0) {
                // 用户优惠券
                WxUserCoupon wxUserCoupon = wxUserCouponService.getById(orders.getCouponId());
                if (wxUserCoupon != null) {
                    // 修改状态为0
                    wxUserCoupon.setIsInvalid(NumConstants.DB_TABLE_IS_INVALID_NOT_USED);
                    int updateUserCouponNum = tWxUserCouponService.updateIsInvalid(wxUserCoupon);
                    if (updateUserCouponNum != NumConstants.INT_NUM_1) {
                        logger.error("修改优惠券失效失败，用户优惠券id" + orders.getCouponId());
                    }
                }
            }
            if (!temp.equals("待付款")) {
                if (orders.getPayment().equals("余额支付")) {
                    // 取消订单时,将余额支付时的订单金额退回学校余额内
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("schoolId", user.getSchoolId());
                    map1.put("charge", orders.getPayPrice());
                    map1.put("send", orders.getPayFoodCoupon());
                    schoolService.charge(map1);
                    stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
                    // 订单消费的余额要退回用户余额内
                    Map<String, Object> map = new HashMap<>();
                    map.put("phone", user.getOpenId() + "-" + user.getPhone());
                    map.put("amount", orders.getPayPrice());
                    if (wxUserBellService.charge(map) == 1) {
                        //集合大于0才会扣库存
                        if (productDisStockList.size() > 0) {
                            boolean disProductStockSuccess = productService.saveOrUpdateBatch(productDisStockList);
                            if (!disProductStockSuccess) {
                                // 这里想的扣库存失败还是可以下单
                                logger.error("商品扣库存失败，商品信息：" + PublicUtilS.getCollectionToString(productDisStockList));
                            }
                        }
                        return orders.getShopId();
                    } else {
                        throw new YWException("退款失败联系管理员");
                    }
                } else if (orders.getPayment().equals("微信支付")) {
                    // 取消订单时,将微信支付时的粮票退回学校余额粮票内
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("schoolId", user.getSchoolId());
                    map1.put("charge", NumConstants.INT_NUM_0);
                    map1.put("send", orders.getPayFoodCoupon());
                    schoolService.charge(map1);
                    stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
                    String fee = AmountUtils.changeY2F(orders.getPayPrice().toString());
                    if (orders.getPayPrice().multiply(new BigDecimal(100)).compareTo(new BigDecimal(fee)) != NumConstants.INT_NUM_0) {
                        DisplayException.throwMessageWithEnum(ResponseViewEnums.WX_TUI_KUAN_ERROR);
                    }
                    int result = RefundUtil.wechatRefund1(school.getWxAppId(), school.getWxSecret(), school.getMchId(), school.getWxPayId(), school.getCertPath(), orders.getId(), fee, fee);
                    if (result != 1) {
                        throw new YWException("微信退款失败联系管理员");
                    } else {
                        //集合大于0才会扣库存
                        if (productDisStockList.size() > 0) {
                            boolean disProductStockSuccess = productService.saveOrUpdateBatch(productDisStockList);
                            if (!disProductStockSuccess) {
                                // 这里想的扣库存失败还是可以下单
                                logger.error("商品扣库存失败，商品信息：" + PublicUtilS.getCollectionToString(productDisStockList));
                            }
                        }
                        return orders.getShopId();
                    }
                }
            } else {
                //待付款
                //集合大于0才会扣库存 开启库存
                if (productDisStockList.size() > 0) {
                    boolean disProductStockSuccess = productService.saveOrUpdateBatch(productDisStockList);
                    if (!disProductStockSuccess) {
                        // 这里想的扣库存失败还是可以下单
                        logger.error("商品扣库存失败，商品信息：" + PublicUtilS.getCollectionToString(productDisStockList));
                    }
                }
                return orders.getShopId();
            }

        }
        return 0;
    }

    @Transactional
    @Override
    public int shopAcceptOrderById(String orderId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Orders::getId, orderId);
        Orders orders = ordersService.getOne(queryWrapper);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Orders update = new Orders();
        update.setShopId(orders.getShopId());
        update.setId(orderId);
        update.setPayTime(df.format(orders.getCreateTime()).substring(0, 10) + "%");
        School school = schoolMapper.selectById(orders.getSchoolId());
        //流水号
        Integer water = this.getWaterByRedis(orders.getShopId(),school.getYesWaterOne(),orders.getSchoolId());
        orders.setWaterNumber(water);
        update.setWaterNumber(water);
        int res = ordersService.shopAcceptOrderById(update);
        if (res == 1) {
            if (orders.getTyp().equals("堂食订单") || orders.getTyp().equals("自取订单")) {
                stringRedisTemplate.opsForValue().set("tsout," + orderId, "1", 2, TimeUnit.HOURS);
                ///stringRedisTemplate.opsForValue().set("tsout," + orderId, "1", 2, TimeUnit.MINUTES);
            }
            List<String> formIds = new ArrayList<>();
            try {
                formIds = stringRedisTemplate.boundListOps("FORMID" + orders.getId()).range(0, -1);
            } catch (Exception ex) {
                LoggerUtil.logError("商家接手外卖订单-shopAcceptOrderById-完成发送消息失败，formid取缓存为空" + orders.getId());
            }
            if (formIds.size() > 0) {
                // 查询订单商品表信息
                QueryWrapper<OrderProduct> productWrapper = new QueryWrapper<>();
                productWrapper.lambda().eq(OrderProduct::getOrderId, orderId);
                List<OrderProduct> orderProducts = orderProductService.list(productWrapper);
                orders.setOp(orderProducts);
                orders.setStatus("待接手");
                try{
                    WxMessageUtil.wxSendMsg(orders, formIds.get(0),orders.getSchoolId());
                    stringRedisTemplate.boundListOps("FORMID" + orders.getId()).remove(1, formIds.get(0));
                }catch (Exception ex){
                    LoggerUtil.logError("wx发送消息失败-shopAcceptOrderById-"+ex.getMessage());
                }
            } else {
                LoggerUtil.logError("商家接手外卖订单-shopAcceptOrderById-完成发送消息失败，发送或者删除redis失败" + orders.getId());
            }

            return orders.getShopId();
        }
        return 0;
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

    @Override
    public Map countKindsOrderByBIdAndTime(Integer buildId, String beginTime, String endTime) {
        Assertions.notNull(buildId, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.hasText(beginTime, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.hasText(endTime, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //Floor floor = floorService.getById(buildId);
        //查询楼栋
        QueryWrapper<Floor> queryWrapperFloor = new QueryWrapper<>();
        queryWrapperFloor.select("id");
        queryWrapperFloor.eq("Id", buildId);
        Floor floor = floorService.getOne(queryWrapperFloor);
        Assertions.notNull(floor, ResponseViewEnums.FLOOR_SELECT_NULL);
        //外卖订单
        QueryWrapper<Orders> queryWrapperTakeOut = new QueryWrapper<>();
        queryWrapperTakeOut
                .eq("floor_id", buildId)
                .eq("typ", OrderConstants.orderTypeTakeOut)
                .ge("create_time", beginTime)
                .le("create_time", endTime);
        Integer allOrdersTakeOut = ordersService.count(queryWrapperTakeOut);
        //堂食订单
        QueryWrapper<Orders> queryWrapperEatHere = new QueryWrapper<>();
        queryWrapperEatHere
                .eq("floor_id", buildId)
                .eq("typ", OrderConstants.queryWrapperEatHere)
                .ge("create_time", beginTime)
                .le("create_time", endTime);
        Integer allOrdersEatHere = ordersService.count(queryWrapperEatHere);
        //跑腿订单
        QueryWrapper<RunOrders> queryWrapperRunning = new QueryWrapper<>();
        queryWrapperRunning
                .eq("floor_id", buildId)
                .eq("typ", OrderConstants.orderTypeRunning)
                .between("create_time", beginTime, endTime);
        Integer allOrdersRunning = runOrdersService.count(queryWrapperRunning);
        //自取订单
        QueryWrapper<Orders> queryWrapperGetSelf = new QueryWrapper<>();
        queryWrapperGetSelf
                .eq("floor_id", buildId)
                .eq("typ", OrderConstants.queryWrapperGetSelf)
                .between("create_time", beginTime, endTime);
        Integer allOrdersGetSelf = ordersService.count(queryWrapperGetSelf);
        //订单总数
        Integer allOrders = allOrdersTakeOut + allOrdersEatHere + allOrdersRunning + allOrdersGetSelf;
        //营业额
        // 商品订单总营业额
        Map<String, Object> map = new HashMap<>();
        map.put("floorId", buildId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        BigDecimal ordersCountPayPrice = BigDecimal.ZERO;
        if (ordersMapper.countPayPriceByFloor(map) != null) {
            ordersCountPayPrice = ordersMapper.countPayPriceByFloor(map);
        }
        // 跑腿订单总营业额
        BigDecimal runOrdersCountTotalPrice = BigDecimal.ZERO;

        if (tRunOrdersService.countTotalPriceByFloor(buildId, beginTime, endTime) != null) {
            runOrdersCountTotalPrice = tRunOrdersService.countTotalPriceByFloor(buildId, beginTime, endTime);
        }
        BigDecimal ordersAllMoney = ordersCountPayPrice.add(runOrdersCountTotalPrice);
        Map result = new HashMap();
        result.put("allOrders", allOrders);
        result.put("allOrdersTakeOut", allOrdersTakeOut);
        result.put("allOrdersEatHere", allOrdersEatHere);
        result.put("allOrdersRunning", allOrdersRunning);
        result.put("allOrdersGetSelf", allOrdersGetSelf);
        result.put("ordersAllMoney", ordersAllMoney);
        return result;
    }


    @Override
    public int orderSettlement(String orderId) {
        Assertions.hasLength(orderId);
        // 订单信息
        Orders orders = ordersService.findById(orderId);
        Assertions.notNull(orders);
        if (!"已完成".equals(orders.getStatus())) {
            LoggerUtil.logError("完成订单异常，订单不是已完成状态：订单id-" + orderId);
            return -1;
        }
        Boolean endTrue = this.orderSettlementByOrders(orders);
        if (!endTrue) {
            return -1;
        }
        return 1;
    }

    /**
     * @param
     * @date: 2019/8/6 15:40
     * @author: QinDaoFang
     * @version:version
     * @return: int
     * @Desc: desc 二次修改 传orders结算少一个查库
     */
    @Transactional
    @Override
    public Boolean orderSettlementByOrders(Orders orders) {
        QueryWrapper<OrdersComplete> query = new QueryWrapper<>();
        query.lambda().eq(OrdersComplete::getOrderId, orders.getId());
        // 查询订单完成表信息是否存在，存在则不可以结算
        OrdersComplete orderComplete1 = orderCompleteService.getOne(query);
        if (orderComplete1 != null) {
            Assertions.notNull(orderComplete1, ResponseViewEnums.ORDERS_COMPLETE_HAD_ERROR);
        }

        // 对订单进行校验
        Assertions.notNull(orders, ResponseViewEnums.ORDER_PARAM_ERROR);
        // 配送员
        Sender sender = senderService.findById(orders.getSenderId());
        WxUser senderUser = wxUserService.findById(sender.getOpenId());
        // 对配送员信息进行校验
        Assertions.notNull(sender, ResponseViewEnums.SCHOOL_HAD_CHANGE);
        Assertions.notNull(senderUser, ResponseViewEnums.SCHOOL_HAD_CHANGE);
        WxUser orderUser = wxUserService.findById(orders.getOpenId());
        Assertions.notNull(orderUser);
        // 店铺
        Shop shop = shopService.getById(orders.getShopId());
        // 对店铺信息进行校验
        Assertions.notNull(shop, ResponseViewEnums.SHOP_HAD_CHANGE);
        // 学校
        School school = schoolService.findById(orders.getSchoolId());
        // 对学校信息进行校验
        Assertions.notNull(school, ResponseViewEnums.SCHOOL_HAD_CHANGE);
        // 对订单进行结算
        OrdersComplete ordersComplete = new OrdersComplete();
        // 平台抽负责人百分比
        ordersComplete.setAppGetSchoolRate(school.getRate());
        // 负责人抽成店铺百分比
        ordersComplete.setSchoolGetShopRate(shop.getRate());
        // 用户优惠券
        WxUserCoupon wxUserCoupon = new WxUserCoupon();
        Coupon coupon = new Coupon();
        // 店铺满减
        // 优惠券金额
        BigDecimal couponAmount = BigDecimal.ZERO;
        // 店铺满减金额
        BigDecimal fullCutAmount = BigDecimal.ZERO;
        // 商品折扣金额
        BigDecimal discountAmount = BigDecimal.ZERO;
        // (餐盒费＋菜价 - 优惠券 - 满减额 - 折扣额）
        BigDecimal tempPrice = BigDecimal.ZERO;
        // 配送员所得金额
        BigDecimal senderGetTotal = BigDecimal.ZERO;
        // 店铺所得金额
        BigDecimal shopGetTotal = BigDecimal.ZERO;
        // 负责人抽取配送员金额
        BigDecimal schoolGetSender = BigDecimal.ZERO;
        // 负责人抽取店铺金额
        BigDecimal schoolGetshop = BigDecimal.ZERO;
        // 负责人所得
        BigDecimal schoolGetTotal = BigDecimal.ZERO;
        // 负责人承担比例金额之和
        BigDecimal schoolUnderTakeAmount = BigDecimal.ZERO;
        // 楼下返还金额
        BigDecimal downStairs = BigDecimal.ZERO;
        // 是否有优惠券
        Boolean isCoupon = false;
        // 负责人抽成配送员百分比
        // 如果配送员为空
        if (orders.getSenderId() == 0 || orders.getSenderId() == null) {
            ordersComplete.setSchoolGetSenderRate(BigDecimal.ZERO);
            /**
             * 配送员所得
             */
            ordersComplete.setSenderGetTotal(senderGetTotal);
            /**
             * 负责人抽取配送员金额
             */
            ordersComplete.setSchoolGetSender(schoolGetSender);
        } else {
            ordersComplete.setSchoolGetSenderRate(sender.getRate());
            // 配送员所得
            // 如果时楼上送达 --> 配送费 * （1-学校抽成）
            if (orders.getDestination() == 1) {
                /**
                 * 配送员所得
                 */
                ordersComplete.setSenderGetTotal(orders.getSendPrice().multiply(new BigDecimal(1).subtract(sender.getRate())));
                /**
                 * 负责人抽取配送员所得
                 */
                schoolGetSender = schoolGetSender.add(orders.getSendPrice().multiply(sender.getRate()));
                ordersComplete.setSchoolGetSender(schoolGetSender);
            } else {
                // 楼下送达，要返还楼上楼下差价 --> （配送费-楼下返还） * （1-学校抽成）
                // 楼下返还金额
                downStairs = downStairs.add(orders.getSchoolTopDownPrice());
                /**
                 * 配送员所得
                 */
                ordersComplete.setSenderGetTotal((orders.getSendPrice().subtract(downStairs))
                        .multiply(new BigDecimal(1).subtract(sender.getRate())));
                /**
                 * 负责人抽取配送员所得
                 */
                schoolGetSender = schoolGetSender.add((orders.getSendPrice().subtract(downStairs))
                        .multiply(sender.getRate()));
                ordersComplete.setSchoolGetSender(schoolGetSender);
            }
        }
        // 超级后台所得对应金额 --> (原价－粮票 - 优惠券 - 满减额 - 折扣额）＊  比例
        BigDecimal appGetTotal = orders.getPayPrice().multiply(school.getRate());
        /**
         * 超级后台所得
         */
        ordersComplete.setAppGetTotal(appGetTotal);
        // 如果使用了优惠券
        if (orders.getCouponId() != 0 && orders.getCouponId() != null) {
            isCoupon = true;
            wxUserCoupon = wxUserCouponService.getById(orders.getCouponId());
            coupon = couponService.getById(wxUserCoupon.getCouponId());
            // 优惠券优惠金额
            couponAmount = couponAmount.add(new BigDecimal(coupon.getCutAmount()));
        }
        // 如果使用了店铺满减
        if (orders.getFullCutId() != null && orders.getFullCutId() != 0) {
            // 店铺满减优惠金额
            fullCutAmount = fullCutAmount.add(orders.getFullUsedAmount());
        } else if (orders.getDiscountPrice().compareTo(BigDecimal.ZERO) == 1) {
            // 商品折扣金额
            discountAmount = discountAmount.add(orders.getDiscountPrice());
        }
        tempPrice = tempPrice.add(orders.getBoxPrice().add(orders.getProductPrice())
                .subtract(couponAmount).subtract(fullCutAmount).subtract(discountAmount));
        /**
         * 负责人抽取店铺金额
         */
        schoolGetshop = schoolGetshop.add(tempPrice.multiply(shop.getRate()));
        ordersComplete.setSchoolGetShop(schoolGetshop);
        /**
         * 负责人承担比例金额
         */
        if (isCoupon) {
            // 如果优惠券类型为2 --> 负责人承担所有优惠券金额
            if (coupon.getCouponType() == 2) {
                schoolUnderTakeAmount = schoolUnderTakeAmount.add(fullCutAmount.multiply(shop.getFullMinusRate()))
                        .add(couponAmount)
                        .add(discountAmount.multiply(shop.getDiscountRate()));
            } else {
                // 否则为店铺优惠券 --> 负责人承担一定比例
                schoolUnderTakeAmount = schoolUnderTakeAmount.add(fullCutAmount.multiply(shop.getFullMinusRate()))
                        .add(couponAmount.multiply(shop.getCouponRate()))
                        .add(discountAmount.multiply(shop.getDiscountRate()));
            }
        }
        /**
         * 店铺所得
         */
        shopGetTotal = shopGetTotal.add(tempPrice
                .multiply(new BigDecimal(1).subtract(shop.getRate()))
                .add(schoolUnderTakeAmount));
        ordersComplete.setShopGetTotal(shopGetTotal);
        /**
         * 负责人所得
         */
        schoolGetTotal = schoolGetTotal.add(schoolGetSender.add(schoolGetshop).subtract(appGetTotal)
                .subtract(orders.getPayFoodCoupon()).subtract(schoolUnderTakeAmount).add(downStairs));
        ordersComplete.setSchoolGetTotal(schoolGetTotal);
        // 订单Id
        ordersComplete.setOrderId(orders.getId());
        orderCompleteService.save(ordersComplete);
        senderGetTotal = ordersComplete.getSenderGetTotal();
        /**
         * 对配送员所得存储
         */
//        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SENDER_BELL,
//                new SenderAddMoneyDTO(sender.getOpenId(), senderGetTotal).toJsonString()
//        );
        /**
         * 对负责人所得存储
         */
//        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SCHOOL_BELL,
//                new SchoolAddMoneyDTO(orders.getSchoolId(), schoolGetTotal, senderGetTotal).toJsonString()
//        );
        // 增加积分
//        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WX_USER_BELL,
//                new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString()
//        );
        stringRedisTemplate.boundListOps("JR").rightPush(JSON.toJSONString(orders));
        /**
         * 将配送员所得金额添加到配送员账户内
         */

        WxUserBell wxUserBell = wxUserBellService.getById(senderUser.getOpenId() + "-" + senderUser.getPhone());
        wxUserBell.setMoney(wxUserBell.getMoney().add(ordersComplete.getSenderGetTotal()));
        Map<String, Object> map = new HashMap<>();
        map.put("amount", wxUserBell.getMoney());
        map.put("phone", wxUserBell.getPhone());
        if (wxUserBellMapper.txUpdate(map) == 0) {
            logger.error("配送员所得金额为" + senderGetTotal + "添加失败，请联系负责人");
            System.out.println("配送员所得金额添加失败，请联系负责人");
        }
        //增加用户积分
        //积分不保存小数位，向下取整
        Integer addSource = orders.getPayPrice().setScale(0, BigDecimal.ROUND_DOWN).intValue();
        Integer addUserSourceNum = wxUserBellMapper.addSourceByWxId(addSource, orderUser.getId());
        if (addUserSourceNum != NumConstants.INT_NUM_1) {
            DisplayException.throwMessageWithEnum(ResponseViewEnums.ORDER_COMPLETE_SOURCE_ERROR);
        }
        // 将负责人所得添加到负责人可提现金额内
        School updateSchool = new School();
        updateSchool.setId(school.getId());
        updateSchool.setMoney(school.getMoney().add(ordersComplete.getShopGetTotal().add(ordersComplete.getSchoolGetTotal())));
        updateSchool.setSenderMoney(school.getSenderMoney().add(senderGetTotal));
        boolean updateSchooleTrue = schoolMapper.updateById(updateSchool) != 1 ? false : true;
        stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
        if (!updateSchooleTrue) {
            logger.error("负责人所得金额为" + schoolGetSender + "配送员所得金额为" + senderGetTotal + "添加失败，请联系负责人");
            System.out.println("负责人和配送员所得金额添加失败，请联系负责人");
        }
        // 将店铺所得添加到店铺可提现金额内
        shop.setTxAmount(shop.getTxAmount().add(shopGetTotal));
        boolean rs = shopService.updateById(shop);
        if (!rs) {
            logger.error("店铺所得金额为" + shopGetTotal + "添加失败，请联系负责人");
            System.out.println("店铺所得金额添加失败，请联系负责人");
        }
        //加当日总交易额
        redisUtil.amountadd(school.getId(), orders.getPayPrice());
        return true;
    }

    @Transactional
    @Override
    public Boolean orderSettlementByOrdersNoSender(Orders orders) {
        QueryWrapper<OrdersComplete> query = new QueryWrapper<>();
        query.lambda().eq(OrdersComplete::getOrderId, orders.getId());
        // 查询订单完成表信息是否存在，存在则不可以结算
        OrdersComplete orderComplete1 = orderCompleteService.getOne(query);
        if (orderComplete1 != null) {
            Assertions.notNull(orderComplete1, ResponseViewEnums.ORDERS_COMPLETE_HAD_ERROR);
        }

        // 对订单进行校验
        Assertions.notNull(orders, ResponseViewEnums.ORDER_PARAM_ERROR);
        // 配送员
        WxUser orderUser = wxUserService.findById(orders.getOpenId());
        Assertions.notNull(orderUser);
        // 店铺
        Shop shop = shopService.getById(orders.getShopId());
        // 对店铺信息进行校验
        Assertions.notNull(shop, ResponseViewEnums.SHOP_HAD_CHANGE);
        // 学校
        School school = schoolService.findById(orders.getSchoolId());
        // 对学校信息进行校验
        Assertions.notNull(school, ResponseViewEnums.SCHOOL_HAD_CHANGE);
        // 对订单进行结算
        OrdersComplete ordersComplete = new OrdersComplete();
        // 平台抽负责人百分比
        ordersComplete.setAppGetSchoolRate(school.getRate());
        // 负责人抽成店铺百分比
        ordersComplete.setSchoolGetShopRate(shop.getRate());
        // 用户优惠券
        WxUserCoupon wxUserCoupon = new WxUserCoupon();
        Coupon coupon = new Coupon();
        // 店铺满减
        ShopFullCut shopFullCut = new ShopFullCut();
        // 优惠券金额
        BigDecimal couponAmount = BigDecimal.ZERO;
        // 店铺满减金额
        BigDecimal fullCutAmount = BigDecimal.ZERO;
        // 商品折扣金额
        BigDecimal discountAmount = BigDecimal.ZERO;
        // (餐盒费＋菜价 - 优惠券 - 满减额 - 折扣额）
        BigDecimal tempPrice = BigDecimal.ZERO;
        // 配送员所得金额
        BigDecimal senderGetTotal = BigDecimal.ZERO;
        // 店铺所得金额
        BigDecimal shopGetTotal = BigDecimal.ZERO;
        // 负责人抽取配送员金额
        BigDecimal schoolGetSender = BigDecimal.ZERO;
        // 负责人抽取店铺金额
        BigDecimal schoolGetshop = BigDecimal.ZERO;
        // 负责人所得
        BigDecimal schoolGetTotal = BigDecimal.ZERO;
        // 负责人承担比例金额之和
        BigDecimal schoolUnderTakeAmount = BigDecimal.ZERO;
        // 楼下返还金额
        BigDecimal downStairs = BigDecimal.ZERO;
        // 是否有优惠券
        Boolean isCoupon = false;
        // 负责人抽成配送员百分比
        // 如果配送员为空
        if (orders.getSenderId() == 0 || orders.getSenderId() == null) {
            ordersComplete.setSchoolGetSenderRate(BigDecimal.ZERO);
            /**
             * 配送员所得
             */
            ordersComplete.setSenderGetTotal(senderGetTotal);
            /**
             * 负责人抽取配送员金额
             */
            ordersComplete.setSchoolGetSender(schoolGetSender);
        } else {
            ordersComplete.setSchoolGetSenderRate(BigDecimal.valueOf(0));
            // 配送员所得
            // 如果时楼上送达 --> 配送费 * （1-学校抽成）
            if (orders.getDestination() == 1) {
                /**
                 * 配送员所得 0
                 */
                ordersComplete.setSenderGetTotal(BigDecimal.valueOf(0));
                /**
                 * 负责人抽取配送员所得 0
                 */
                schoolGetSender = schoolGetSender.add(BigDecimal.valueOf(0));
                ordersComplete.setSchoolGetSender(schoolGetSender);
            } else {
                // 楼下送达，要返还楼上楼下差价 --> （配送费-楼下返还） * （1-学校抽成）
                // 楼下返还金额
                downStairs = downStairs.add(orders.getSchoolTopDownPrice());
                /**
                 * 配送员所得 0
                 */
                ordersComplete.setSenderGetTotal(BigDecimal.valueOf(0));
                /**
                 * 负责人抽取配送员所得 0
                 */
                schoolGetSender = schoolGetSender.add(BigDecimal.valueOf(0));
                ordersComplete.setSchoolGetSender(schoolGetSender);
            }
        }
        // 超级后台所得对应金额 --> (原价－粮票 - 优惠券 - 满减额 - 折扣额）＊  比例
        BigDecimal appGetTotal = orders.getPayPrice().multiply(school.getRate());
        /**
         * 超级后台所得
         */
        ordersComplete.setAppGetTotal(appGetTotal);
        // 如果使用了优惠券
        if (orders.getCouponId() != 0 && orders.getCouponId() != null) {
            isCoupon = true;
            wxUserCoupon = wxUserCouponService.getById(orders.getCouponId());
            coupon = couponService.getById(wxUserCoupon.getCouponId());
            // 优惠券优惠金额
            couponAmount = couponAmount.add(new BigDecimal(coupon.getCutAmount()));
        }
        // 如果使用了店铺满减
        if (orders.getFullCutId() != null && orders.getFullCutId() != 0) {
            // 店铺满减优惠金额
            fullCutAmount = fullCutAmount.add(orders.getFullUsedAmount());
        } else if (orders.getDiscountPrice().compareTo(BigDecimal.ZERO) == 1) {
            // 商品折扣金额
            discountAmount = discountAmount.add(orders.getDiscountPrice());
        }
        tempPrice = tempPrice.add(orders.getBoxPrice().add(orders.getProductPrice())
                .subtract(couponAmount).subtract(fullCutAmount).subtract(discountAmount));
        /**
         * 负责人抽取店铺金额
         */
        schoolGetshop = schoolGetshop.add(tempPrice.multiply(shop.getRate()));
        ordersComplete.setSchoolGetShop(schoolGetshop);
        /**
         * 负责人承担比例金额
         */
        if (isCoupon) {
            // 如果优惠券类型为2 --> 负责人承担所有优惠券金额
            if (coupon.getCouponType() == 2) {
                schoolUnderTakeAmount = schoolUnderTakeAmount.add(fullCutAmount.multiply(shop.getFullMinusRate()))
                        .add(couponAmount)
                        .add(discountAmount.multiply(shop.getDiscountRate()));
            } else {
                // 否则为店铺优惠券 --> 负责人承担一定比例
                schoolUnderTakeAmount = schoolUnderTakeAmount.add(fullCutAmount.multiply(shop.getFullMinusRate()))
                        .add(couponAmount.multiply(shop.getCouponRate()))
                        .add(discountAmount.multiply(shop.getDiscountRate()));
            }
        }
        /**
         * 店铺所得
         */
        shopGetTotal = shopGetTotal.add(tempPrice
                .multiply(new BigDecimal(1).subtract(shop.getRate()))
                .add(schoolUnderTakeAmount));
        ordersComplete.setShopGetTotal(shopGetTotal);
        /**
         * 负责人所得
         */
        schoolGetTotal = schoolGetTotal.add(schoolGetSender.add(schoolGetshop).subtract(appGetTotal)
                .subtract(orders.getPayFoodCoupon()).subtract(schoolUnderTakeAmount).add(downStairs));
        ordersComplete.setSchoolGetTotal(schoolGetTotal);
        // 订单Id
        ordersComplete.setOrderId(orders.getId());
        orderCompleteService.save(ordersComplete);
        senderGetTotal = ordersComplete.getSenderGetTotal();
        /**
         * 对配送员所得存储
         */
        //stringRedisTemplate.boundListOps("JR").rightPush(JSON.toJSONString(orders));
        /**
         * 将配送员所得金额添加到配送员账户内,不需要了0
         */
        //增加用户积分
        //积分不保存小数位，向下取整
        Integer addSource = orders.getPayPrice().setScale(0, BigDecimal.ROUND_DOWN).intValue();
        Integer addUserSourceNum = wxUserBellMapper.addSourceByWxId(addSource, orderUser.getId());
        if (addUserSourceNum != NumConstants.INT_NUM_1) {
            DisplayException.throwMessageWithEnum(ResponseViewEnums.ORDER_COMPLETE_SOURCE_ERROR);
        }
        // 将负责人所得添加到负责人可提现金额内
        School updateSchool = new School();
        updateSchool.setId(school.getId());
        updateSchool.setMoney(school.getMoney().add(ordersComplete.getShopGetTotal().add(ordersComplete.getSchoolGetTotal())));
        updateSchool.setSenderMoney(school.getSenderMoney().add(senderGetTotal));
        boolean updateSchooleTrue = schoolMapper.updateById(updateSchool) != 1 ? false : true;
        stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
        if (!updateSchooleTrue) {
            logger.error("负责人所得金额为" + schoolGetSender + "配送员所得金额为" + senderGetTotal + "添加失败，请联系负责人");
            System.out.println("负责人和配送员所得金额添加失败，请联系负责人");
        }
        // 将店铺所得添加到店铺可提现金额内
        shop.setTxAmount(shop.getTxAmount().add(shopGetTotal));
        boolean rs = shopService.updateById(shop);
        if (!rs) {
            logger.error("店铺所得金额为" + shopGetTotal + "添加失败，请联系负责人");
            System.out.println("店铺所得金额添加失败，请联系负责人");
        }
        //加当日总交易额
        redisUtil.amountadd(school.getId(), orders.getPayPrice());
        return true;
    }

    /**
     * @date:   2019/8/15 15:55
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   orderId
     * @Desc:   desc 接手订单
     */
    @Override
    public ResponseObject shopAcceptOrderById2(String orderId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Orders::getId, orderId);
        Orders orders = ordersService.getOne(queryWrapper);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Orders update = new Orders();
        update.setShopId(orders.getShopId());
        update.setId(orderId);
        update.setPayTime(df.format(orders.getCreateTime()).substring(0, 10) + "%");
        School school = schoolMapper.selectById(orders.getSchoolId());
        //流水号
        Integer water = this.getWaterByRedis(orders.getShopId(),school.getYesWaterOne(),orders.getSchoolId());
        orders.setWaterNumber(water);
        update.setWaterNumber(water);
        int res = ordersService.shopAcceptOrderById(update);
        if (res != 1) {
            return new ResponseObject(false,ResponseViewEnums.FAILED);
        }
        //获取订单op
        String opOrdersString = (String) stringRedisTemplate.boundHashOps("SHOP_DJS" + orders.getShopId()).get(orderId);
        Orders opOrders = JSONObject.parseObject(opOrdersString,Orders.class);
        if (opOrders != null && opOrders.getOp().size() < NumConstants.INT_NUM_1){
            //如果缓存op是空查数据库
            QueryWrapper<OrderProduct> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id",orderId);
            List<OrderProduct> products = orderProductService.list(wrapper);
            opOrders.setOp(products);
        }
        //1-删redis
        stringRedisTemplate.boundHashOps("SHOP_DJS" + orders.getShopId()).delete(orderId);
        // 从所有待接手订单中删除
        stringRedisTemplate.boundHashOps("ALL_DJS").delete(orderId);
        // 新建所有商家已接手的订单缓存
        stringRedisTemplate.boundHashOps("SHOP_YJS").put(orderId, JSON.toJSONString(orders));
        if (orders.getTyp().equals("堂食订单") || orders.getTyp().equals("自取订单")) {
            stringRedisTemplate.opsForValue().set("tsout," + orderId, "1", 2, TimeUnit.HOURS);
            //stringRedisTemplate.opsForValue().set("tsout," + orderId, "1", 2, TimeUnit.MINUTES);
        }
        //发送微信消息
        this.wxSendOrderMsgByOrder(orders);
        //查询
        opOrders.setWaterNumber(water);
        return new ResponseObject(true,PublicErrorEnums.SUCCESS)
                .push("water",water)
                .push("order",JSON.toJSONString(opOrders));
    }

    /**
     * @date:   2019/8/23 19:57
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param
     * @Desc:   desc
     */
    private synchronized Integer getWaterByRedis(Integer shopId,Integer yesWaterOne,Integer schoolId) {
        if (yesWaterOne != null && yesWaterOne != NumConstants.INT_NUM_0){
            //学校
            Boolean haskey = stringRedisTemplate.boundHashOps("SCHOOL_WATER_NUMBER").hasKey(schoolId.toString());
            if (!haskey){
                stringRedisTemplate.boundHashOps("SCHOOL_WATER_NUMBER").put(schoolId.toString(),"0");
                Date entTime = TimeUtilS.getDayEnd();
                stringRedisTemplate.boundHashOps("SCHOOL_WATER_NUMBER").expireAt(entTime);
            }
            int water = stringRedisTemplate.boundHashOps("SCHOOL_WATER_NUMBER").increment(schoolId.toString(), 1L).intValue();
            return water;
        }
        //店铺
        Boolean haskey = stringRedisTemplate.boundHashOps("SHOP_WATER_NUMBER").hasKey(shopId.toString());
        if (!haskey){
            stringRedisTemplate.boundHashOps("SHOP_WATER_NUMBER").put(shopId.toString(),"0");
            Date entTime = TimeUtilS.getDayEnd();
            stringRedisTemplate.boundHashOps("SHOP_WATER_NUMBER").expireAt(entTime);
        }
        int water = stringRedisTemplate.boundHashOps("SHOP_WATER_NUMBER").increment(shopId.toString(), 1L).intValue();
        return water;
    }

    @Override
    public ResponseObject printAndAcceptOneOrderByOId(String orderId,Long shopId) {
        Assertions.notNull(orderId,PublicErrorEnums.PUBLIC_DATA_ERROR);
        String orderRedis = (String) stringRedisTemplate.boundHashOps(RedisConstants.SHOP_DJS+shopId).get(orderId);
        Assertions.notNull(orderRedis,PublicErrorEnums.PUBLIC_DATA_ERROR);
        Orders orders = JSONObject.parseObject(orderRedis,Orders.class);
        Assertions.notNull(orders);
        //1-先接手订单
        ResponseObject responseObject = this.shopAcceptOrderById2(orderId);
        if (!responseObject.isCode()){
            LoggerUtil.logError("打印接手订单失败-printAndAcceptOneOrderByOId-订单号-"+orderId+ResponseViewEnums.ORDER_PRINT_ACCEPT_ERROR.getErrorMessage());
            return new ResponseObject(false,ResponseViewEnums.ORDER_PRINT_ACCEPT_ERROR);
        }
        //2-打印信息
        int water = (Integer) responseObject.getParams().get("water");
        orders.setWaterNumber(water);
        String content = this.getOrderPrintContent(orders);
        List<ShopPrint> shopPrintList = shopPringService.findOneShopFeiEByShopId(shopId);
        if (shopPrintList.size() < NumConstants.INT_NUM_1){
            DisplayException.throwMessage(ResponseViewEnums.ORDER_PRINT_NO_PRINTER.getErrorMessage()+"订单号"+orderId);
        }
        ShopPrint shopPrint = shopPrintList.get(0);
        ShopPrintResultDTO<String> printResult = ShopPrintUtils.feiESendMsgAndPrint(shopPrint.getFeiESn(),content);
        if (printResult == null || !printResult.isSuccess()){
            //发送打印失败,不管了
            LoggerUtil.logError("打印接手订单失败-printAndAcceptOneOrderByOId-订单号-"+orderId+"打印-"+printResult.getMsg()+printResult.getRet());
        }
        //3-放入打印查询队列
        PrintDataDTO printDataDTO = new PrintDataDTO();
        printDataDTO.setOurOrderId(orders.getId());
        printDataDTO.setOurShopId(orders.getShopId());
        printDataDTO.setPlatePrintSn(shopPrint.getFeiESn());
        printDataDTO.setPlatePrintKey(shopPrint.getFeiEKey());
        printDataDTO.setPlatePrintOrderId(printResult.getData());
        printDataDTO.setPrintBrand(ShopPrintConfigConstants.PRINT_BRAND_DB_FEI_E);
        printDataDTO.setWaterNumber(orders.getWaterNumber());
        printDataDTO.setYesPrintTrue(NumConstants.INTEGER_NUM_1);
        orders.setStatus("商家已接手");
        printDataDTO.setRealOrder(orders);
        stringRedisTemplate.boundListOps(RedisConstants.Shop_Wait_Print_OId_List).leftPush(JSON.toJSONString(printDataDTO));
        Date entTime = TimeUtilS.getDayEnd();
        stringRedisTemplate.boundListOps(RedisConstants.Shop_Wait_Print_OId_List).expireAt(entTime);
        stringRedisTemplate.boundListOps("Shop_Test_Print_OId_List").leftPush(JSON.toJSONString(printDataDTO));
        stringRedisTemplate.boundListOps("Shop_Test_Print_OId_List").expire(1,TimeUnit.DAYS);
        return new ResponseObject(true,PublicErrorEnums.SUCCESS);
    }

    private String getOrderPrintContent(Orders orders) {
        if (orders.getOp() == null || orders.getOp().size() < 1){
            QueryWrapper<OrderProduct> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id",orders.getId());
            List<OrderProduct> orderProductList = orderProductService.list(wrapper);
            orders.setOp(orderProductList);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<CB>#" + orders.getWaterNumber() + orders.getTyp() + "</CB><BR>");
        stringBuffer.append(orders.getId() + "<BR>");
        stringBuffer.append("<B>" + orders.getShopName() + "</B><BR>");
        stringBuffer.append(TimeUtilS.dateFormat(orders.getCreateTime()) + "<BR>");
        stringBuffer.append("<C>-------------商品-------------</C><BR>");
        for (OrderProduct product : orders.getOp()) {
            stringBuffer.append("<B>" + product.getProductName() + "(" +product.getAttributeName() + ")<BR>");
            stringBuffer.append("x" + product.getProductCount() + "    " + product.getAttributePrice() + "</B><BR>");

        }
        stringBuffer.append( "<C>-------------计价-------------</C><BR>");
        stringBuffer.append("商品原价：" + "<RIGHT>" + orders.getProductPrice() + "</RIGHT><BR>");
        stringBuffer.append("餐盒费：" + "<RIGHT>" + orders.getBoxPrice() + "</RIGHT><BR>");
        stringBuffer.append("配送费：" + "<RIGHT>" + orders.getSendPrice() + "</RIGHT><BR>");
        if ("满减优惠".equals(orders.getDiscountType())){
            stringBuffer.append("满减优惠：" + "<RIGHT>" + "-"+ orders.getDiscountPrice() + "</RIGHT><BR>"
);
        }else if("商品折扣".equals(orders.getDiscountType())){
            stringBuffer.append("折扣优惠：" + "<RIGHT>" + "-"+ orders.getDiscountPrice() + "</RIGHT><BR>"
);
        }
        if (orders.getCouponId().intValue() > NumConstants.INT_NUM_0 ){
            stringBuffer.append("优惠券优惠：" + "<RIGHT>" + "-"+ orders.getCouponUsedAmount() + "</RIGHT><BR>");
        }
        if (orders.getPayFoodCoupon().compareTo(new BigDecimal(0)) > NumConstants.INT_NUM_0){
            stringBuffer.append("粮票优惠：" + "<RIGHT>" + "-"+ orders.getPayFoodCoupon() + "</RIGHT><BR>");
        }
        stringBuffer.append("<BOLD>实际支付：" + "<RIGHT>" +orders.getPayPrice() + "</RIGHT></BOLD><BR>");
        stringBuffer.append("<C>-------------用户-------------</C><BR>");
        stringBuffer.append("<B>备注：" + orders.getRemark() + "<BR>");
        stringBuffer.append("<BR>");
        stringBuffer.append(orders.getAddressDetail() + "，" + orders.getAddressPhone() + "，" + orders.getAddressName() + "</B>");
        return stringBuffer.toString();
    }

    public  Boolean wxSendOrderMsgByOrder(Orders orders) {
        Assertions.notNull(orders, ResponseViewEnums.SEND_WX_MESSAGE_ERROR_NO_PARAMS);
        Assertions.notNull(orders.getId(), ResponseViewEnums.SEND_WX_MESSAGE_ERROR_NO_PARAMS);
        List<String> formIds = new ArrayList<>();
        try {
            Long redisSize = stringRedisTemplate.opsForList().size("FORMID" + orders.getId());
            formIds = stringRedisTemplate.opsForList().range("FORMID" + orders.getId(),0,-1);
        } catch (Exception ex) {
            LoggerUtil.logError("发送微信模板消息-商家接手类-wxSendOrderMsg-完成发送消息失败，formid取缓存为空" + orders.getId());
            return false;
        }
        if (formIds.size() > 0) {
            // 查询订单商品表信息
            QueryWrapper<OrderProduct> productWrapper = new QueryWrapper<>();
            productWrapper.lambda().eq(OrderProduct::getOrderId, orders.getId());
            List<OrderProduct> orderProducts = orderProductService.list(productWrapper);
            orders.setOp(orderProducts);
            orders.setStatus("待接手");
            try{
                WxMessageUtil.wxSendMsg(orders, formIds.get(0),orders.getSchoolId());
                stringRedisTemplate.boundListOps("FORMID" + orders.getId()).remove(1, formIds.get(0));
            }catch (Exception ex){
                LoggerUtil.logError("wx发送消息失败-printAndAcceptOneOrderByOId-"+ex.getMessage());
            }
        } else {
            LoggerUtil.logError("发送微信模板消息-商家接手类-wxSendOrderMsg-完成发送消息失败，发送或者删除redis失败" + orders.getId());
           //微信发送模板消息失败算接手成功
            return false;
        }
        return true;
    }
}
