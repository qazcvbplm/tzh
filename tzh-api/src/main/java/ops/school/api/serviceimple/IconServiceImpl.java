package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.IconMapper;
import ops.school.api.entity.Icon;
import ops.school.api.service.IconService;
import org.springframework.stereotype.Service;

@Service
public class IconServiceImpl extends ServiceImpl<IconMapper, Icon> implements IconService {
}
