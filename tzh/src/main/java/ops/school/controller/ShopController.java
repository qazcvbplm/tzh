package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.auth.JWTUtil;
import ops.school.api.constants.RedisConstants;
import ops.school.api.dto.ShopTj;
import ops.school.api.entity.*;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.*;
import ops.school.api.util.*;
import ops.school.api.wxutil.WXUtil;
import ops.school.api.constants.NumConstants;
import ops.school.service.TCommonService;
import ops.school.service.TOrdersService;
import ops.school.service.TProductService;
import ops.school.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "店铺模块")
@RequestMapping("ops/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private FullCutService fullCutService;
    @Autowired
    private ShopOpenTimeService shopOpenTimeService;
    @Autowired
    private TOrdersService tOrdersService;
    @Autowired
    private TCommonService tCommonService;
    @Value("${barcode.path}")
    private String path;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "添加", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid Shop shop, BindingResult result) {
        Util.checkParams(result);
        shop.setSchoolId(Integer.valueOf(request.getAttribute("Id").toString()));
        shopService.add(shop);
        return new ResponseObject(true, "添加成功");
    }

    @ApiOperation(value = "查询", httpMethod = "GET")
    @RequestMapping("find")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, Shop shop) {
        shop.setIsDelete(NumConstants.DB_TABLE_IS_DELETE_NO);
        QueryWrapper<Shop> query = new QueryWrapper<Shop>().setEntity(shop);
//		IPage<Shop> iPage = shopService.page(new Page<>(shop.getPage(), shop.getSize()), query);
        Integer countNum = shopService.count(query);
        List<Shop> shopList = shopService.find(shop);
        return new ResponseObject(true, "ok")
                .push("list", shopList)
                .push("total", countNum);
    }

    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, Shop shop) {
        int i = shopService.update(shop);
        Shop shopFind = shopService.getById(shop.getId());
        //最后删除缓存
        stringRedisTemplate.boundHashOps(RedisConstants.Index_Shop_Product_Hash).delete(shopFind.getSchoolId().toString());
        return new ResponseObject(true, "更新" + i + "条记录");
    }

    @ApiOperation(value = "添加满减", httpMethod = "POST")
    @PostMapping("add_fullcut")
    public ResponseObject add_fullcut(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid FullCut fullcut, BindingResult result) {
        Util.checkParams(result);
        fullCutService.save(fullcut);
		Shop shopFind = shopService.getById(fullcut.getShopId());
		//最后删除缓存
		stringRedisTemplate.boundHashOps(RedisConstants.Index_Shop_Product_Hash).delete(shopFind.getSchoolId().toString());
        return new ResponseObject(true, "添加成功");
    }

    @ApiOperation(value = "删除满减", httpMethod = "POST")
    @PostMapping("delete_fullcut")
    public ResponseObject add_fullcut(HttpServletRequest request, HttpServletResponse response, int id) {
		FullCut fullCut = fullCutService.getById(id);
		if (fullCutService.removeById(id)) {
			Shop shopFind = shopService.getById(fullCut.getShopId());
			//最后删除缓存
			stringRedisTemplate.boundHashOps(RedisConstants.Index_Shop_Product_Hash).delete(shopFind.getSchoolId().toString());
            return new ResponseObject(true, "删除成功");
        } else {
            return new ResponseObject(false, "删除失败");
        }

    }

    @ApiOperation(value = "查询满减", httpMethod = "POST")
    @PostMapping("find_fullcut")
    public ResponseObject find_fullcut(HttpServletRequest request, HttpServletResponse response, int shopId) {
        List<FullCut> list = fullCutService.findByShopId(shopId);
        return new ResponseObject(true, "ok").push("list", list);
    }

    @ApiOperation(value = "按店铺查询营业时间", httpMethod = "POST")
    @PostMapping("find/openTime")
    public ResponseObject openTime(HttpServletRequest request, HttpServletResponse response, int shopId) {
        List<ShopOpenTime> list = shopOpenTimeService.findByShopId(shopId);
        return new ResponseObject(true, "ok").push("time", list);
    }

    @ApiOperation(value = "添加营业时间", httpMethod = "POST")
    @PostMapping("add/openTime")
    public ResponseObject openTime(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid ShopOpenTime time, BindingResult result) {
        Util.checkParams(result);
        try {
            time.setStartTimeLong(TimeUtilS.get1970YearDateLong(time.getStartTime()).getTime());
            time.setEndTimeLong(TimeUtilS.get1970YearDateLong(time.getEndTime()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            LoggerUtil.logError(e.getMessage());
            DisplayException.throwMessageWithEnum(ResponseViewEnums.SHOP_ADD_TIME_ERROR);
        }
        shopOpenTimeService.save(time);
		Shop shopFind = shopService.getById(time.getShopId());
		//最后删除缓存
		stringRedisTemplate.boundHashOps(RedisConstants.Index_Shop_Product_Hash).delete(shopFind.getSchoolId().toString());
        return new ResponseObject(true, "ok");
    }

    @ApiOperation(value = "删除营业时间", httpMethod = "POST")
    @PostMapping("delete/deleteopenTime")
    public ResponseObject deleteopenTime(HttpServletRequest request, HttpServletResponse response, int id) {
		ShopOpenTime shopOpenTime = shopOpenTimeService.getById(id);
        shopOpenTimeService.removeById(id);
		Shop shopFind = shopService.getById(shopOpenTime.getShopId());
		//最后删除缓存
		stringRedisTemplate.boundHashOps(RedisConstants.Index_Shop_Product_Hash).delete(shopFind.getSchoolId().toString());
        return new ResponseObject(true, "ok");
    }

    @ApiOperation(value = "店铺登录", httpMethod = "POST")
    @PostMapping("android/login")
    public ResponseObject android_login(HttpServletRequest request, HttpServletResponse response, String loginName, String loginPassWord) {
        Shop shop = shopService.login(loginName, loginPassWord);
        String token = JWTUtil.sign(shop.getId() + "", shop.getShopLoginName(), "android");
        return new ResponseObject(true, "ok").push("msg", Util.toJson(shop)).push("token", token);
    }

    @ApiOperation(value = "店铺营业开关", httpMethod = "POST")
    @PostMapping("android/openorclose")
    public String openorclose(HttpServletRequest request, HttpServletResponse response, Integer id) {
        return shopService.openorclose(id) + "";
    }
	
/*	@ApiOperation(value="商家临时统计",httpMethod="POST")
	@PostMapping("nocheck/shoptempstatistics")
	public ResponseObject shoptempstatistics(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer shopId,@RequestParam String beginTime,@RequestParam String endTime){
		                  SenderTj result=shopService.statistics(shopId,beginTime,endTime);
		                  return new ResponseObject(true, "ok").push("result", result);
	}*/

    @ApiOperation(value = "商家统计", httpMethod = "POST")
    @PostMapping("nocheck/shopstatistics")
    public ResponseObject shopstatistics(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam Integer shopId, @RequestParam String beginTime, @RequestParam String endTime) {
        ShopTj result = tOrdersService.shopstatistics(shopId, beginTime, endTime);
        return new ResponseObject(true, "ok").push("result", result);
    }

    @ApiOperation(value = "店铺二维码", httpMethod = "POST")
    @PostMapping("barcode")
    public ResponseObject barcode(HttpServletRequest request, HttpServletResponse response, int id) {
        Shop shop = shopService.getById(id);
        School school = schoolService.findById(shop.getSchoolId());
        String path = this.path + "/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        InputStream shopCodeIn = WXUtil.getCodeInStream(school.getWxAppId(),
                school.getWxSecret(),
                "pages/index/item/menu/menu?shopid=" + id,
                path + id + ".jpg");
        if (shopCodeIn == null){
            return new ResponseObject(false, "生成二维码失败");
        }
        if (shopCodeIn != null && shop.getShopImage() != null) {
            String shopCodePath = path + id + ".jpg";
            Map<String, Object> msg = ImageUtil.pictureCombine(shop.getShopImage(), shopCodePath, path, shop);
            if ((Integer) msg.get("code") == 1) {
                String shopCodeImage = (String) msg.get("shopCodeImage");
                shop.setShopCodeImage(shopCodeImage);
                // 修改店铺二维码图片
                boolean re = shopService.updateById(shop);
                if (re) {
                    return new ResponseObject(true, "生成二维码成功").push("barcode", shopCodeImage);
                }
            } else {
                return new ResponseObject(false, "生成二维码失败");
            }
        }
        return new ResponseObject(false, "生成二维码失败");
    }

    /**
     * @date:   2019/8/28 22:42
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   shopId
     * @param   discount
     * @Desc:   desc
     */
    @ApiOperation(value = "设置店铺是否全店铺商品统一折扣", httpMethod = "POST")
    @PostMapping("discount/all")
    public ResponseObject discountAllProduct(Integer shopId, BigDecimal discount) {
        Assertions.notNull(shopId,ResponseViewEnums.SHOP_DISCOUNT_ALL_PARAMS_NULL);
        Assertions.notNull(discount,ResponseViewEnums.SHOP_DISCOUNT_ALL_PARAMS_ID_NULL);
        ResponseObject responseObject = shopService.discountAllProductBySId(shopId,discount);
        return responseObject;
    }

    @ApiOperation(value = "查询该店铺下没删除且没下架的必选商品", httpMethod = "POST")
    @PostMapping("choose/all")
    public ResponseObject getAllNeedChooseAndNoDelPros(Product product) {
        Assertions.notNull(product,ResponseViewEnums.SHOP_NEED_TO_ID);
        Assertions.notNull(product.getShopId(),ResponseViewEnums.SHOP_NEED_TO_ID);
        ResponseObject responseObject = productService.getAllNeedChooseAndNoDelPros(product);
        return responseObject;
    }
}
