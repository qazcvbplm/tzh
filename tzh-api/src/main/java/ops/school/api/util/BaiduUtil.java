package ops.school.api.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ops.school.api.exception.YWException;
import org.springframework.web.client.RestTemplate;


public class BaiduUtil {

    private final static String zbzh = "http://api.map.baidu.com/geoconv/v1/?coords=";    //百度坐标转化api
    private final static String ak = "oRYkfitFSo3DxFdy8aI9y2PiQ6Km2DjC";                  //app key
    private final static String distance = "http://api.map.baidu.com/routematrix/v2/walking"; // 百度测距离api
    private final static String locationurl = "http://api.map.baidu.com/geocoder/v2/?ak=qFbGsVhASUXTGeLaz4H243dpqjlDyg6W";


    /**
     * 计算两个点距离
     *
     * @param origins      x,y  起点
     * @param destinations x1,y1|x2,y2.......
     * @return
     */
    public static int DistanceAll(String origins, String destinations) {
        String url = distance;
        String r = new RestTemplate().getForObject(url + "?output=json&origins=" + origins + "&destinations=" + destinations + "&ak=" + ak, String.class);
        //String r =HttpRequest.sendGet(url,"&output=json&origins=" + origins + "&destinations=" + destinations + "&ak=" + ak);
        JSONObject json = JSON.parseObject(r, JSONObject.class);
        if (json.getInteger("status") != 0) {
            throw new YWException(json.getString("message"));
        } else {
            return json.getJSONArray("result").getJSONObject(0).getJSONObject("distance").getInteger("value");
        }
    }

}
