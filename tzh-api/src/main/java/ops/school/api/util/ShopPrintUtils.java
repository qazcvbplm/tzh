package ops.school.api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feyin.api.FeyinClient;
import com.feyin.api.FeyinResponse;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.dto.print.FeiERsultData;
import ops.school.api.dto.print.ShopPrintFeiEDTO;
import ops.school.api.enums.ResponseViewEnums;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/12
 * 11:33
 * #
 */
public class ShopPrintUtils {

    public static ShopPrintFeiEDTO feiEAddPrinter(String snList) {

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                //读取超时
                .setSocketTimeout(3000)
                //连接超时
                .setConnectTimeout(3000)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(ShopPrintConfigConstants.FEI_E_URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", ShopPrintConfigConstants.FEI_E_USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(ShopPrintConfigConstants.FEI_E_USER, ShopPrintConfigConstants.FEI_E_UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printerAddlist"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("printerContent", snList));

        CloseableHttpResponse response = null;
        ShopPrintFeiEDTO feiEDTO = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                //请求体内容
                String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                //转化成json对象然后返回accessToken属性的值
                 feiEDTO = JSONObject.parseObject(responseContent,ShopPrintFeiEDTO.class);
                //如果返回的参数有no
                if (feiEDTO != null && feiEDTO.getData().getNo().size() != NumConstants.INT_NUM_0){
                    //截取错误信息
                    String msg = feiEDTO.getData().getNo().get(0);
                    String errorMsg = msg.substring(msg.indexOf("错") - 1);
                    //打印日志
                    LoggerUtil.logError("系统记录-添加飞鹅打印机失败-saveOneShopFeiEByDTO-日志" + errorMsg);
                    //set到对象返回
                    feiEDTO.setErrorMessage(errorMsg);
                    feiEDTO.setSuccess(false);
                    return feiEDTO;
                }else if (feiEDTO == null){
                    return new ShopPrintFeiEDTO(false, ResponseViewEnums.SHOP_ADD_FEI_FAILED.getErrorMessage());
                }else {
                    feiEDTO.setSuccess(true);
                }
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    String result = EntityUtils.toString(httpentity);
                    LoggerUtil.log("系统记录-添加飞鹅打印机-saveOneShopFeiEByDTO-日志" + result);
                }
            } else {
                return new ShopPrintFeiEDTO(false, ResponseViewEnums.SHOP_ADD_FEI_FAILED.getErrorMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.logError(e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return feiEDTO;

    }

    /**
     * @param
     * @date: 2019/8/12 14:25
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String
     * @Desc: desc +appid=APPID
     */
    public static String feiYinGetToken() throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()
                //读取超时
                .setSocketTimeout(3000)
                //连接超时
                .setConnectTimeout(3000)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        String params = "code=" + ShopPrintConfigConstants.FE_YIN_2_0_USER_MEMBER_CODE + "&" + "secret=" + ShopPrintConfigConstants.FE_YIN_2_0_API_KEY;
        //创建http get方式
        HttpGet httpGet = new HttpGet(ShopPrintConfigConstants.FE_YIN_URL_2_0_TOKEN + params);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //请求体内容
                String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                //转化成json对象然后返回accessToken属性的值
                JSONObject responseJson = JSONObject.parseObject(responseContent);
                String accessToken = responseJson.getString("access_token");
                System.out.println(accessToken);
                result = accessToken;
            } else {
                return null;
            }
        } catch (Exception ex) {
            LoggerUtil.logError("系统记录-获取飞印打印机token日志-feiYinGetToken-日志" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpGet.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean feiYinAddPrinter(String deviceNo) {
        String accessToken = null;
        try {
            accessToken = ShopPrintUtils.feiYinGetToken();
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.logError("系统记录-添加飞印打印机-saveOneShopFeiEByDTO-feiYinAddPrinter-日志" + e.getMessage());
            return false;
        }
        String bindUrl = "https://api.open.feyin.net/device/" + deviceNo + "/bind"+"?access_token=" + accessToken;
        //

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                //读取超时
                .setSocketTimeout(3000)
                //连接超时
                .setConnectTimeout(3000)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(bindUrl);

        long reqTime = System.currentTimeMillis();
        JSONObject object = new JSONObject();
//        object.put("memberCode", this.memberCode);
//        object.put("securityCode", this.getSecurityCode(deviceNo, reqTime));
        object.put("deviceNo", deviceNo);
        object.put("reqTime", reqTime);
        object.put("access_token", accessToken);
        String json = object.toString();

        CloseableHttpResponse response = null;
        String result = null;
        StringEntity se = new StringEntity(json, Consts.UTF_8);
        post.setEntity(se);
        try {
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                //请求体内容
                String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                //转化成json对象然后返回accessToken属性的值
                JSONObject responseJson = JSONObject.parseObject(responseContent);
                String errcode = responseJson.getString("errcode");
                String errmsg = responseJson.getString("errmsg");
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    //result = EntityUtils.toString(httpentity);
                    LoggerUtil.log("系统记录-添加飞鹅打印机-saveOneShopFeiEByDTO-日志" + responseJson);
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }


    //生成签名字符串
    private static String signature(String USER, String UKEY, String STIME) {
        String s = DigestUtils.sha1Hex(USER + UKEY + STIME);
        return s;
    }

    public static void main(String[] args) throws Exception {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(9);
        System.out.println("开始任务");

        //延时3秒执行
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行任务");
            }
        }, 2, TimeUnit.SECONDS);
        System.out.println("本程序存在5秒后自动退出------------");
        executor.shutdown();
        return;
    }
}
