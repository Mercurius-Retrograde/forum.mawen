package com.example.forum.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.example.forum.exception.CustomizeErrorCode;
import com.example.forum.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

@Service
public class UCloudProvider {
    @Value("${ucloud.us3.public-key}")
    private String publickey;
    @Value("${ucloud.us3.private-key}")
    private String privatekey;
    @Value("${ucloud.us3.bucketName}")
    private String bucketName;
    @Value("${ucloud.us3.region}")
    private String region;
    @Value("${ucloud.us3.proxySuffix}")
    private String proxySuffix;
    @Value("${ucloud.us3.expiresDuration}")
    private Integer expiresDuration;


    public String upload(InputStream fileStream, String mimeType, String fileName) {
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            // Bucket相关API的授权器
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publickey, privatekey);
            // 对象操作需要ObjectConfig来配置您的地区和域名后缀
            ObjectConfig config = new ObjectConfig(region, proxySuffix);
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    })
                    .execute();
                    if(response != null && response.getRetCode() == 0) {
                        String url = UfileClient.object(objectAuthorization, config)
                                .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, expiresDuration)
                                .createUrl();
                        return url;
                    } else {
//                        log.error("upload error,{}", response);
                        throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
                    }
        } catch (UfileClientException e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
