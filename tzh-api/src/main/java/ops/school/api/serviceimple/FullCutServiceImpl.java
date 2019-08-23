package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.FullCutMapper;
import ops.school.api.entity.FullCut;
import ops.school.api.service.FullCutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FullCutServiceImpl extends ServiceImpl<FullCutMapper, FullCut> implements FullCutService {

    @Autowired
    private FullCutMapper fullCutMapper;

    @Override
    public List<FullCut> findByShopId(Integer shopId) {
        QueryWrapper<FullCut> query = new QueryWrapper<FullCut>();
        query.lambda().eq(FullCut::getShopId, shopId);
        query.orderByDesc("full");
        return fullCutMapper.selectList(query);
    }

    /**
     * @date:   2019/8/23 11:42
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.FullCut>
     * @param   shopIds
     * @Desc:   desc
     */
    @Override
    public List<FullCut> batchFindByShopIds(List<Long> shopIds) {
        if (shopIds.size() < NumConstants.INT_NUM_1){
            return new ArrayList<>();
        }
        List<FullCut> fullCuts = fullCutMapper.batchFindByShopIds(shopIds);
        return fullCuts;
    }
}
