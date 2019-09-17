package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.RichText;

import java.util.List;

public interface RichTextService extends IService<RichText> {

    List<RichText> findByIdAndParentId(Integer id, Integer parentId);
}
