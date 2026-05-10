package com.hj.mall.thirdparty.feign;

import com.hj.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 第三方服务 Feign 降级处理
 * 当 mall-third-party 服务不可用时触发
 */
@Slf4j
@Component
public class ThirdPartyFeignFallback implements ThirdPartyFeignClient {

    @Override
    public Result<String> upload(MultipartFile file, String directory) {
        log.error("[ThirdPartyFeign] 文件上传服务降级: 文件名={}, 目录={}",
                file.getOriginalFilename(), directory);
        return Result.failed("第三方文件服务暂时不可用，请稍后重试");
    }

    @Override
    public Result<Void> delete(String fileUrl) {
        log.error("[ThirdPartyFeign] 文件删除服务降级: url={}", fileUrl);
        return Result.failed("第三方文件服务暂时不可用，请稍后重试");
    }

    @Override
    public Result<String> getPresignedDownloadUrl(String fileName, String directory, int expiryMinutes) {
        log.error("[ThirdPartyFeign] 获取预签名下载 URL 服务降级: 文件名={}, 目录={}",
                fileName, directory);
        return Result.failed("第三方文件服务暂时不可用，请稍后重试");
    }
}
