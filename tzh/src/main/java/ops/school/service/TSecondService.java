package ops.school.service;

import ops.school.api.entity.SecondHand;

import java.util.List;

public interface TSecondService {

    List<SecondHand> fuzzyFind(String title, Integer isShow, Integer schoolId,
                               String category, Integer page, Integer size);
}
