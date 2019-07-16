package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ChargeLogMapper;
import ops.school.api.entity.ChargeLog;
import ops.school.api.service.ChargeLogService;
import org.springframework.stereotype.Service;

@Service
public class ChargeLogServiceImpl extends ServiceImpl<ChargeLogMapper, ChargeLog> implements ChargeLogService {
}
