package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ChargeMapper;
import ops.school.api.entity.Charge;
import ops.school.api.service.ChargeService;
import org.springframework.stereotype.Service;

@Service
public class ChargeServiceImpl extends ServiceImpl<ChargeMapper, Charge> implements ChargeService {
}
