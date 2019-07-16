package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.SlideMapper;
import ops.school.api.entity.Slide;
import ops.school.api.service.SlideService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlideServiceImpl extends ServiceImpl<SlideMapper, Slide> implements SlideService {

    @Override
    public boolean save(Slide entity) {
        entity.setSort(System.currentTimeMillis());
        return super.save(entity);
    }

    @Override
    public List<Slide> findBySchoolId(Integer schoolId) {
        QueryWrapper<Slide> query = new QueryWrapper<Slide>();
        query.lambda().eq(Slide::getSchoolId, schoolId);
        return this.list(query);
    }
}
