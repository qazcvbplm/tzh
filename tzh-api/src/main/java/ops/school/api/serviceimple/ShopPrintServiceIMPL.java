package ops.school.api.serviceimple;

import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.dao.ShopMapper;
import ops.school.api.dao.ShopPrintMapper;
import ops.school.api.dto.ShopPrintDTO;
import ops.school.api.dto.print.ShopPrintResultDTO;
import ops.school.api.entity.Shop;
import ops.school.api.entity.ShopPrint;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.ShopPrintService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.ShopPrintUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author: QinDaoFang
 * @date:   2019/8/11 18:20 
 * @desc:   
 */
@Service(value = "shopPrintService")
public class ShopPrintServiceIMPL implements ShopPrintService {
    
    @Autowired
    private ShopPrintMapper shopPrintMapper;

    @Autowired
    private ShopMapper shopMapper;


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
        int addNum = shopPrintMapper.insert(shopPrint);
        if (addNum != NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(ResponseViewEnums.FAILED);
        }
        // 添加到第三方打印机管理
        // 如果是飞鹅的打印
        if (saveDTO.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FEI_E){
            //sn key "sn1#key1#remark1#carnum1\nsn2#key2#remark2#carnum2";
            String snList = ""+saveDTO.getFeiESn() + "#" + saveDTO.getFeiEKey() + "#" + shop.getShopName()+"飞鹅打印机";
            ShopPrintResultDTO addFeiE = ShopPrintUtils.feiEAddPrinter(snList);
            if (!addFeiE.isSuccess()){
                LoggerUtil.logError("系统记录-添加飞鹅打印机失败-saveOneShopFeiEByDTO-日志"+addFeiE.getErrorMessage());
                DisplayException.throwMessageWithEnum(ResponseViewEnums.SHOP_ADD_FEI_FAILED);
            }
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
        Assertions.notNull(updateDTO.getId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //设置日期格式
        updateDTO.setCreateTime(new Date());
        updateDTO.setUpdateTime(new Date());
        ShopPrint updateEntity = new ShopPrint();
        BeanUtils.copyProperties(updateDTO,updateEntity);
        QueryWrapper<ShopPrint> wrapper = new QueryWrapper<>();
        wrapper.eq("id",updateEntity.getId());
        int updateNum = shopPrintMapper.update(updateEntity,wrapper);
        if (updateNum < 1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true, ResponseViewEnums.SUCCESS);
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
