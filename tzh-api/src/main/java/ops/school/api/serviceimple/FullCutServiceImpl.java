package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.FullCutMapper;
import ops.school.api.entity.FullCut;
import ops.school.api.service.FullCutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullCutServiceImpl extends ServiceImpl<FullCutMapper, FullCut> implements FullCutService {

    @Autowired
    private FullCutMapper fullCutMapper;

    @Override
    public List<FullCut> findByShopId(Integer shopId) {
        QueryWrapper<FullCut> query = new QueryWrapper<FullCut>();
        query.lambda().eq(FullCut::getShopId, shopId);
        return fullCutMapper.selectList(query);
    }
}
