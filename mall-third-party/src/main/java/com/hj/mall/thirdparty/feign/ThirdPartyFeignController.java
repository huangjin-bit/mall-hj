package com.hj.mall.thirdparty.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.thirdparty.service.OSSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 第三方服务 Feign 控制器
 * 专门用于 Feign 内部调用的接口路径：/feign/oss
 */
@Slf4j
@RestController
@RequestMapping("/feign/oss")
@RequiredArgsConstructor
public class ThirdPartyFeignController {

    private final OSSService ossService;

    /**
     * Feign 单文件上传
     */
    @PostMapping("/upload")
    public Result<String> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("directory") String directory) {

        try {
            log.info("[FeignController] Feign 文件上传请求: 文件名={}, 目录={}",
                    file.getOriginalFilename(), directory);

            // 参数校验
            if (file.isEmpty()) {
                return Result.validateFailed("文件不能为空");
            }

            String fileUrl = ossService.upload(file, directory);
            return Result.success(fileUrl);

        } catch (IllegalArgumentException e) {
            log.warn("[FeignController] 参数校验失败: {}", e.getMessage());
            return Result.validateFailed(e.getMessage());
        } catch (Exception e) {
            log.error("[FeignController] Feign 文件上传失败", e);
            return Result.failed("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * Feign 删除文件
     */
    @DeleteMapping
    public Result<Void> delete(@RequestParam("fileUrl") String fileUrl) {

        try {
            log.info("[FeignController] Feign 删除文件请求: url={}", fileUrl);

            // 参数校验
            if (fileUrl == null || fileUrl.trim().isEmpty()) {
                return Result.validateFailed("文件 URL 不能为空");
            }

            ossService.delete(fileUrl);
            return Result.success();

        } catch (IllegalArgumentException e) {
            log.warn("[FeignController] 参数校验失败: {}", e.getMessage());
            return Result.validateFailed(e.getMessage());
        } catch (Exception e) {
            log.error("[FeignController] Feign 文件删除失败", e);
            return Result.failed("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * Feign 获取预签名下载 URL
     */
    @GetMapping("/download-url")
    public Result<String> getPresignedDownloadUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam("directory") String directory,
            @RequestParam("expiryMinutes") int expiryMinutes) {

        try {
            log.info("[FeignController] Feign 获取预签名下载 URL: 文件名={}, 目录={}, 过期时间={} 分钟",
                    fileName, directory, expiryMinutes);

            // 参数校验
            if (fileName == null || fileName.trim().isEmpty()) {
                return Result.validateFailed("文件名不能为空");
            }

            if (expiryMinutes <= 0 || expiryMinutes > 1440) {
                return Result.validateFailed("过期时间必须在 1-1440 分钟之间");
            }

            String downloadUrl = ossService.getPresignedDownloadUrl(fileName, directory, expiryMinutes);
            return Result.success(downloadUrl);

        } catch (IllegalArgumentException e) {
            log.warn("[FeignController] 参数校验失败: {}", e.getMessage());
            return Result.validateFailed(e.getMessage());
        } catch (Exception e) {
            log.error("[FeignController] Feign 获取预签名下载 URL 失败", e);
            return Result.failed("获取预签名下载 URL 失败: " + e.getMessage());
        }
    }
}
