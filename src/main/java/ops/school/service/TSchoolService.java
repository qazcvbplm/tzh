package ops.school.service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface TSchoolService {
    @Transactional
    String tx(int schoolId, BigDecimal amount, String openId);
}
