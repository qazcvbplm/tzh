package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ShopMapper;
import ops.school.api.entity.PageQueryDTO;
import ops.school.api.entity.Shop;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.YWException;
import ops.school.api.service.ShopService;
import ops.school.api.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShopServiceImple extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Autowired
    private ShopMapper shopMapper;


    @Override
    public void add(Shop shop) {
        if (shopMapper.checkByLoginName(shop.getShopLoginName()) == null) {
            shop.setSort(System.currentTimeMillis());
            this.add(shop);
        } else {
            throw new YWException("登录名重复请重新 输入");
        }
    }

    @Override
    public List<Shop> find(Shop shop) {
        return shopMapper.find(shop);
    }

    @Override
    public int update(Shop shop) {
        if (shop.getShopLoginName() != null) {
            if (shopMapper.checkByLoginName(shop.getShopLoginName()) != null) {
                throw new YWException("登录名重复请重新 输入");
            }
        }
        return this.updateById(shop) ? 1 : 0;
    }


    @Override
    public Shop login(String loginName, String enCode) {
        Shop shop = shopMapper.checkByLoginName(loginName);
        if (shop != null) {
            if (shop.getShopLoginPassWord().equals(enCode)) {
                return shop;
            } else {
                throw new YWException("密码错误");
            }
        } else {
            throw new YWException("用户名不存在");
        }
    }

   /* @Override
    public SenderTj statistics(Integer shopId, String beginTime, String endTime) {
        SenderTj rs = new SenderTj();
        rs.setTakeoutNosuccess(0);
        rs.setTakeoutSuccess(0);
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", shopId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        List<RunOrders> list = runOrdersMapper.temp(map);
        for (RunOrders temp : list) {
            if (temp.getStatus().equals("待接手")) {
                rs.setTakeoutNosuccess(rs.getTakeoutNosuccess() + temp.getFloorId());
            }
            if (temp.getStatus().equals("配送员已接手")) {
                rs.setTakeoutNosuccess(rs.getTakeoutNosuccess() + temp.getFloorId());
            }
            if (temp.getStatus().equals("已完成")) {
                rs.setTakeoutSuccess(temp.getFloorId());
            }
        }
        return rs;
    }*/


    @Override
    public int openorclose(Integer id) {
        Shop shop = shopMapper.selectByPrimaryKey(id);
        if (shop.getOpenFlag() == 1) {
            shop.setOpenFlag(0);
        } else {
            shop.setOpenFlag(1);
        }
        Shop update = new Shop();
        update.setId(id);
        update.setOpenFlag(shop.getOpenFlag());
        return update(update);
    }

    @Override
    public int shoptx(Map<String, Object> map) {
        return shopMapper.shoptx(map);
    }

    /**
     * @date:   2019/7/22 15:20
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   shop
     * @param   pageQueryDTO
     * @Desc:   desc 分页查询店铺，查询满减，根据开店时间倒叙排（关闭的店铺在最后面）
     */
    @Override
    public ResponseObject findShopWithFullCutOBTime(Shop shop, PageQueryDTO pageQueryDTO) {
        Assertions.notNull(shop,shop.getSchoolId());
        List<Shop> shopList = shopMapper.findShopWithFullCutOBTime(shop,pageQueryDTO);
        return new ResponseObject(true,"ok").push("list",shopList);
    }
}
