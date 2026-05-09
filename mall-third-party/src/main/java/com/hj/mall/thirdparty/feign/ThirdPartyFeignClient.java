package com.hj.mall.thirdparty.feign;

import com.hj.mall.common.api.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 第三方服务 Feign 客户端
 * 供其他微服务调用文件上传功能
 */
@FeignClient(
        name = "mall-third-party",
        path = "/feign/oss",
        fallbackFactory = ThirdPartyFeignFallback.class
)
public interface ThirdPartyFeignClient {

    /**
     * Feign 单文件上传
     *
     * @param file      文件对象
     * @param directory 存储目录
     * @return 文件 URL
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("directory") String directory
    );

    /**
     * Feign 删除文件
     *
     * @param fileUrl 文件 URL
     * @return 操作结果
     */
    @DeleteMapping
    Result<Void> delete(@RequestParam("fileUrl") String fileUrl);

    /**
     * Feign 获取预签名下载 URL
     *
     * @param fileName      文件名
     * @param directory     存储目录
     * @param expiryMinutes 过期时间（分钟）
     * @return 预签名下载 URL
     */
    @GetMapping("/download-url")
    Result<String> getPresignedDownloadUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam("directory") String directory,
            @RequestParam("expiryMinutes") int expiryMinutes
    );
}
