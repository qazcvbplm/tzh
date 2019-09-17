package ops.school;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jmx.snmp.Timestamp;
import ops.school.api.util.TimeUtilS;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class Api_java_demo {
		
		public static final String URL = "http://api.feieyun.cn/Api/Open/";//不需要修改
		
		public static final String USER = "hzchuyinkeji@163.com";//*必填*：账号名
		public static final String UKEY = "QyN8ICuprmDaJmpe";//*必填*: 注册账号后生成的UKEY
		public static final String SN = "316500010";//*必填*：打印机编号，必须要在管理后台里添加打印机或调用API接口添加之后，才能调用API
		
		
		//**********测试时，打开下面注释掉方法的即可,更多接口文档信息,请访问官网开放平台查看**********
		public static void main(String[] args) throws Exception{
			String a1 = "{\"creatTime\":1568266392923,\"cycleRedisCount\":0,\"ourOrderId\":\"201909121333122541299619669\",\"ourSchoolId\":22,\"ourShopId\":215,\"platePrintSn\":\"test\",\"printBrand\":1,\"realOrder\":{\"addressDetail\":\"椰子大学,楼栋一,201\",\"addressName\":\"陈顺波\",\"addressPhone\":\"18857818257\",\"afterDiscountPrice\":15.00,\"appId\":1,\"boxPrice\":0.00,\"couponFullAmount\":0.00,\"couponId\":0,\"couponUsedAmount\":0.00,\"createTime\":\"2019-09-12 13:33:12\",\"destination\":0,\"discountPrice\":6.00,\"discountType\":\"商品折扣\",\"endTime\":\"\",\"evaluateFlag\":0,\"floorId\":104,\"fullAmount\":0.00,\"fullCutId\":0,\"fullUsedAmount\":0.00,\"id\":\"201909121333122541299619669\",\"op\":[{\"attributeName\":\"A1\",\"attributePrice\":10.00,\"id\":12266,\"orderId\":\"201909121333122541299619669\",\"productCount\":2,\"productDiscount\":6.0000,\"productId\":10276,\"productImage\":\"http://ops.chuyinkeji.cn/5fab35f4-270e-4737-ba15-53fa1009b0d2\",\"productName\":\"A1\",\"totalPrice\":4.0000}],\"openId\":\"oWP5G42cNW2gghAZiqvfOFDQ--0A\",\"originalPrice\":21.00,\"payFoodCoupon\":0.00,\"payPrice\":15.00,\"payTime\":\"2019-09-12 13:33:12\",\"payment\":\"\",\"productPrice\":20.00,\"remark\":\"无\",\"reseverTime\":\"14:13\",\"schoolId\":22,\"schoolTopDownPrice\":1.00,\"sendAddCountPrice\":0.00,\"sendAddDistancePrice\":0.00,\"sendBasePrice\":1.00,\"sendGetFlag\":0,\"sendPrice\":1.00,\"senderId\":0,\"shopAcceptTime\":\"\",\"shopAddress\":\"太和殿\",\"shopId\":215,\"shopImage\":\"http://ops.chuyinkeji.cn/5e499ede-ea15-4dcb-b7b5-435f4ed6ed98\",\"shopName\":\"十点见（本店装修ing）\",\"shopPhone\":\"18857818257\",\"status\":\"商家已接手\",\"typ\":\"外卖订单\",\"waterNumber\":2},\"sendMsg3Params\":[null,null,null],\"waterNumber\":2,\"yesPrintTrue\":1}";
			JSON json = JSONObject.parseObject(a1);
			System.out.println(json);
			
		}
		
		
		
		
		
		//=====================以下是函数实现部分================================================
		
		private static String addprinter(String snlist){
			
		   //通过POST请求，发送打印信息到服务器
			RequestConfig requestConfig = RequestConfig.custom()  
		            .setSocketTimeout(30000)//读取超时  
		            .setConnectTimeout(30000)//连接超时
		            .build();
			
			CloseableHttpClient httpClient = HttpClients.custom()
					 .setDefaultRequestConfig(requestConfig)
					 .build();	
			
		    HttpPost post = new HttpPost(URL);
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("user",USER));
			String STIME = String.valueOf(System.currentTimeMillis()/1000);
			nvps.add(new BasicNameValuePair("stime",STIME));
			nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
			nvps.add(new BasicNameValuePair("apiname","Open_printerAddlist"));//固定值,不需要修改
			nvps.add(new BasicNameValuePair("printerContent",snlist));
			
			CloseableHttpResponse response = null;
			String result = null;
	        try
	        {
	    	   post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
	    	   response = httpClient.execute(post);
	       	   int statecode = response.getStatusLine().getStatusCode();
	       	   if(statecode == 200){
		        	HttpEntity httpentity = response.getEntity(); 
		            if (httpentity != null){
		            	result = EntityUtils.toString(httpentity);
		            }
	           }
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       }
	       finally{
	    	   try {
	    		   if(response!=null){
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
	       return result;
		  
		}
				
				
		//方法1
		private static String print(String sn){
			//标签说明：
			//单标签: 
			//"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
			//"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
			//成对标签：
			//"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
			//<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
			//拼凑订单内容时可参考如下格式
			//根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式
			
			String content;
			content = "<CB>测试打印</CB><BR>";
			content += "名称　　　　　 单价  数量 金额<BR>";
			content += "--------------------------------<BR>";
			content += "饭　　　　　　 1.0    1   1.0<BR>";
			content += "炒饭　　　　　 10.0   10  10.0<BR>";
			content += "蛋炒饭　　　　 10.0   10  100.0<BR>";
			content += "鸡蛋炒饭　　　 100.0  1   100.0<BR>";
			content += "番茄蛋炒饭　　 1000.0 1   100.0<BR>";
			content += "西红柿蛋炒饭　 1000.0 1   100.0<BR>";
			content += "西红柿鸡蛋炒饭 100.0  10  100.0<BR>";
			content += "备注：加辣<BR>";
			content += "--------------------------------<BR>";
			content += "合计：xx.0元<BR>";
			content += "送货地点：广州市南沙区xx路xx号<BR>";
			content += "联系电话：13888888888888<BR>";
			content += "订餐时间：2016-08-08 08:08:08<BR>";
			content += "<QR>http://www.dzist.com</QR>";
			
		   //通过POST请求，发送打印信息到服务器
			RequestConfig requestConfig = RequestConfig.custom()  
		            .setSocketTimeout(30000)//读取超时  
		            .setConnectTimeout(30000)//连接超时
		            .build();
			
			CloseableHttpClient httpClient = HttpClients.custom()
					 .setDefaultRequestConfig(requestConfig)
					 .build();	
			
		    HttpPost post = new HttpPost(URL);
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("user",USER));
			String STIME = String.valueOf(System.currentTimeMillis()/1000);
			nvps.add(new BasicNameValuePair("stime",STIME));
			nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
			nvps.add(new BasicNameValuePair("apiname","Open_printMsg"));//固定值,不需要修改
			nvps.add(new BasicNameValuePair("sn",sn));
			nvps.add(new BasicNameValuePair("content",content));
			nvps.add(new BasicNameValuePair("times","1"));//打印联数
			
			CloseableHttpResponse response = null;
			String result = null;
	        try
	        {
	    	   post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
	    	   response = httpClient.execute(post);
	       	   int statecode = response.getStatusLine().getStatusCode();
	       	   if(statecode == 200){
		        	HttpEntity httpentity = response.getEntity(); 
		            if (httpentity != null){
		            	//服务器返回的JSON字符串，建议要当做日志记录起来
		            	result = EntityUtils.toString(httpentity);
		            }
	           }
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       }
	       finally{
	    	   try {
	    		   if(response!=null){
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
	       return result;
		  
		}

		
		//方法2
		private static String queryOrderState(String orderid){
		
		   //通过POST请求，发送打印信息到服务器
			RequestConfig requestConfig = RequestConfig.custom()  
		            .setSocketTimeout(30000)//读取超时  
		            .setConnectTimeout(30000)//连接超时
		            .build();
			
			CloseableHttpClient httpClient = HttpClients.custom()
					 .setDefaultRequestConfig(requestConfig)
					 .build();	
			
		    HttpPost post = new HttpPost(URL);
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("user",USER));
			String STIME = String.valueOf(System.currentTimeMillis()/1000);
			nvps.add(new BasicNameValuePair("stime",STIME));
			nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
			nvps.add(new BasicNameValuePair("apiname","Open_queryOrderState"));//固定值,不需要修改
			nvps.add(new BasicNameValuePair("orderid",orderid));
			
			CloseableHttpResponse response = null;
			String result = null;
	        try
	        {
	    	   post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
	       	   response = httpClient.execute(post);
	       	   int statecode = response.getStatusLine().getStatusCode();
	       	   if(statecode == 200){
		        	HttpEntity httpentity = response.getEntity(); 
		            if (httpentity != null){
		            	//服务器返回
		            	result = EntityUtils.toString(httpentity);
		            }
	           }
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       }
	        finally{
		    	   try {
		    		   if(response!=null){
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
	       return result;
	  
	   }

		
		
		//方法3
		private static String queryOrderInfoByDate(String sn,String strdate){
		
		    //通过POST请求，发送打印信息到服务器
			RequestConfig requestConfig = RequestConfig.custom()  
		            .setSocketTimeout(30000)//读取超时  
		            .setConnectTimeout(30000)//连接超时
		            .build();
			
			CloseableHttpClient httpClient = HttpClients.custom()
					 .setDefaultRequestConfig(requestConfig)
					 .build();	
			
		    HttpPost post = new HttpPost(URL);
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("user",USER));
			String STIME = String.valueOf(System.currentTimeMillis()/1000);
			nvps.add(new BasicNameValuePair("stime",STIME));
			nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
			nvps.add(new BasicNameValuePair("apiname","Open_queryOrderInfoByDate"));//固定值,不需要修改
			nvps.add(new BasicNameValuePair("sn",sn));
			nvps.add(new BasicNameValuePair("date",strdate));//yyyy-MM-dd格式
			
			CloseableHttpResponse response = null;
			String result = null;
	        try
	        {
	    	   post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
	       	   response = httpClient.execute(post);
	       	   int statecode = response.getStatusLine().getStatusCode();
	       	   if(statecode == 200){
		        	HttpEntity httpentity = response.getEntity(); 
		            if (httpentity != null){
		            	//服务器返回
		            	result = EntityUtils.toString(httpentity);
		            }
	           }
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       }
	        finally{
		    	   try {
		    		   if(response!=null){
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
	       return result;
	  
	   }
		
		
		
		//方法4
		private static String queryPrinterStatus(String sn){
		
		    //通过POST请求，发送打印信息到服务器
			RequestConfig requestConfig = RequestConfig.custom()  
		            .setSocketTimeout(30000)//读取超时  
		            .setConnectTimeout(30000)//连接超时
		            .build();
			
			CloseableHttpClient httpClient = HttpClients.custom()
					 .setDefaultRequestConfig(requestConfig)
					 .build();	
			
		    HttpPost post = new HttpPost(URL);
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("user",USER));
			String STIME = String.valueOf(System.currentTimeMillis()/1000);
			nvps.add(new BasicNameValuePair("stime",STIME));
			nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
			nvps.add(new BasicNameValuePair("apiname","Open_queryPrinterStatus"));//固定值,不需要修改
			nvps.add(new BasicNameValuePair("sn",sn));
			
			CloseableHttpResponse response = null;
			String result = null;
	        try
	        {
	    	   post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
	       	   response = httpClient.execute(post);
	       	   int statecode = response.getStatusLine().getStatusCode();
	       	   if(statecode == 200){
		        	HttpEntity httpentity = response.getEntity(); 
		            if (httpentity != null){
		            	//服务器返回
		            	result = EntityUtils.toString(httpentity);
		            }
	           }
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       }
	        finally{
		    	   try {
		    		   if(response!=null){
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
	       return result;
	  
	   }
		
		
		//生成签名字符串
		private static String signature(String USER,String UKEY,String STIME){
			String s = DigestUtils.sha1Hex(USER+UKEY+STIME);
			return s;
		}

		
	
}
