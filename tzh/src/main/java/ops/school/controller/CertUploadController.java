package ops.school.controller;

import io.swagger.annotations.Api;
import ops.school.api.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("ops/cert")
@Api(tags = "证书上传")
public class CertUploadController {

    @Value("${cert.path}")
    private String rootPath;


    @PostMapping("up")
    private String upload(MultipartFile file) {
        try {
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            String fileName = UUID.randomUUID() + ".p12";
            file.transferTo(new File(rootPath + "/" + fileName));
            return rootPath + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.logError("上传证书失败-error" + e.getMessage());
            return "error";
        }

    }
}
