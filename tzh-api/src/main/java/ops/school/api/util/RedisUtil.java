package ops.school.api.util;

import ops.school.api.constants.NumConstants;
import ops.school.api.dto.SchoolIndexDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> schoolCache;


    public SchoolIndexDto init(int schoolId) {
        if (schoolCache.opsForValue().get("ops" + schoolId) == null) {
            return new SchoolIndexDto();
        } else {
            return (SchoolIndexDto) schoolCache.opsForValue().get("ops" + schoolId);
        }
    }

    private void set(int schoolId, SchoolIndexDto temp) {
        schoolCache.opsForValue().set("ops" + schoolId, temp);
    }


    public void userCountadd(int schoolId) {
        SchoolIndexDto temp = init(schoolId);
        temp.userCountadd();
        set(schoolId, temp);
    }


    public void takeoutCountadd(int schoolId) {
        SchoolIndexDto temp = init(schoolId);
        temp.takeoutCountadd();
        set(schoolId, temp);
    }

    public void runCountadd(int schoolId) {
        SchoolIndexDto temp = init(schoolId);
        temp.runCountadd();
        set(schoolId, temp);
    }

    public void takeoutCountSuccessadd(int schoolId) {
        SchoolIndexDto temp = init(schoolId);
        temp.takeoutCountaddSuccess();
        set(schoolId, temp);
    }

    public void runCountSuccessadd(int schoolId) {
        SchoolIndexDto temp = init(schoolId);
        temp.runCountSuccessadd();
        set(schoolId, temp);
    }

    public void cancelRunOrdersAdd(int schoolId) {
        SchoolIndexDto temp = init(schoolId);
        temp.setCancelRunOrderCount(temp.getCancelRunOrderCount()+1);
        set(schoolId, temp);
    }

    public void cancelOrdersAdd(int schoolId) {
        SchoolIndexDto temp = init(schoolId);
        temp.setCancelOrderCount(temp.getCancelOrderCount()+1);
        set(schoolId, temp);
    }

    public void amountadd(int schoolId, BigDecimal amount) {
        SchoolIndexDto temp = init(schoolId);
        temp.amountadd(amount);
        set(schoolId, temp);
    }


    public SchoolIndexDto get(int schoolId) {
        SchoolIndexDto temp = init(schoolId);
        return temp;
    }


    public void clear() {
        Set<String> keys = schoolCache.keys("ops*");
        schoolCache.delete(keys);
    }


    public boolean keyNoExpireThenSetTime(String key, int time, TimeUnit timeUnit) {
        Long exporeTime = schoolCache.boundHashOps(key).getExpire();
        if (exporeTime != null && exporeTime.intValue() > NumConstants.INT_NUM_0){
            return true;
        }
        return schoolCache.boundHashOps(key).expire(time,timeUnit);
    }

    public boolean keyNoExpireThenSetTimeAt(String key, Date date) {
        Long exporeTime = schoolCache.boundHashOps(key).getExpire();
        if (exporeTime != null && exporeTime.intValue() > NumConstants.INT_NUM_0){
            return true;
        }
        return schoolCache.boundHashOps(key).expireAt(date);
    }
}
