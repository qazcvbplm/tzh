package ops.school.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;  
import java.util.List;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FetchRet;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
  
import com.qiniu.common.QiniuException;  
import com.qiniu.http.Response;  
import com.qiniu.storage.BucketManager;  
import com.qiniu.storage.UploadManager;  
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;  
import com.qiniu.util.Auth;

/**
 * @author: QinDaoFang
 * @date:   2019/7/31 13:52
 * @desc:   七牛操作工具类
 */
public class QiNiuUtils {
    private final static String ACCESS_KEY = "jra5S4ulxngso0Fs6JhCmx2OMAS9DL1yjwa6v6Oo";
    private final static String SECRET_KEY = "eywGi5zc52FYWQkAECG5RtvlOf53yXHtKIvSWPQV";
    /**默认上传空间*/  
    private final static String BUCKET_NAME = "";
    /**空间默认域名*/  
    private final static String BUCKET_HOST_NAME = "http://ops.chuyinkeji.cn";

    // 构造一个带指定Zone对象的配置类
    private final static Configuration configuration = new Configuration(Zone.zone0());
      
    private static UploadManager uploadManager = new UploadManager(configuration);
      
    private static int LIMIT_SIZE = 1000;



    /**
     * @date:   2019/7/31 13:53
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String[]
     * @param
     * @Desc:   desc 返回七牛帐号的所有空间
     */
    public static String[] listBucket() throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        return bucketManager.buckets();  
    }  
      

    /**
     * @date:   2019/7/31 14:12
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<com.qiniu.storage.model.FileInfo>
     * @Description: 获取指定空间下的文件列表
     * @param bucketName  空间名称
     * @param prefix      文件名前缀
     * @param limit       每次迭代的长度限制，最大1000，推荐值 100[即一个批次从七牛拉多少条]
     * @Desc:   desc
     */
    public static List<FileInfo> listFileOfBucket(String bucketName,String prefix,int limit) {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        BucketManager.FileListIterator it = bucketManager.createFileListIterator(bucketName, prefix, limit, null);  
        List<FileInfo> list = new ArrayList<FileInfo>();  
        while (it.hasNext()) {  
            FileInfo[] items = it.next();  
            if (null != items && items.length > 0) {  
                list.addAll(Arrays.asList(items));  
            }  
        }  
        return list;  
    }

    /**
     * 获取下载文件路径，即：donwloadUrl
     *
     * @return
     */
    public String getDownloadUrl(String targetUrl) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String downloadUrl = auth.privateDownloadUrl(targetUrl);
        return downloadUrl;
    }

    /**
     * 文件下载
     *
     * @param targetUrl
     */
    public void download(String targetUrl) {
        //获取downloadUrl
        String downloadUrl = getDownloadUrl(targetUrl);
        //本地保存路径
        String filePath = "/home/qiniu";
        download(downloadUrl, filePath);
    }

    private static InputStream downloadHttpFile(String httpUrl) {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(httpUrl).build();
        okhttp3.Response resp = null;
        try {
            resp = client.newCall(req).execute();
            System.out.println(resp.isSuccessful());
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();
                InputStream is = body.byteStream();
                return is;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unexpected code " + resp);
        }
        return null;
    }

    /**
     * 通过发送http get 请求获取文件资源 放到本地
     *
     * @param url
     * @param filepath
     * @return
     */
    private static void download(String url, String filepath) {
        OkHttpClient client = new OkHttpClient();
        System.out.println(url);
        Request req = new Request.Builder().url(url).build();
        okhttp3.Response resp = null;
        try {
            resp = client.newCall(req).execute();
            System.out.println(resp.isSuccessful());
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();
                InputStream is = body.byteStream();
                byte[] data = readInputStream(is);
                //判断文件夹是否存在，不存在则创建
                File file = new File(filepath);
                if (!file.exists() && !file.isDirectory()) {
                    System.out.println("===文件夹不存在===创建====");
                    file.mkdir();
                }
                File imgFile = new File(filepath + "888.jpg");
                FileOutputStream fops = new FileOutputStream(imgFile);
                fops.write(data);
                fops.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unexpected code " + resp);
        }
    }

    /**
     * 读取字节输入流内容
     *
     * @param is
     * @return
     */
    private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 2];
        int len = 0;
        try {
            while ((len = is.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toByteArray();
    }

    /**
     * @date:   2019/7/31 14:12
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String
     * @Description: 七牛图片上传
     * @param @param inputStream    待上传文件输入流
     * @param @param bucketName     空间名称
     * @param @param key            空间内文件的key
     * @param @param mimeType       文件的MIME类型，可选参数，不传入会自动判断
     * @Desc:   desc
     */
    public static String uploadFile(InputStream inputStream,String bucketName,String key,String mimeType) throws IOException {    
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        String token = auth.uploadToken(bucketName);  
        byte[] byteData = IOUtils.toByteArray(inputStream);  
        Response response = uploadManager.put(byteData, key, token, null, mimeType, false);  
        inputStream.close();  
        return response.bodyString();  
    }  
      

    /**
     * @date:   2019/7/31 14:11
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String
     * @Description: 七牛图片上传
     * @param @param inputStream    待上传文件输入流
     * @param @param bucketName     空间名称
     * @param @param key            空间内文件的key
     * @Desc:   desc
     */
    public static String uploadFile(InputStream inputStream,String bucketName,String key) throws IOException {    
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        String token = auth.uploadToken(bucketName);  
        byte[] byteData = IOUtils.toByteArray(inputStream);  
        Response response = uploadManager.put(byteData, key, token, null, null, false);  
        inputStream.close();  
        return response.bodyString();  
    }  
      

    /**
     * @date:   2019/7/31 14:11
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String
     * @Description: 七牛图片上传
     * @param filePath     待上传文件的硬盘路径
     * @param fileName     待上传文件的文件名
     * @param bucketName   空间名称
     * @param key          空间内文件的key
     * @Desc:   desc
     */
    public static String uploadFile(String filePath,String fileName,String bucketName,String key) throws IOException {    
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        String token = auth.uploadToken(bucketName);  
        InputStream is = new FileInputStream(new File(filePath + fileName));  
        byte[] byteData = IOUtils.toByteArray(is);  
        Response response = uploadManager.put(byteData, (key == null || "".equals(key))? fileName : key, token);  
        is.close();  
        return response.bodyString();  
    }  
      

    /**
     * @date:   2019/7/31 14:11
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String
     * @Description: 七牛图片上传[若没有指定文件的key,则默认将fileName参数作为文件的key]
     * @param filePath     待上传文件的硬盘路径
     * @param fileName     待上传文件的文件名
     * @param bucketName   空间名称
     * @Desc:   desc
     */
    public static String uploadFile(String filePath,String fileName,String bucketName) throws IOException {    
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        String token = auth.uploadToken(bucketName);  
        InputStream is = new FileInputStream(new File(filePath + fileName));  
        byte[] byteData = IOUtils.toByteArray(is);  
        Response response = uploadManager.put(byteData, fileName, token);  
        is.close();  
        return response.bodyString();  
    }  
      

    /**
     * @date:   2019/7/31 14:11
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String
     * @Description: 提取网络资源并上传到七牛空间里
     * @param url           网络上一个资源文件的URL
     * @param bucketName    空间名称
     * @param key           空间内文件的key[唯一的]
     * @Desc:   desc
     */
    public static String fetchToBucket(String url,String bucketName,String key) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        FetchRet putret = bucketManager.fetch(url, bucketName, key);
        return putret.key;  
    }  
      

    /**
     * @date:   2019/7/31 14:10
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String
     * @param   url
     * @param   bucketName
     * @Desc:   desc 提取网络资源并上传到七牛空间里,不指定key，则默认使用url作为文件的key
     */
    public static String fetchToBucket(String url,String bucketName) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        FetchRet putret = bucketManager.fetch(url, bucketName);
        return putret.key;  
    }  
      

    /**
     * @date:   2019/7/31 14:10
     * @author: QinDaoFang
     * @version:version
     * @return: void
     * @Description: 七牛空间内文件复制
     * @param bucket         源空间名称
     * @param key            源空间里文件的key(唯一的)
     * @param targetBucket   目标空间
     * @param targetKey      目标空间里文件的key(唯一的)
     * @Desc:   desc
     */
    public static void copyFile(String bucket, String key, String targetBucket, String targetKey) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        bucketManager.copy(bucket, key, targetBucket, targetKey);  
    }  
      

    /**
     * @date:   2019/7/31 14:10
     * @author: QinDaoFang
     * @version:version
     * @return: void
     * @Description: 七牛空间内文件剪切
     * @param bucket         源空间名称
     * @param key            源空间里文件的key(唯一的)
     * @param targetBucket   目标空间
     * @param targetKey      目标空间里文件的key(唯一的)
     * @Desc:   desc
     */
    public static void moveFile(String bucket, String key, String targetBucket, String targetKey) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        bucketManager.move(bucket, key, targetBucket, targetKey);  
    }  
      

    /**
     * @date:   2019/7/31 14:10
     * @author: QinDaoFang
     * @version:version
     * @return: void
     * @param   bucket
     * @param   key
     * @param   targetKey
     * @Desc:   desc七牛空间内文件重命名
     */
    public static void renameFile(String bucket, String key, String targetKey) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        bucketManager.rename(bucket, key, targetKey);  
    }  
      

    /**
     * @date:   2019/7/31 14:09
     * @author: QinDaoFang
     * @version:version
     * @Description: 七牛空间内文件删除
     * @param bucket    空间名称
     * @param key       空间内文件的key[唯一的]
     * @Desc:   desc
     */
    public static void deleteFile(String bucket, String key) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        bucketManager.delete(bucket, key);  
    }  
      

    /**
     * @date:   2019/7/31 14:09
     * @author: QinDaoFang
     * @version:version
     * @return: com.qiniu.storage.model.FileInfo[]
     * @Description: 返回指定空间下的所有文件信息
     * @param @param bucketName   空间名称
     * @param @param prefix       文件key的前缀
     * @param @param limit        批量提取的最大数目
     * @Desc:   desc
     */
    public static FileInfo[] findFiles(String bucketName,String prefix,int limit) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        FileListing listing = bucketManager.listFiles(bucketName, prefix, null, limit, null);  
        if(listing == null || listing.items == null || listing.items.length <= 0) {  
            return null;  
        }  
        return listing.items;  
    }  
      

    /**
     * @date:   2019/7/31 14:09
     * @author: QinDaoFang
     * @version:version
     * @return: com.qiniu.storage.model.FileInfo[]
     * @Description: 返回指定空间下的所有文件信息
     * @param @param bucketName   空间名称
     * @param @param prefix       文件key的前缀
     * @Desc:   desc
     */
    public static FileInfo[] findFiles(String bucketName,String prefix) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        FileListing listing = bucketManager.listFiles(bucketName, prefix, null, LIMIT_SIZE, null);  
        if(listing == null || listing.items == null || listing.items.length <= 0) {  
            return null;  
        }  
        return listing.items;  
    }  


    /**
     * @date:   2019/7/31 14:09
     * @author: QinDaoFang
     * @version:version
     * @return: com.qiniu.storage.model.FileInfo[]
     * @param   bucketName
     * @Desc:   desc 返回指定空间下的所有文件信息
     */
    public static FileInfo[] findFiles(String bucketName) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        FileListing listing = bucketManager.listFiles(bucketName, null, null, LIMIT_SIZE, null);  
        if(listing == null || listing.items == null || listing.items.length <= 0) {  
            return null;  
        }  
        return listing.items;  
    }  
      

    /**
     * @date:   2019/7/31 14:08
     * @author: QinDaoFang
     * @version:version
     * @return: com.qiniu.storage.model.FileInfo
     * @param   bucketName
     * @param   key
     * @param   limit
     * @Desc:   desc 返回指定空间下的某个文件
     */
    public static FileInfo findOneFile(String bucketName,String key,int limit) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        FileListing listing = bucketManager.listFiles(bucketName, key, null, limit, null);  
        if(listing == null || listing.items == null || listing.items.length <= 0) {  
            return null;  
        }  
        return (listing.items)[0];  
    }  
      

    /**
     * @date:   2019/7/31 14:08
     * @author: QinDaoFang
     * @version:version
     * @return: com.qiniu.storage.model.FileInfo
     * @param   bucketName
     * @param   key
     * @Desc:   desc 返回指定空间下的某个文件(重载)
     */
    public static FileInfo findOneFile(String bucketName,String key) throws QiniuException {  
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  
        BucketManager bucketManager = new BucketManager(auth,configuration);
        FileListing listing = bucketManager.listFiles(bucketName, key, null, LIMIT_SIZE, null);  
        if(listing == null || listing.items == null || listing.items.length <= 0) {  
            return null;  
        }  
        return (listing.items)[0];  
    }  

    /**
     * @date:   2019/7/31 14:07
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.String
     * @param   key
     * @Desc:   desc 返回七牛空间内指定文件的访问URL
     */
    public static String getFileAccessUrl(String key) throws QiniuException {  
        return BUCKET_HOST_NAME + "/" + key;  
    }  
      
    public static void main(String[] args) throws IOException {  
        //uploadFile("C:/test.jpg");  
          
        /*String[] buckets = listBucket(); 
        for(String bucket : buckets) { 
            System.out.println(bucket); 
        }*/  
          
        /*List<FileInfo> list = listFileOfBucket(BUCKET_NAME, null, 1000); 
        for(FileInfo fileInfo : list) { 
            System.out.println("key：" + fileInfo.key); 
            System.out.println("hash：" + fileInfo.hash); 
            System.out.println("................"); 
        }*/  
          
        //copyFile(BUCKET_NAME, "images-test", BUCKET_NAME, "images-test-1111");  
          
        //renameFile(BUCKET_NAME, "images-test-1111", "images-test-2222.jpg");  
          
        //deleteFile(BUCKET_NAME, "images-test-2222.jpg");  
          
        //fetchToBucket("http://www.nanrenwo.net/uploads/allimg/121026/14-1210261JJD03.jpg", BUCKET_NAME,"1111111111111111.jpg");  
          
//        FileInfo[] fileInfos = findFiles(BUCKET_NAME, "10", LIMIT_SIZE);
//        for(FileInfo fileInfo : fileInfos) {
//            System.out.println(fileInfo.key);
//            System.out.println(fileInfo.hash);
//            System.out.println("..............");
//        }
        InputStream inputStream = downloadHttpFile("http://ops.chuyinkeji.cn/708dc02a-f7fd-47f8-8d84-bc7fbbb214ed");
    }  
}  