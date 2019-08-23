package ops.school.api.serviceimple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.RedisConstants;
import ops.school.api.dao.*;
import ops.school.api.dto.project.IndexShopProductRedisDTO;
import ops.school.api.entity.*;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.FullCutService;
import ops.school.api.service.IndexShopProductService;
import ops.school.api.service.ShopOpenTimeService;
import ops.school.api.service.ShopService;
import ops.school.api.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/22
 * 14:57
 * #
 */
@Service("indexShopProductService")
public class IndexShopProductServiceIMPL implements IndexShopProductService {

    @Autowired
    private IndexShopProductMapper indexShopProductMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private ShopOpenTimeService shopOpenTimeService;

    @Autowired
    private FullCutService fullCutService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ShopService shopService;

    /**
     * @date:   2019/8/22 15:10
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.IndexShopProduct>
     * @param   schoolId
     * @Desc:   desc
     */
    @Override
    public ResponseObject findIndexShopProBySchoolId(Long schoolId) {
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_FIND_NO_SCHOOL);
        //1-先查缓存
        Object redisGet = stringRedisTemplate.boundHashOps(RedisConstants.Index_Shop_Product_Hash).get(schoolId.toString());
        IndexShopProductRedisDTO redisGetDTO = null;
        if (redisGet != null){
            redisGetDTO = JSONObject.parseObject((String) redisGet,IndexShopProductRedisDTO.class);
        }
        if (redisGetDTO != null){
            return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                    .push("shopList",redisGetDTO.getShopList())
                    .push("productList",redisGetDTO.getProductList());
        }
        //2-再查数据库
        List<IndexShopProduct> indexShopProducts = indexShopProductMapper.findIndexShopProBySchoolId(schoolId);
        if (indexShopProducts.size() < NumConstants.INT_NUM_1){
            return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                    .push("shopList",new ArrayList<>())
                    .push("productList",new ArrayList<>());
        }
        List<Long> shopIds = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        for (IndexShopProduct index : indexShopProducts) {
            if (index.getShopId() != null){
                shopIds.add(index.getShopId());
            }
            if (index.getProductId() != null){
                productIds.add(index.getProductId());
            }
        }
        PublicUtilS.removeDuplicate(shopIds);
        PublicUtilS.removeDuplicate(productIds);
        List<Shop> shopList = new ArrayList<>();
        List<ShopOpenTime> shopOpenTimeList = new ArrayList<>();
        List<FullCut> fullCutList = new ArrayList<>();
        //查询店铺
        if (shopIds.size() > NumConstants.INT_NUM_0){
            shopList = shopMapper.selectBatchIds(shopIds);
            //查询开店时间
            shopOpenTimeList = shopOpenTimeService.batchFindByShopIds(shopIds);
            //查满减
            fullCutList = fullCutService.batchFindByShopIds(shopIds);
        }
        //封装开业时间,满减
        if (shopList.size() > NumConstants.INT_NUM_1){
            Map<Integer,List<ShopOpenTime>> openTimeMap = PublicUtilS.listforEqualKeyListMap(shopOpenTimeList,"shopId");
            Map<Integer,List<FullCut>> fullCutMap = PublicUtilS.listforEqualKeyListMap(fullCutList,"shopId");
            for (Shop shop : shopList) {
                //封装开启店铺
                Boolean shopOpenTrue = false;
                if (openTimeMap.get(shop.getId()) != null && openTimeMap.get(shop.getId()).size() > NumConstants.INT_NUM_0){
                    try {
                        shopOpenTrue = shopService.ShopNowIsOpen(openTimeMap.get(shop.getId()),shop.getId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        shopOpenTrue = false;
                        LoggerUtil.logError(e.getMessage());
                    }
                }
                int openFlag = shopOpenTrue ? 1 : 0;
                shop.setOpenFlag(openFlag);
                //封装满减
                if (fullCutMap.get(shop.getId()) != null){
                    shop.setFullCut(fullCutMap.get(shop.getId()));
                }else {
                    shop.setFullCut(new ArrayList<>());
                }

            }
        }
        List<Product> productList = new ArrayList<>();
        if (productIds.size() > NumConstants.INT_NUM_0){
            productList = productMapper.selectBatchIds(productIds);
        }
        IndexShopProductRedisDTO redisDTO = new IndexShopProductRedisDTO();
        redisDTO.setIndexShopProductList(indexShopProducts);
        redisDTO.setProductList(productList);
        redisDTO.setShopList(shopList);
        redisDTO.setSchoolId(schoolId);
        stringRedisTemplate.boundHashOps(RedisConstants.Index_Shop_Product_Hash).put(schoolId.toString(), JSON.toJSONString(redisDTO));
        redisUtil.keyNoExpireThenSetTimeAt(RedisConstants.Index_Shop_Product_Hash,TimeUtilS.getDayEnd());
        return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                .push("shopList",shopList)
                .push("productList",productList);
    }

