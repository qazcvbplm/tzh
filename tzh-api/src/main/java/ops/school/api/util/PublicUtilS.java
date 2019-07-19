package ops.school.api.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/7/19
 * 18:43
 * #
 */
public class PublicUtilS {

    private static final Probe PROBE = ProbeFactory.getProbe();

    /**
     * 根据字段名获取list
     */
    @SuppressWarnings("unchecked")
    public static <E> String getCollectionToString(List<E> list) {
        StringBuffer stringBuffer = new StringBuffer();
        if (CollectionUtils.isNotEmpty(list)) {
            return JSON.toJSONString(list);
        }
        return "";
    }

    /**
     * 根据字段名获取list
     */
    @SuppressWarnings("unchecked")
    public static <E, K> List<K> getValueList(List<E> list, String valueProp) {

        List<K> valueList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {

            list.removeAll(Collections.singleton(null));

            for (E e : list) {

                K value = (K) PROBE.getObject(e, valueProp);
                if (value != null) {
                    valueList.add(value);
                }

            }

        }

        return valueList;
    }

    /**
     * 根据字段名将list转为map
     */
    @SuppressWarnings("unchecked")
    public static <K, V, E> Map<K, V> listForMap(List<E> list, String keyProp, String valueProp) {

        Map<K, V> map = new LinkedHashMap<>();

        if (CollectionUtils.isNotEmpty(list)) {

            list.removeAll(Collections.singleton(null));

            for (E object : list) {

                K key = (K) PROBE.getObject(object, keyProp);
                V value = (V) ((StringUtils.isEmpty(valueProp))?(object):(PROBE.getObject(object, valueProp)));
                if (value != null) {
                    map.put(key, value);
                }

            }

        }

        return map;
    }


    /***
     * @desc: desc集合set去重
     * @date: 2019/05/28 09:26
     * @param   list
     * @return:
     * @author: Fang
     */
    public static <T> void removeDuplicate(List<T> list) {
        HashSet<T> set = new HashSet<T>(list.size());
        List<T> result = new ArrayList<T>(list.size());
        for (T t : list) {
            if (set.add(t)) {
                result.add(t);
            }
        }
        list.clear();
        list.addAll(result);
    }


    /**
     * java通过UUID生成16位唯一订单号
     */
    public static String get16OrderIdByUUId() {
        int first = new Random(10).nextInt(8) + 1;
        System.out.println(first);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // d 代表参数为正数型
        return first + String.format("%015d", hashCodeV);
    }

    /**
     * java通过UUID生成32位唯一订单号
     */
    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * @return
     */
    public static String get32CodeByUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static void main(String[] args) {
        String orderingID = get32CodeByUUID();
        System.out.println(orderingID);
    }
}
