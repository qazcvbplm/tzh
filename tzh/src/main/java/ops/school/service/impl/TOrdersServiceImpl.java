package ops.school.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.config.Server;
import ops.school.api.dao.OrdersMapper;
import ops.school.api.dto.ShopTj;
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
import ops.school.api.util.*;
import ops.school.api.wx.refund.RefundUtil;
import ops.school.api.wxutil.AmountUtils;
import ops.school.config.RabbitMQConfig;
import ops.school.constants.NumConstants;
import ops.school.constants.OrderConstants;
import ops.school.constants.ProductConstants;
import ops.school.message.dto.SchoolAddMoneyDTO;
import ops.school.message.dto.SenderAddMoneyDTO;
import ops.school.message.dto.WxUserAddSourceDTO;
import ops.school.service.*;
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
            if (pt.getDiscount().compareTo(new BigDecimal(1)) == -1){
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
     * @date:   2019/7/19 18:16
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   productOrderDTOS --> 商品ID productId 商品规格ID attributeId 商品数量 count
     * @param   orders 包含微信用户openid，学校id，店铺id，楼栋id，订单类型type,
     *          配送费sendPrice, 餐盒费boxPrice, 用户优惠券ID wxUserCouponId, 实付款金额payPrice
     * @Desc:   desc 用户提交订单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObject addOrder2(List<ProductOrderDTO> productOrderDTOS, @Valid Orders orders) {
//        Long startOrderTime = System.currentTimeMillis();
        //判断商品为空
        Assertions.notEmpty(productOrderDTOS,ResponseViewEnums.ORDER_DONT_HAVE_PRODUCT);
        //判断用户有
        WxUser wxUser = wxUserService.findById(orders.getOpenId());
        //根据用户的openid和phone查找用户和用户余额 todo
        // WxUser wxUser1 = wxUserService.findUserAndBellOrCache(orders.getOpenId(),orders.getWxUserPhone());
        Assertions.notNull(wxUser,ResponseViewEnums.WX_USER_NO_EXIST);
        //判断学校是否是有，并且是当前学校
        School school = schoolService.findById(wxUser.getSchoolId());
        Assertions.notNull(school,ResponseViewEnums.SCHOOL_HAD_CHANGE);
        //判断店铺有，暂时不做
        Shop shop = shopService.getById(orders.getShopId());
        Assertions.notNull(shop,ResponseViewEnums.SHOP_HAD_CHANGE);
        //楼栋判断
        Floor floor = floorService.getById(orders.getFloorId());
        Assertions.notNull(floor,ResponseViewEnums.FLOOR_SELECT_NULL);
        //判断商品有并且库存够，批量id查询
        Map pIdAndAIdMap = PublicUtilS.listForMap(productOrderDTOS,"productId","attributeId");
        //根据商品id和商品规格id批量查询商品及规格
        List<ProductAndAttributeDTO> productAndAttributeS = productService.batchFindProdAttributeByIdS(pIdAndAIdMap);
        Map proAttributeSelectMap =  PublicUtilS.listForMapValueE(productAndAttributeS,"id");
        //假如前端传3个商品，查出来两个，有一个就没有，报错
        if (productAndAttributeS.size() < productOrderDTOS.size()){
            //报错 商品信息变化
            DisplayException.throwMessageWithEnum(ResponseViewEnums.PRODUCT_HAD_CHANGE);
        }
        // 判断订单备注是否有表情内容
        String remarkOrder = CheckUtils.checkEmoji(orders.getRemark());
        if (remarkOrder != null){
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
        Map paramProductIdCountMap = PublicUtilS.listForMap(productOrderDTOS,"productId","count");
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
        for (ProductOrderDTO productOrder:productOrderDTOS) {
            /**
             * 商品校验逻辑
             */
            productAndAttributeDTOTemp = (ProductAndAttributeDTO)proAttributeSelectMap.get(productOrder.getProductId());
            product = productAndAttributeDTOTemp.getProduct();
            productAttribute = productAndAttributeDTOTemp.getProductAttribute();
            Assertions.notNull(product,ResponseViewEnums.ORDER_DONT_HAVE_PRODUCT);
            Assertions.notNull(productAttribute,ResponseViewEnums.ORDER_DONT_HAVE_PRODUCT);
            //如果 商品id 属性id 计数不能空或者计数不能0 商品不能空
            if (productOrder.getProductId() == null || productOrder.getAttributeId() == null || productOrder.getCount() == null || productOrder.getCount() == 0){
                DisplayException.throwMessageWithEnum(ResponseViewEnums.ORDER_PARAM_ERROR);
            }
//            假如前端传3个商品，查出来两个，有一个就没有，报错
            if (paramProductIdCountMap.get(product.getId()) == null){
                throwErrorNoStockYes = true;
                noStockProdctNames += product.getProductName();
                //跳出循环，这个商品就不做逻辑处理
                continue;
                //如果参数查询的product的id在参数map中（一定在），并且商品开启库存
            }else if (product.getStockFlag().intValue() == ProductConstants.PRODUCT_STOCK_FLAG_YES){
                needToDisProductStockYes = true;
                //那么比较库存够不够，不够
                if (product.getStock() < (Integer) paramProductIdCountMap.get(product.getId())){
                    throwErrorNoStockYes = true;
                    noStockProdctNames += product.getProductName();
                    //跳出循环，这个商品就不做逻辑处理
                    continue;
                }else {
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
            if (productAttribute != null && count != 0){
                originalPrice = originalPrice.add(productAttribute.getPrice().multiply(new BigDecimal(count)));
                afterDiscountPrice = afterDiscountPrice.add(productAttribute.getPrice().multiply(new BigDecimal(count)));
                productPrice = productPrice.add(productAttribute.getPrice().multiply(new BigDecimal(count)));
                if (product != null){
                    // 订单商品表
                    OrderProduct orderProduct = new OrderProduct();
                    // 如果商品折扣小于1，即商品有折扣
                    if (product.getDiscount().compareTo(new BigDecimal(1)) == -1){
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
                    if (product.getBoxPriceFlag() == 1){
                        boxcount += count;
                    }
                    // 修改商品销量 + count
                    product.setSale(product.getSale()+count);
                    boolean rs = productService.updateById(product);
                    if (!rs){
                        logger.error("商品销量更新失败，商品id为："+ product.getId());
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
        if (throwErrorNoStockYes){
            DisplayException.throwMessage(noStockProdctNames+"卖完啦！");
        }
        // 餐盒费之和
        if (orders.getTyp().equals("外卖订单") || orders.getTyp().equals("自取订单")){
            boxPrice = boxPrice.add(shop.getBoxPrice().multiply(new BigDecimal(boxcount)));
        }
        // 配送费-->按物品件数增加配送费
        if (orders.getTyp().equals("外卖订单")){
            if (shop.getSendPriceAddByCountFlag() == 1) {
                sendAddCountPrice = sendAddCountPrice.add(new BigDecimal((totalcount - 1)).multiply(shop.getSendPriceAdd()));
            }
            // 配送费-->判断配送距离增加配送费
            int distance = BaiduUtil.DistanceAll(floor.getLat() + "," + floor.getLng(), shop.getLat() + "," + shop.getLng());
            if (distance > school.getSendMaxDistance()) {
                int per = (distance / school.getSendPerOut()) + 1;
                sendAddDistancePrice = new BigDecimal(per).multiply(school.getSendPerMoney());
            }
            // 最终配送费-->基础配送费+额外距离配送费+额外件数配送费
            sendPrice = sendPrice.add(shop.getSendPrice()).add(sendAddCountPrice).add(sendAddDistancePrice);
        }
        // 如果商品折扣未使用-->店铺满减
        if (!isDiscount){
            // 查询商家所有满减规则（从最大满减额度开始）
            List<FullCut> fullCuts =  fullCutService.findByShopId(orders.getShopId());
            if (fullCuts.size() != 0){
                for (FullCut shopFullCut:fullCuts) {
                    // 当原菜价满足店铺满减条件时 （>=）todo
                    if (originalPrice.compareTo(new BigDecimal(shopFullCut.getFull())) != -1){
                        // 店铺满减之后的优惠价格 --> 如果原菜价 >= 折扣价格时
                        if (originalPrice.subtract(new BigDecimal(shopFullCut.getCut())).compareTo(BigDecimal.ZERO) != -1){
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
        // 折后价格+餐盒费-->优惠券使用时的价格
        beforeCouponPrice = beforeCouponPrice.add(afterDiscountPrice).add(boxPrice);
        // 在没有优惠券的时候，支付价格为折后价格
        payPrice = beforeCouponPrice;
        //优惠券id是wx的wxCouponId
        WxUserCoupon wxUserCoupon = null;
        if (orders.getCouponId() != null && orders.getCouponId() != 0){
             wxUserCoupon = wxUserCouponService.getById(orders.getCouponId());
             //判断优惠券类型是
            Long currentTime = System.currentTimeMillis();
            // 用户优惠券失效 >= 当前时间
            if (wxUserCoupon != null && wxUserCoupon.getIsInvalid() == 0 && wxUserCoupon.getFailureTime().getTime() >= currentTime){
                //注释
                Coupon coupon = couponService.getById(wxUserCoupon.getCouponId());
                if (coupon != null){
                    // 折后价格+餐盒费 >= 优惠券满减使用条件
                    if (beforeCouponPrice.compareTo(new BigDecimal(coupon.getFullAmount())) != -1){
                        //  并且 折后价格+餐盒费 >= 优惠券满减金额时
                        if (beforeCouponPrice.subtract(new BigDecimal(coupon.getCutAmount())).compareTo(BigDecimal.ZERO) != -1){
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
                        } catch (Exception e){
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
        query.lambda().eq(WxUserBell::getPhone,wxUser.getOpenId()+"-"+wxUser.getPhone());
        WxUserBell wxUserBell = wxUserBellService.getOne(query);
        if (wxUserBell != null){
            if (wxUserBell.getFoodCoupon().compareTo(BigDecimal.ZERO) == 1){
                // 折后价格-优惠券+餐盒费+配送费 >= 粮票
                if (payPrice.subtract(wxUserBell.getFoodCoupon()).compareTo(BigDecimal.ZERO) == -1){
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
        OrderTempDTO tempDTO = new OrderTempDTO(orders.getSendPrice(),orders.getBoxPrice(),orders.getPayPrice(),orders.getProductPrice(),
                orders.getDiscountPrice(),orders.getPayFoodCoupon());
        // 后端计算的数据对象
        OrderTempDTO orderTempDTO = new OrderTempDTO(sendPrice,boxPrice,payPrice.setScale(2,BigDecimal.ROUND_HALF_UP),
                productPrice,discountPrice.setScale(2,BigDecimal.ROUND_HALF_UP),payFoodCoupon.setScale(2,BigDecimal.ROUND_HALF_UP));
        if (!tempDTO.equals(orderTempDTO)){
            return new ResponseObject(false,"订单金额有问题，请负责人进行核实!");
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
        /**
         * 支付金额计算逻辑
         */

        //保存订单逻辑
        boolean saveOrderSuccess = ordersService.save(ordersSaveTemp);
        if (!saveOrderSuccess){
            DisplayException.throwMessageWithEnum(ResponseViewEnums.ORDER_SAVE_ERROR);
        }
        // 修改用户粮票余额
        boolean rs = wxUserBellService.updateById(wxUserBell);
        if (!rs){
            logger.error("修改用户粮票金额失败，用户手机号为:"+wxUser.getPhone());
        }
        //保存订单商品逻辑，不行就要自己写接口
        boolean saveOPSuccess = orderProductService.saveBatch(orderProductSaveList);
        if(!saveOPSuccess){
            logger.error("订单商品保存失败，商品信息：" + PublicUtilS.getCollectionToString(orderProductSaveList));
        }
        //下单完成后扣库存
        if (needToDisProductStockYes){
            boolean disProductStockSuccess = productService.saveOrUpdateBatch(productDisStockList);
            if (!disProductStockSuccess){
                // 这里想的扣库存失败还是可以下单
                logger.error("商品扣库存失败，商品信息："+PublicUtilS.getCollectionToString(productDisStockList));
            }
        }
        //下单后领优惠券 新页面新接口
        //用户优惠券失效逻辑,如果用户的优惠券是生效的IsInvalid=0
        if (wxUserCoupon != null){
            // 优惠券
            Coupon coupon = couponService.getById(wxUserCoupon.getCouponId());
            if (coupon != null && coupon.getIsInvalid() != 1){
                wxUserCoupon.setIsInvalid(NumConstants.DB_TABLE_IS_INVALID_NO);
                int updateUserCouponNum = tWxUserCouponService.updateIsInvalid(wxUserCoupon);
                if (updateUserCouponNum != NumConstants.INT_NUM_1){
                    logger.error("修改优惠券失效失败，用户优惠券id"+orders.getCouponId());
                }
            }
        }
        Map resultMap = new HashMap();
        resultMap.put("orderId",generatorOrderId);
//        Long endOrderTime = System.currentTimeMillis();
//        System.out.println(endOrderTime - startOrderTime);
        return new ResponseObject(true,"创建订单成功！",resultMap);
    }

    @Transactional
    @Override
    public int pay(Orders orders,String formid) {
        Shop shop = shopService.getById(orders.getShopId());
        School school = schoolService.findById(shop.getSchoolId());
        Application application = applicationService.getById(school.getAppId());
        if (application.getVipTakeoutDiscountFlag() == 1) {
            orders.setPayPrice((orders.getPayPrice().multiply(application.getVipTakeoutDiscount())).setScale(2,
                    BigDecimal.ROUND_HALF_DOWN));
        }
        // 余额支付，用户数据更新
        Map<String, Object> map = new HashMap<>();
        WxUser user = wxUserService.findById(orders.getOpenId());
        map.put("phone", user.getOpenId() + "-" + user.getPhone());
        map.put("amount", orders.getPayPrice());
        if (wxUserBellService.pay(map) == 1) {
            if (paySuccess(orders.getId(), "余额支付") == 0) {
                throw new YWException("订单状态异常");
            }
            WxUser wxUser = wxUserService.findById(orders.getOpenId());
            String[] formIds = formid.split(",");
            stringRedisTemplate.boundHashOps("FORMID" + orders.getId()).put(orders.getId(),JSON.toJSONString(formIds));
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
            stringRedisTemplate.boundHashOps("SHOP_DJS" + orders.getShopId()).put(orderId, JSON.toJSONString(orders));
            stringRedisTemplate.boundHashOps("ALL_DJS").put(orderId, JSON.toJSONString(orders));
            stringRedisTemplate.convertAndSend(Server.SOCKET, ordersStr);
            //stringRedisTemplate.convertAndSend(Server.SOCKET, ordersStr);
        }
        return rs;
    }

    @Transactional
    @Override
    public int cancel(String id) {
        Orders orders = ordersService.findById(id);
        WxUser user = wxUserService.findById(orders.getOpenId());
        School school = schoolService.findById(user.getSchoolId());
        if (!orders.getStatus().equals("待付款")) {
            if (System.currentTimeMillis() - orders.getPayTimeLong() < 5 * 60 * 1000) {
                throw new YWException("需要至少5分钟才能退款");
            }
        }
        String temp = orders.getStatus();
            if (ordersService.cancel(id) == 1) {
                // 当订单内粮票额度不等于0时
                if (orders.getPayFoodCoupon().compareTo(new BigDecimal("0")) != 0) {
                    // 订单消费的粮票要退回用户粮票内
                    wxUserBellService.addFoodCoupon(user.getOpenId() + "-" + user.getPhone(), orders.getPayFoodCoupon());
                    // 取消订单时，将粮票消费的金额退回学校剩余粮票内
                    school.setUserChargeSend(school.getUserChargeSend().add(orders.getPayFoodCoupon()));
                    schoolService.updateById(school);
                }
                // 如果订单使用优惠券 --> 退还优惠券
                if (orders.getCouponId() != null && orders.getCouponId() != 0){
                    // 用户优惠券
                    WxUserCoupon wxUserCoupon = wxUserCouponService.getById(orders.getCouponId());
                    if (wxUserCoupon != null){
                        // 修改状态为0
                        wxUserCoupon.setIsInvalid(NumConstants.DB_TABLE_IS_INVALID_NOT_USED);
                        int updateUserCouponNum = tWxUserCouponService.updateIsInvalid(wxUserCoupon);
                        if (updateUserCouponNum != NumConstants.INT_NUM_1){
                            logger.error("修改优惠券失效失败，用户优惠券id"+orders.getCouponId());
                        }
                    }
                }
                if (!temp.equals("待付款")) {
                    if (orders.getPayment().equals("余额支付")) {
                        // 订单消费的余额要退回用户余额内
                        Map<String, Object> map = new HashMap<>();
                        map.put("phone", user.getOpenId() + "-" + user.getPhone());
                        map.put("amount", orders.getPayPrice().subtract(orders.getPayFoodCoupon()));
                        // 取消订单时,将余额支付时的订单金额退回学校余额内
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("schoolId", user.getSchoolId());
                        map1.put("charge", orders.getPayPrice().subtract(orders.getPayFoodCoupon()));
                        schoolService.charge(map1);
                        if (wxUserBellService.charge(map) == 1) {
                            return orders.getShopId();
                        } else {
                            throw new YWException("退款失败联系管理员");
                        }
                    } else if (orders.getPayment().equals("微信支付")) {
                        String fee = AmountUtils.changeY2F(orders.getPayPrice().subtract(orders.getPayFoodCoupon()).toString());
                        int result = RefundUtil.wechatRefund1(school.getWxAppId(), school.getWxSecret(), school.getMchId(),
                                school.getWxPayId(), school.getCertPath(), orders.getId(), fee, fee);
                        if (result != 1) {
                            throw new YWException("退款失败联系管理员");
                        } else {
                            return orders.getShopId();
                        }
                    }
                } else {
                    return orders.getShopId();
                }
            }
        return 0;
    }

    @Transactional
    @Override
    public int shopAcceptOrderById(String orderId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Orders::getId,orderId);
        Orders orders = ordersService.getOne(queryWrapper);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Orders update = new Orders();
            update.setShopId(orders.getShopId());
            update.setId(orderId);
            update.setPayTime(df.format(orders.getCreateTime()).substring(0, 10) + "%");
        int water = stringRedisTemplate.boundHashOps("SHOP_WATER_NUMBER").increment(orders.getShopId().toString(), 1L).intValue();
        orders.setWaterNumber(water + 1);
        update.setWaterNumber(water + 1);
            int res = ordersService.shopAcceptOrderById(update);
            if (res == 1) {
                if (orders.getTyp().equals("堂食订单") || orders.getTyp().equals("自取订单")) {
                    stringRedisTemplate.opsForValue().set("tsout," + orderId, "1", 2, TimeUnit.HOURS);
                }
                rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_MIN_PROGRAM_MESSAGE, JSON.toJSONString(orders));
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
        queryWrapperFloor.eq("Id",buildId);
        Floor floor = floorService.getOne(queryWrapperFloor);
        Assertions.notNull(floor, ResponseViewEnums.FLOOR_SELECT_NULL);
        //外卖订单
        QueryWrapper<Orders> queryWrapperTakeOut = new QueryWrapper<>();
        queryWrapperTakeOut
                .eq("floor_id",buildId)
                .eq("typ", OrderConstants.orderTypeTakeOut)
                .ge("create_time",beginTime)
                .le("create_time",endTime);
        Integer allOrdersTakeOut = ordersService.count(queryWrapperTakeOut);
        //堂食订单
        QueryWrapper<Orders> queryWrapperEatHere = new QueryWrapper<>();
        queryWrapperEatHere
                .eq("floor_id",buildId)
                .eq("typ", OrderConstants.queryWrapperEatHere)
                .ge("create_time",beginTime)
                .le("create_time",endTime);
        Integer allOrdersEatHere = ordersService.count(queryWrapperEatHere);
        //跑腿订单
        QueryWrapper<RunOrders> queryWrapperRunning = new QueryWrapper<>();
        queryWrapperRunning
                .eq("floor_id",buildId)
                .eq("typ", OrderConstants.orderTypeRunning)
                .between("create_time",beginTime,endTime);
        Integer allOrdersRunning = runOrdersService.count(queryWrapperRunning);
        //自取订单
        QueryWrapper<Orders> queryWrapperGetSelf = new QueryWrapper<>();
        queryWrapperGetSelf
                .eq("floor_id",buildId)
                .eq("typ", OrderConstants.queryWrapperGetSelf)
                .between("create_time",beginTime,endTime);
        Integer allOrdersGetSelf = ordersService.count(queryWrapperGetSelf);
        //订单总数
        Integer allOrders = allOrdersTakeOut + allOrdersEatHere + allOrdersRunning + allOrdersGetSelf;
        //营业额
        // 商品订单总营业额
        Map<String,Object> map = new HashMap<>();
        map.put("floorId",buildId);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        BigDecimal ordersCountPayPrice = BigDecimal.ZERO;
        if (ordersMapper.countPayPriceByFloor(map) != null){
            ordersCountPayPrice = ordersMapper.countPayPriceByFloor(map);
        }
        // 跑腿订单总营业额
        BigDecimal runOrdersCountTotalPrice = BigDecimal.ZERO;

        if (tRunOrdersService.countTotalPriceByFloor(buildId,beginTime,endTime) != null){
            runOrdersCountTotalPrice = tRunOrdersService.countTotalPriceByFloor(buildId,beginTime,endTime);
        }
        BigDecimal ordersAllMoney = ordersCountPayPrice.add(runOrdersCountTotalPrice);
        Map result = new HashMap();
        result.put("allOrders",allOrders);
        result.put("allOrdersTakeOut",allOrdersTakeOut);
        result.put("allOrdersEatHere",allOrdersEatHere);
        result.put("allOrdersRunning",allOrdersRunning);
        result.put("allOrdersGetSelf",allOrdersGetSelf);
        result.put("ordersAllMoney",ordersAllMoney);
        return result;
    }

    @Transactional
    @Override
    public int orderSettlement(String orderId) {
        QueryWrapper<OrdersComplete> query = new QueryWrapper<>();
        query.lambda().eq(OrdersComplete::getOrderId,orderId);
        // 查询订单完成表信息是否存在，存在则不可以结算
        OrdersComplete orderComplete1 = orderCompleteService.getOne(query);
        if (orderComplete1 != null){
            Assertions.notNull(orderComplete1,ResponseViewEnums.ORDERSCOMPLETE_HAD_ERROR);
        }
        // 订单信息
        Orders orders = ordersService.findById(orderId);
        // 对订单进行校验
        Assertions.notNull(orders,ResponseViewEnums.ORDER_PARAM_ERROR);
        // 配送员
        Sender sender = senderService.findById(orders.getSenderId());
        // 对配送员信息进行校验
        Assertions.notNull(sender, ResponseViewEnums.SCHOOL_HAD_CHANGE);
        // 店铺
        Shop shop = shopService.getById(orders.getShopId());
        // 对店铺信息进行校验
        Assertions.notNull(shop,ResponseViewEnums.SHOP_HAD_CHANGE);
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
        if (orders.getSenderId() == 0 || orders.getSenderId() == null){
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
        if (orders.getCouponId() != 0 && orders.getCouponId() != null){
            isCoupon = true;
            wxUserCoupon = wxUserCouponService.getById(orders.getCouponId());
            coupon = couponService.getById(wxUserCoupon.getCouponId());
            // 优惠券优惠金额
            couponAmount = couponAmount.add(new BigDecimal(coupon.getCutAmount()));
        }
        // 如果使用了店铺满减
        if (orders.getFullCutId() != null && orders.getFullCutId() != 0){
            shopFullCut = shopFullCutService.getById(orders.getFullCutId());
            // 店铺满减优惠金额
            fullCutAmount = fullCutAmount.add(new BigDecimal(shopFullCut.getCutAmount()));
        } else if (orders.getDiscountPrice().compareTo(BigDecimal.ZERO) == 1){
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
        if (isCoupon){
            // 如果优惠券类型为2 --> 负责人承担所有优惠券金额
            if (coupon.getCouponType() == 2){
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
        ordersComplete.setOrderId(orderId);
        orderCompleteService.save(ordersComplete);
        /**
         * 对配送员所得存储
         */
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SENDER_BELL,
                new SenderAddMoneyDTO(sender.getOpenId(), senderGetTotal).toJsonString()
        );
        /**
         * 对负责人所得存储
         */
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SCHOOL_BELL,
                new SchoolAddMoneyDTO(orders.getSchoolId(), schoolGetTotal, senderGetTotal).toJsonString()
        );
        // 增加积分
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WX_USER_BELL,
                new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString()
        );
        stringRedisTemplate.boundListOps("JR").rightPush(JSON.toJSONString(orders));
//        /**
//         * 将配送员所得金额添加到配送员账户内
//         */
//        WxUser wxUser = wxUserService.findById(sender.getOpenId());
//        WxUserBell wxUserBell = wxUserBellService.getById(wxUser.getOpenId()+"-"+wxUser.getPhone());
//        wxUserBell.setMoney(wxUserBell.getMoney().add(senderGetTotal));
//        Map<String,Object> map = new HashMap<>();
//        map.put("amount",wxUserBell.getMoney().add(senderGetTotal));
//        map.put("phone",wxUserBell.getPhone());
//        if (wxUserBellMapper.txUpdate(map) == 0){
//            logger.error("配送员所得金额为"+senderGetTotal+"添加失败，请联系负责人");
//            System.out.println("配送员所得金额添加失败，请联系负责人");
//        }
//        // 将负责人所得添加到负责人可提现金额内
//        school.setMoney(school.getMoney().add(senderGetTotal));
//        school.setSenderMoney(school.getSenderMoney().add(senderGetTotal));
//        if (schoolMapper.updateByPrimaryKeySelective(school) == 0){
//            logger.error("负责人所得金额为"+schoolGetSender+"配送员所得金额为"+senderGetTotal+"添加失败，请联系负责人");
//            System.out.println("负责人和配送员所得金额添加失败，请联系负责人");
//        }
//        // 将店铺所得添加到店铺可提现金额内
//        shop.setTxAmount(shop.getTxAmount().add(shopGetTotal));
//        boolean rs = shopService.updateById(shop);
//        if (!rs){
//            logger.error("店铺所得金额为"+shopGetTotal+"添加失败，请联系负责人");
//            System.out.println("店铺所得金额添加失败，请联系负责人");
//        }
        return 0;
    }

}
