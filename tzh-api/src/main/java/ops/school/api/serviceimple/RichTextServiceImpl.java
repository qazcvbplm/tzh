package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.RichTextMapper;
import ops.school.api.entity.RichText;
import ops.school.api.service.RichTextService;
import org.springframework.stereotype.Service;

@Service
public class RichTextServiceImpl extends ServiceImpl<RichTextMapper, RichText> implements RichTextService {
}
