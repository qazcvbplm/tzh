package ops.school.api.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import ops.school.api.entity.Shop;

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

                K key = null;
                try {
                    key = (K) ReflectUtilS.getValue(object, keyProp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                V value = (V) ((StringUtils.isEmpty(valueProp))?(object):(PROBE.getObject(object, valueProp)));
                if (value != null) {
                    map.put(key, value);
                }

            }

        }

        return map;
    }


    /**
     * 根据字段名将list转为map
     */
    @SuppressWarnings("unchecked")
    public static <K, V, E> Map<K, V> listForMapValueE(List<E> list, String keyProp) {

        Map<K, V> map = new LinkedHashMap<>();

        if (CollectionUtils.isNotEmpty(list)) {

            list.removeAll(Collections.singleton(null));

            for (E object : list) {

                K key = (K) PROBE.getObject(object, keyProp);
                V value = (V) object;
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
     *
     * <pre>
     * 从List的E中返回valueProp属性的List
     * </pre>
     *
     * @param <E>
     * @param list
     * @param propertyName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> List<Object> GetPropertyList(List<E> list,String propertyName) {
        List<Object> valueList = new ArrayList<Object>();
        for (Object o : list) {
            Object value = PROBE.getObject(o, propertyName);
            valueList.add(value);
        }
        return valueList;
    }

    /**
     *
     * <pre>
     * 从List的E中返回valueProp属性的List
     * </pre>
     *
     * @param <E>
     * @param list
     * @param keyProp map中的的key值
     * @param valueProp map中的value值，为null时，取对象本身
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> Map<Object,Object> ListforMap(List<E> list, String keyProp, String valueProp) {
        Map<Object,Object> map = new HashMap<Object,Object>();

        for (int i = 0, n = list.size(); i < n; i++) {
            Object object = list.get(i);
            if(null == object){
                continue;
            }
            Object key = PROBE.getObject(object, keyProp);
            Object value = null;
            if (valueProp == null || "".equals(valueProp)){
                value = object;
            } else {
                value = PROBE.getObject(object, valueProp);
            }
            map.put(key, value);
        }
        return map;
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

        List<Shop> shopList = new ArrayList<>();
        Shop shop = new Shop();
        shop.setId(1);
        shop.setShopName("aaa");
        shopList.add(shop);
        Shop shop2 = new Shop();
        shop2.setId(2);
        shop2.setShopName("bbb");
        shopList.add(shop2);

        Map map = ListforMap(shopList, "id","shopName");

        Map map1 = listForMap(shopList, "id","shopName");

        Map map2 = listForMapValueE(shopList, "id");


        List list = GetPropertyList(shopList, "id");

        List map3 = getValueList(shopList, "id");
    }
}
