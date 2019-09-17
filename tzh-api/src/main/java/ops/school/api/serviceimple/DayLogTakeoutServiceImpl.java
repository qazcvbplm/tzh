package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.DayLogTakeoutMapper;
import ops.school.api.entity.DayLogTakeout;
import ops.school.api.service.DayLogTakeoutService;
import org.springframework.stereotype.Service;

@Service
public class DayLogTakeoutServiceImpl extends ServiceImpl<DayLogTakeoutMapper, DayLogTakeout> implements DayLogTakeoutService {
}
