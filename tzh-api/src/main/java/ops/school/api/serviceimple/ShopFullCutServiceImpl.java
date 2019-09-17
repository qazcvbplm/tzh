package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ShopFullCutMapper;
import ops.school.api.entity.ShopFullCut;
import ops.school.api.service.ShopFullCutService;
import org.springframework.stereotype.Service;

@Service
public class ShopFullCutServiceImpl extends ServiceImpl<ShopFullCutMapper, ShopFullCut> implements ShopFullCutService {
}
