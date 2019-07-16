package ops.school.api.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * Spring工具类
 * Created by zhengke on 2017/2/5.
 */
public class SpringUtil implements ApplicationContextAware {
    /**
     * spring上下文
     */
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 根据类型获取实例
     *
     * @param classType 类型
     * @param <T>       类型
     * @return 实例
     */
    public static <T> T getBean(Class<T> classType) {
        return context.getBean(classType);
    }

    /**
     * 根据BEAN ID获取实例
     *
     * @param beanId id
     * @return 实例
     */
    public static Object getBean(String beanId) {
        return context.getBean(beanId);
    }

    /**
     * 获取所有当前类型的实例返回map
     *
     * @param classType 类型
     * @param <T>       类型
     * @return 实例map
     */
    public static <T> Map<String, T> getBeansByType(Class<T> classType) {
        return context.getBeansOfType(classType);
    }

    /**
     * 获取redis缓存状态
     *
     * @return
     */
    public static boolean redisCache() {
        StringRedisTemplate stringRedisTemplate = getBean(StringRedisTemplate.class);
        if (stringRedisTemplate == null) {
            return false;
        } else {
            String rs = stringRedisTemplate.opsForValue().get("cache");
            if (rs == null) {
                return false;
            }
            return Boolean.parseBoolean(rs);
        }
    }
}
