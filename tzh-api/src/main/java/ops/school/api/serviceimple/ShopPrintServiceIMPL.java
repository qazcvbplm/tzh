package ops.school.api.serviceimple;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.dao.ShopMapper;
import ops.school.api.dao.ShopPrintMapper;
import ops.school.api.dto.ShopPrintDTO;
import ops.school.api.dto.print.FeiERsultData;
import ops.school.api.dto.print.ShopPrintResultDTO;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.OrderProduct;
import ops.school.api.entity.Orders;
import ops.school.api.entity.Shop;
import ops.school.api.entity.ShopPrint;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.OrderProductService;
import ops.school.api.service.OrdersService;
import ops.school.api.service.ShopPrintService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.ShopPrintUtils;
import ops.school.api.util.TimeUtilS;
import ops.school.api.wxutil.WxMessageUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


/**
 * @author: QinDaoFang
 * @date:   2019/8/11 18:20 
 * @desc:   
 */
@Service(value = "shopPrintService")
@Transactional(rollbackFor = {Exception.class,DisplayException.class})
public class ShopPrintServiceIMPL implements ShopPrintService {
    
    @Autowired
    private ShopPrintMapper shopPrintMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderProductService orderProductService;



    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   saveDTO
     * @Desc:   desc 通过DTO新增
     */
    @Override
    public ResponseObject saveOneShopFeiEByDTO(@Valid ShopPrintDTO saveDTO) {
        Assertions.notNull(saveDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getPrintBrand(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getCreateId(),ResponseViewEnums.CREATE_ID_CANT_NULL);
        Assertions.notNull(saveDTO.getFeiESn(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getFeiEKey(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getShopId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        if (saveDTO.getId() != null && saveDTO.getId() > NumConstants.INT_NUM_0){
            return new ResponseObject(false,ResponseViewEnums.SHOP_PRINT_ERROR_OPTIONS);
        }
        Shop shop = shopMapper.selectById(saveDTO.getShopId());
        Assertions.notNull(shop,ResponseViewEnums.SHOP_HAD_CHANGE);
        //添加时一个店铺只对应一个一个 打印机
        QueryWrapper<ShopPrint> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id",saveDTO.getShopId());
        wrapper.eq("print_brand",saveDTO.getPrintBrand());
        List<ShopPrint> shopPrints = shopPrintMapper.selectList(wrapper);
        if (shopPrints != null && shopPrints.size() > 0){
            DisplayException.throwMessageWithEnum(ResponseViewEnums.SHOP_ONE_HAVE_ONE_TYPE_PRINT);
        }
        //设置日期格式
        saveDTO.setCreateTime(new Date());
        saveDTO.setUpdateTime(new Date());
        saveDTO.setUpdateId(saveDTO.getCreateId());
        ShopPrint shopPrint = new ShopPrint();
        BeanUtils.copyProperties(saveDTO, shopPrint);
        shopPrint.setId(null);
        int addNum = shopPrintMapper.insert(shopPrint);
        if (addNum != NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(ResponseViewEnums.FAILED);
        }
        // 添加到第三方打印机管理
        // 如果是飞鹅的打印
        saveDTO.setShopName(shop.getShopName());
        ShopPrintResultDTO addFeiE = this.addThirdPlatformPrinter(saveDTO);
        if (!addFeiE.isSuccess()){
            LoggerUtil.logError("系统记录-添加飞鹅打印机失败-saveOneShopFeiEByDTO-日志"+addFeiE.getErrorMessage());
            DisplayException.throwMessage(ResponseViewEnums.SHOP_ADD_FEI_FAILED.getErrorMessage()+addFeiE.getErrorMessage());
        }
        return new ResponseObject(true, ResponseViewEnums.SUCCESS);
    }


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   updateDTO
     * @Desc:   desc 通过DTO更新
     */
    @Override
    public ResponseObject updateOneShopFeiEByDTO(ShopPrintDTO updateDTO) {
        Assertions.notNull(updateDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //编辑
        if (updateDTO.getId() != null && updateDTO.getId() > NumConstants.INT_NUM_0 ){
            Assertions.notNull(updateDTO.getId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
            //1-先查是不是有记录了
            ShopPrint shopPrint = shopPrintMapper.selectById(updateDTO.getId());
            //如果传了id查不出来，新增
            if (shopPrint == null && shopPrint.getId() == null){
                ResponseObject view = this.saveOneShopFeiEByDTO(updateDTO);
                return view;
            }
            //2-查店铺有没有
            Shop shop = shopMapper.selectById(shopPrint.getShopId());
            Assertions.notNull(shop,ResponseViewEnums.SHOP_NOT_EXISTS);
            //设置日期格式

            updateDTO.setUpdateTime(new Date());
            ShopPrint updateEntity = new ShopPrint();
            BeanUtils.copyProperties(updateDTO,updateEntity);
            //shopid不能更新
            updateEntity.setShopId(null);
            QueryWrapper<ShopPrint> wrapper = new QueryWrapper<>();
            wrapper.eq("id",updateEntity.getId());
            int updateNum = shopPrintMapper.update(updateEntity,wrapper);
            if (updateNum < 1){
                DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
            }
            //判断是否更新机器码
            boolean addPrintTrue = shopPrint.getFeiESn().equals(updateDTO.getFeiESn());
            if (addPrintTrue){
                return new ResponseObject(true, ResponseViewEnums.SUCCESS);
            }
            // 添加到第三方打印机管理
            // 如果是飞鹅的打印
            updateDTO.setShopName(shop.getShopName());
            ShopPrintResultDTO addFeiE = this.addThirdPlatformPrinter(updateDTO);
            if (!addFeiE.isSuccess()){
                LoggerUtil.logError("系统记录-更新飞鹅打印机失败-updateOneShopFeiEByDTO-日志"+addFeiE.getErrorMessage());
                DisplayException.throwMessage(ResponseViewEnums.SHOP_ADD_FEI_FAILED.getErrorMessage()+addFeiE.getErrorMessage());
            }
            return new ResponseObject(true, ResponseViewEnums.SUCCESS);
        }
        //新增
        return this.saveOneShopFeiEByDTO(updateDTO);
    }

    private ShopPrintResultDTO addThirdPlatformPrinter(ShopPrintDTO dto) {
        //逻辑判断在前面判断好
        ShopPrintResultDTO result = null;
        if (dto.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FEI_E){
            //sn key "sn1#key1#remark1#carnum1\nsn2#key2#remark2#carnum2";
            String snList = ""+dto.getFeiESn() + "#" + dto.getFeiEKey() + "#" + dto.getShopName()+"飞鹅打印机";
            result = ShopPrintUtils.feiEAddPrinter(snList);
        }
        return result;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id删除
     */
    @Override
    public ResponseObject deleteOneShopFeiEById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        int deleteNum = shopPrintMapper.deleteById(id);
        if (deleteNum < 1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true, ResponseViewEnums.SUCCESS);
    }
    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: ShopFeiE
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    @Override
    public ShopPrint findOneShopFeiEById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ShopPrint findVO = shopPrintMapper.selectById(id);
        return findVO;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<ShopFeiE>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    @Override
    public List<ShopPrint> batchFindShopFeiEByIds(List<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PULBIC_EMPTY_PARAM);
        }
        List<ShopPrint> batchFindVOSByIds = shopPrintMapper.batchFindByIds(ids);
        return batchFindVOSByIds;
    }

    /**
     * @date:   2019/8/11 20:57
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.ShopPrint
     * @param   shopId
     * @Desc:   desc
     */
    @Override
    public List<ShopPrint> findOneShopFeiEByShopId(Long shopId) {
        Assertions.notNull(shopId,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        QueryWrapper<ShopPrint> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id",shopId);
        List<ShopPrint> shopPrints = shopPrintMapper.selectList(wrapper);
        return shopPrints;
    }

}
