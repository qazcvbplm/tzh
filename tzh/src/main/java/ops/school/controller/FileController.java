package ops.school.controller;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/ops/filesystem")
public class FileController {

    @CrossOrigin
    @PostMapping("/upfile")
    public String fileup(HttpServletRequest request, MultipartFile file) {
        if (file.getSize() > 100 * 1024) {
            return "图片不可超过100KB";
        }
        FileInputStream inputStream;
        try {
            inputStream = (FileInputStream) file.getInputStream();
            String path = uploadQNImg(inputStream, UUID.randomUUID().toString()); // KeyUtil.genUniqueKey()生成图片的随机名
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将图片上传到七牛云
     */
    private String uploadQNImg(FileInputStream file, String key) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传

        try {
            Auth auth = Auth.create("jra5S4ulxngso0Fs6JhCmx2OMAS9DL1yjwa6v6Oo", "eywGi5zc52FYWQkAECG5RtvlOf53yXHtKIvSWPQV");
            String upToken = auth.uploadToken("school");
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                String returnPath = "http://ops.chuyinkeji.cn" + "/" + putRet.key;
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                return r.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
