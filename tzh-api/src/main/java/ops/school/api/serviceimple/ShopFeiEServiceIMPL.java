package ops.school.api.serviceimple;

import java.util.List;
import java.util.Map;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.dao.ShopFeiEMapper;
import ops.school.api.dto.ShopFeiEDTO;
import ops.school.api.entity.ShopFeiE;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.ShopFeiEService;
import ops.school.api.util.ResponseObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Service;


/**
 * @author: QinDaoFang
 * @date:   2019/8/11 18:20 
 * @desc:   
 */
@Service
public class ShopFeiEServiceIMPL implements ShopFeiEService {
    
    @Resource(name="shopFeiEMapper")
    private ShopFeiEMapper shopFeiEMapper;


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   saveDTO
     * @Desc:   desc 通过DTO新增
     */
    @Override
    public ResponseObject saveOneShopFeiEByDTO(@Valid ShopFeiEDTO saveDTO) {
        Assertions.notNull(saveDTO,saveDTO.getShopId());
        Assertions.notNull(saveDTO.getCreateId(),"创建人不能为空");
        Assertions.notNull(saveDTO, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //设置日期格式
        saveDTO.setCreateTime(new Date());
        saveDTO.setUpdateTime(new Date());
        saveDTO.setUpdateId(saveDTO.getCreateId());
        ShopFeiE shopFeiE = new ShopFeiE();
        BeanUtils.copyProperties(saveDTO,shopFeiE);
        shopFeiEMapper.insert(shopFeiE);
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
    public ResponseObject updateOneShopFeiEByDTO(ShopFeiEDTO updateDTO) {
        Assertions.notNull(updateDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(updateDTO.getId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //设置日期格式
        updateDTO.setCreateTime(new Date());
        updateDTO.setUpdateTime(new Date());
        ShopFeiE updateEntity = new ShopFeiE();
        BeanUtils.copyProperties(updateDTO,updateEntity);
        QueryWrapper<ShopFeiE> wrapper = new QueryWrapper<>();
        wrapper.eq("id",updateEntity.getId());
        int updateNum = shopFeiEMapper.update(updateEntity,wrapper);
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
        int deleteNum = shopFeiEMapper.deleteById(id);
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
    public ShopFeiE findOneShopFeiEById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ShopFeiE findVO = shopFeiEMapper.selectById(id);
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
    public List<ShopFeiE> batchFindShopFeiEByIds(List<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PULBIC_EMPTY_PARAM);
        }
        List<ShopFeiE> batchFindVOSByIds = shopFeiEMapper.batchFindByIds(ids);
        return batchFindVOSByIds;
    }


}
