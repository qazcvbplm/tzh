package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.RichTextMapper;
import ops.school.api.entity.RichText;
import ops.school.api.service.RichTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RichTextServiceImpl extends ServiceImpl<RichTextMapper, RichText> implements RichTextService {

    @Autowired
    private RichTextMapper richTextMapper;

    @Override
    public List<RichText> findByIdAndParentId(Integer id, Integer parentId) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("parentId",parentId);
        return richTextMapper.findByIdAndParentId(map);
    }
}
