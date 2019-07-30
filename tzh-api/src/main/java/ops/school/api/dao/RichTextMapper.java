package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import ops.school.api.entity.RichText;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RichTextMapper extends BaseMapper<RichText> {

    List<RichText> findByIdAndParentId(Map<String,Object> map);
}
