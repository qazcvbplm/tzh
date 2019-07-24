package ops.school.service;

import ops.school.api.entity.SecondHand;

import java.util.List;

public interface TSecondService {

    List<SecondHand> fuzzyFind(String title, Integer page, Integer size);
}
