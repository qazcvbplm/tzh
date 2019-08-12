package ops.school.api.util;

import ops.school.api.constants.ShopPrintConfigConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import com.alibaba.fastjson.JSONObject;



public class HttpClientUtil {

    //apiURL就是访问第三方的URL
    private static String apiURL = ShopPrintConfigConstants.FEI_E_URL;
    //创建应用的时候会对应生成
    private static String client_id = "创建应用的时候会对应生成id";

    private static String client_secret = "";

    public static void main(String[] args) throws Exception {
        System.out.println("=========1获取token=========");
        String accessToken = getToken(apiURL, client_id, client_secret);// 获取token
        if (accessToken != null)
            System.out.println(accessToken);

    }

    @SuppressWarnings("deprecation")
    public static String getToken(String url, String appid, String secret)
            throws Exception {
        String resultStr = null;
        @SuppressWarnings("resource")
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        //JsonParser jsonparer =JsonParser;// 初始化解析json格式的对象
        // 接收参数json列表
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("grant_type", "client_credentials");
        jsonParam.put("client_id", client_id);
        jsonParam.put("client_secret", client_secret);
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        post.setEntity(entity);
        // 请求结束，返回结果
        HttpResponse res = httpClient.execute(post);
        // 如果服务器成功地返回响应
        String responseContent = null; // 响应内容
        HttpEntity httpEntity = res.getEntity();
        responseContent = EntityUtils.toString(httpEntity, "UTF-8");

        //System.out.println( responseContent);
        //JsonObject json = JsonParser.parse(responseContent);
        JSONObject json = JSONObject.parseObject(responseContent);
        // .getAsJsonObject();
        if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            if (json.get("errcode") != null) {
                //resultStr = json.get("errcode").getAsString();
            } else {// 正常情况下
                resultStr = json.get("access_token").toString();
            }
        }
        // 关闭连接 ,释放资源
        httpClient.getConnectionManager().shutdown();
        return resultStr;
    }


    /**
     * 获得环信的所有用户
     */
    @SuppressWarnings("deprecation")
    public static String get(String method, String token) throws Exception {
        String resultStr = null;
        @SuppressWarnings("resource")
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet post = new HttpGet("url");

        //请求时一定要把token放到header带过去
        post.setHeader("Authorization", "Bearer " + token);
        // 请求结束，返回结果
        HttpResponse res = httpClient.execute(post);
        // 如果服务器成功地返回响应
        String responseContent = null; // 响应内容
        HttpEntity httpEntity = res.getEntity();
        responseContent = EntityUtils.toString(httpEntity, "UTF-8");
        JSONObject json = JSONObject.parseObject(responseContent);
        System.out.println(json);
        // 关闭连接 ,释放资源
        httpClient.getConnectionManager().shutdown();
        return resultStr;
    }
}