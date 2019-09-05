package ops.school.api.util;

import com.alibaba.fastjson.JSON;
import io.swagger.models.auth.In;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.ShopCoupon;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import ops.school.api.entity.Shop;

import java.io.*;
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
     * 根据字段名获取list
     */
    @SuppressWarnings("unchecked")
    public static <E, K> List<K> getValueList(Collection<E> collection, String valueProp) {

        List<K> valueList = new LinkedList<>();

        if (CollectionUtils.isNotEmpty(collection)) {

            collection.removeAll(Collections.singleton(null));
            for (E e : collection) {

                K value = (K) PROBE.getObject(e, valueProp);
                if (value != null) {
                    valueList.add(value);
                }

            }

        }

        return valueList;
    }

    /**
     * 根据字段名获取list
     */
    @SuppressWarnings("unchecked")
    public static <K, V, E> Map<K, V> getValueMap(List<E> list, String keyProp) {

        Map<K, V> map = new LinkedHashMap<>();

        if (CollectionUtils.isNotEmpty(list)) {

            list.removeAll(Collections.singleton(null));

            for (E object : list) {

                K key = null;
                try {
                    key = (K) PROBE.getObject(object, keyProp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (key != null) {
                    map.put(key, (V) object);
                }

            }

        }
        return map;
    }


    /**
     * @param list
     * @param keyProp
     * @param valueProp
     * @date: 2019/7/26 17:19
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.Map<K, java.util.List < V>>
     * @Desc: desc
     */
    @SuppressWarnings("unchecked")
    public static <K, V, E> Map<K, List<V>> listforListMap(List<E> list, String keyProp, String valueProp) {
        Map<K, List<V>> map = Collections.emptyMap();
        if (CollectionUtils.isNotEmpty(list)) {
            list.removeAll(Collections.singleton(null));
            map = new HashMap<K, List<V>>(list.size());
            V value = null;
            for (E object : list) {
                K key = (K) PROBE.getObject(object, keyProp);
                if (StringUtils.isEmpty(valueProp)) {
                    value = (V) object;
                } else {
                    value = (V) PROBE.getObject(object, valueProp);
                }
                List<V> values = map.get(key);
                if (values == null) {
                    values = new ArrayList<V>();
                }
                values.add(value);
                map.put(key, values);
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V, E> Map<K, List<V>> listforEqualKeyListMap(List<E> list, String keyProp) {
        Map<K, List<V>> map = Collections.emptyMap();
        if (CollectionUtils.isNotEmpty(list)) {
            list.removeAll(Collections.singleton(null));
            map = new HashMap<K, List<V>>(list.size());
            V value = null;
            for (E object : list) {
                K key = (K) PROBE.getObject(object, keyProp);
                value = (V) object;
                //如果map里面有key，则取出value
                List<V> valueListInMap = null;
                if (map.containsKey(key)) {
                    valueListInMap = map.get(key);
                    valueListInMap.add(value);
                    map.put(key, valueListInMap);
                    continue;
                }
                List<V> values = map.get(key);
                if (values == null) {
                    values = new ArrayList<V>();
                }
                values.add(value);
                map.put(key, values);
            }
        }
        return map;
    }

    /**
     * @author: QinDaoFang
     * @date: 2019/7/27 12:17
     * @desc: 特殊方法
     */
//    @SuppressWarnings("unchecked")
//    public static <K, V, E> Map<K, V> getCouponIdAndShopIdsString(List<E> list,String keyProp,String valueProp){
//        Map<K, List<V>> map = Collections.emptyMap();
//        if (CollectionUtils.isNotEmpty(list)) {
//            list.removeAll(Collections.singleton(null));
//            StringBuffer shopIdsString = null;
//            V value = null;
//            map = new HashMap<K, List<V>>(list.size());
//            for (int i = 0; i < list.size(); i++) {
//                E object = list.get(i);
//                K key = (K) PROBE.getObject(object, keyProp);
//                value = (V) PROBE.getObject(object, valueProp);
//                shopIdsString.append(value);
//                if (i < shopIds.size() -1){
//                    resultShopIds.append(',');
//                }
//
//                List<V> values = map.get(key);
//                if (values == null) {
//                    values = new ArrayList<V>();
//                }
//                values.add(value);
//                map.put(key, values);
//            }
//            for (E object : list) {
//
//            }
//        }
//        return map;
//    }

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
                    key = (K) PROBE.getObject(object, keyProp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                V value = (V) ((StringUtils.isEmpty(valueProp)) ? (object) : (PROBE.getObject(object, valueProp)));
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

    /**
     * 根据字段名将list转为map
     */
    @SuppressWarnings("unchecked")
    public static <K, V, E> Map<K, V> listForMapValueE(Collection<E> Collection, String keyProp) {

        Map<K, V> map = new HashMap<>();

        if (CollectionUtils.isNotEmpty(Collection)) {

            Collection.removeAll(Collections.singleton(null));

            for (E object : Collection) {

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

    @SuppressWarnings("unchecked")
    public static <T> T[] removeDuplicate(T[] array) {
        HashSet<T> set = new HashSet<T>(array.length);
//        T[] result = (T[]) new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            if (set.add(array[i])) {
                //result[i] = (T) array[i];
            }
        }
        return (T[]) set.toArray();
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
    public static <E> List<Object> GetPropertyList(List<E> list, String propertyName) {
        List<Object> valueList = new ArrayList<Object>();
        for (Object o : list) {
            Object value = PROBE.getObject(o, propertyName);
            valueList.add(value);
        }
        return valueList;
    }

    /**
     * <pre>
     * 从List的E中返回valueProp属性的List
     * </pre>
     *
     * @param <E>
     * @param list
     * @param keyProp   map中的的key值
     * @param valueProp map中的value值，为null时，取对象本身
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> Map<Object, Object> ListforMap(List<E> list, String keyProp, String valueProp) {
        Map<Object, Object> map = new HashMap<Object, Object>();

        for (int i = 0, n = list.size(); i < n; i++) {
            Object object = list.get(i);
            if (null == object) {
                continue;
            }
            Object key = PROBE.getObject(object, keyProp);
            Object value = null;
            if (valueProp == null || "".equals(valueProp)) {
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
     *
     * @return
     */
    public static String get32CodeByUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @param inputStream
     * @date: 2019/9/5 13:05
     * @author: QinDaoFang
     * @version:version
     * @return: boolean
     * @Desc: desc
     */
    public static boolean checkInputStreamNullTrue(InputStream inputStream) {
        long size = 0;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("");
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(b)) != -1) {
                fileInputStream.read(b, 0, len);
            }
            size = fileInputStream.getChannel().size();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size > 0;
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

        Map map = ListforMap(shopList, "id", "shopName");

        Map map1 = listForMap(shopList, "id", "shopName");

        Map map2 = listForMapValueE(shopList, "id");


        List list = GetPropertyList(shopList, "id");

        List map3 = getValueList(shopList, "id");

        map2.get(Long.valueOf(2));
        map2.get(1);


        String[] array = {"1", "2", "4", "1", "1", "4", "5",};
        int[] intarray = {1, 2, 3, 1, 2, 4};
        Integer[] tegArray = {1, 2, 3, 1, 2, 4, 5};

        List<Long> longList = new ArrayList<>();
        longList.add(Long.valueOf(2));
        longList.add(Long.valueOf(1));
        longList.add(Long.valueOf(5));
        longList.add(Long.valueOf(1));
        longList.add(Long.valueOf(7));
        longList.add(Long.valueOf(6));
        System.out.println(longList);
        removeDuplicate(longList);
        System.out.println(longList);

    }


}
