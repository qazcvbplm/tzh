package ops.school.api.exception;

import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.enums.RootEnums;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * CreatebyFang
 * fangfor@outlook.com   Time:2019/7/15 17:02
 * #
 */

public class Assertions {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true \n" + PublicErrorEnums.FAILED.getErrorMessage());
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(String message,Object... object) {
        if (object.length < 1 ) {
            throw new IllegalArgumentException("{no params for Assertions.notNull()}");
        }
        String paramNames = "";
        Integer sum = 0;
        for (int i = 0; i < object.length -1; i++) {
            if (object[i] == null){
                paramNames += "\n 第"+(i+1)+"个参数为空 \n";
                sum++;
            }
        }
        if (sum > 0){
            throw new IllegalArgumentException(PublicErrorEnums.PULBIC_EMPTY_PARAM.getErrorMessage()+"{" + message + paramNames +"}");
        }
    }

    /**
     * 通过枚举报错
     * @param object
     * @param errorEnums
     */
    public static void notNull(Object object, PublicErrorEnums errorEnums) {
        if (object == null) {
            throw new IllegalArgumentException(errorEnums.getErrorMessage());
        }
    }

    public static void notNull(Object object) {
        notNull(object, PublicErrorEnums.PULBIC_EMPTY_PARAM+"\n [Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * @auther: Fang
     * @date:   2019/05/23 14:51
     * @desc:   参数都是要必填的参数
     */
    public static void notNull(Object... object) {
        notNull("[Assertion failed] - those argument one (or more) is (or are) required; it must not be null",object);
    }

    /**
     * @author: QinDaoFang
     * @date:   2019/7/15 17:23
     * @desc:   和hasText区别是能不能为空“ ”，这个可以为空
     */
    public static void hasLength(String text, String message) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text, RootEnums enums) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(enums.getErrorMessage());
        }
    }

    public static void hasLength(String text) {
        hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasText(String text) {
        hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    public static void hasText(String text, RootEnums enums) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(enums.getErrorMessage());
        }
    }

    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void doesNotContain(String textToSearch, String substring) {
        doesNotContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
    }

    public static void notEmpty(Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Object[] array) {
        notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            Object[] var2 = array;
            int var3 = array.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object element = var2[var4];
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }

    }

    public static void noNullElements(Object[] array) {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    public static void notEmpty(Collection<?> collection, RootEnums enums) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(enums.getErrorMessage());
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, PublicErrorEnums.PULBIC_EMPTY_PARAM.getErrorMessage()+"\n [Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    public static void isInstanceOf(Class<?> clazz, Object obj) {
        isInstanceOf(clazz, obj, "");
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException((StringUtils.hasLength(message) ? message + " " : "") + "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, "");
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException((StringUtils.hasLength(message) ? message + " " : "") + subType + " is not assignable to " + superType);
        }
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression) {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }
}
