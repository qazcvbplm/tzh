package ops.school.service.impl;

import ops.school.api.dao.SecondHandMapper;
import ops.school.api.entity.SecondHand;
import ops.school.service.TSecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TSecondServiceImpl implements TSecondService {
    @Autowired
    private SecondHandMapper secondHandMapper;

    @Override
    public List<SecondHand> fuzzyFind(String title, Integer isShow, Integer schoolId,
                                      String category, Integer page, Integer size) {
        if (isShow != null && isShow == 1){
            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("schoolId",schoolId);
            map.put("category",category);
            map.put("page", (page - 1) * size);
            map.put("size", size);
            return secondHandMapper.findFuzzy(map);
        }
        return null;
    }
}