    /**
     * @date:   2019/8/22 16:14
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   shops
     * @param   products
     * @param   schoolId
     * @Desc:   desc
     */
    @Override
    public ResponseObject addIndexShopProduct(String shops, String products, Integer schoolId) {
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_ADD_NO_SCHOOL);
        String regex = ",+|，+|\\s+";
        String[] shopArr = shops.split(regex);
        String[] productArr = products.split(regex);
        Object[] shopArray = PublicUtilS.removeDuplicate(shopArr);
        Object[] productArray = PublicUtilS.removeDuplicate(productArr);
        int len = Math.max(shopArray.length, productArray.length);
        List<IndexShopProduct> indexShopProducts = new ArrayList<>(len);
        for (int i = 0; i < len ;i++) {
            IndexShopProduct index = new IndexShopProduct();
            index.setCreateId(Long.valueOf(schoolId));
            index.setUpdateId(Long.valueOf(schoolId));
            index.setCreateTime(new Date());
            index.setUpdateTime(new Date());
            index.setSchoolId(Long.valueOf(schoolId));
            if (i < shopArray.length && !StringUtils.isBlank(String.valueOf(shopArray[i]))){
                index.setShopId(Long.valueOf(String.valueOf(shopArray[i])));
            }
            if (i < productArray.length && !StringUtils.isBlank(String.valueOf(productArray[i]))){
                index.setProductId(Long.valueOf(String.valueOf(productArray[i])));
            }

            indexShopProducts.add(index);
        }
        if (indexShopProducts.size() < NumConstants.INT_NUM_1){
            return new ResponseObject(true,ResponseViewEnums.INDEX_ADD_ERROR_NULL);
        }
        //先删除
        QueryWrapper<IndexShopProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("school_id",schoolId);
        int deleteNum = indexShopProductMapper.delete(wrapper);
        //再新增
        int addNum = indexShopProductMapper.batchInsert(indexShopProducts);
        //最后删除缓存
        stringRedisTemplate.boundHashOps(RedisConstants.Index_Shop_Product_Hash).delete(schoolId.toString());
        return new ResponseObject(true,PublicErrorEnums.SUCCESS);
    }


    @Override
    public ResponseObject findIndexStringBySchoolId(Long schoolId) {
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_FIND_NO_SCHOOL);
        List<IndexShopProduct> indexShopProducts = indexShopProductMapper.findIndexShopProBySchoolId(schoolId);
        if (indexShopProducts.size() < NumConstants.INT_NUM_1){
            return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                    .push("shopList",new ArrayList<>())
                    .push("productList",new ArrayList<>());
        }
        StringBuffer shopIds = new StringBuffer();
        StringBuffer productIds = new StringBuffer();
        for (IndexShopProduct index : indexShopProducts) {
            if (index.getShopId() != null){
                shopIds.append(index.getShopId());
                shopIds.append(",");
            }
            if (index.getProductId() != null){
                productIds.append(index.getProductId());
                productIds.append(",");
            }
        }
        return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                .push("shopList",shopIds.toString())
                .push("productList",productIds.toString());
    }
}
