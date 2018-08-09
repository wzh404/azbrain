package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.service.UserManagerService;
import com.blueocean.azbrain.util.CryptoUtil;
import com.blueocean.azbrain.util.ExcelUtil;
import com.blueocean.azbrain.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${spring.resources.static-locations}")
    private String resourceLocation;

    @Value("${azbrain.static.icon.url}")
    private String staticUrl;

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/upload", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject upload(@RequestParam(value = "file") MultipartFile file){
        if (file.isEmpty()) {
            logger.warn("upload file is empty");
            return ResultObject.fail(ResultCode.MANAGE_UPLOAD_FILE_FAILED);
        }

        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf('.'));

        // 过滤掉file:前缀
        String filePath = resourceLocation.substring(5);
        String destFileName = CryptoUtil.sha1(fileName, System.currentTimeMillis()+"", Math.random()+"");
        File dest = new File(filePath + destFileName + suffixName);
        try {
            file.transferTo(dest);
            String iconUrl = staticUrl + destFileName + suffixName;
            return ResultObject.ok("file", iconUrl);
        } catch (Exception e) {
            logger.error("file", e);
            return ResultObject.fail(ResultCode.MANAGE_UPLOAD_FILE_FAILED);
        }
    }
}
