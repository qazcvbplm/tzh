package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.LogsMapper;
import ops.school.api.entity.Logs;
import ops.school.api.service.LogsService;
import org.springframework.stereotype.Service;

@Service
public class LogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements LogsService {
}
