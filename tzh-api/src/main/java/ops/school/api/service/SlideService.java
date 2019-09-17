package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Slide;

import java.util.List;

public interface SlideService extends IService<Slide> {

    List<Slide> findBySchoolId(Integer schoolId);
}
