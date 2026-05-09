package com.hj.mall.thirdparty.controller;

import com.hj.mall.common.api.Result;
import com.hj.mall.thirdparty.service.OSSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象存储控制器
 * 提供文件上传、下载、删除等 REST API
 */
@Slf4j
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OssController {

    private final OSSService ossService;

    /**
     * 单文件上传
     *
     * @param file      上传的文件
     * @param directory 存储目录（如：product、avatar、document 等）
     * @return 文件访问 URL
     */
    @PostMapping("/upload")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", defaultValue = "default") String directory) {

        try {
            log.info("[OssController] 单文件上传请求: 文件名={}, 目录={}", file.getOriginalFilename(), directory);

            // 参数校验
            if (file.isEmpty()) {
                return Result.validateFailed("文件不能为空");
            }

            String fileUrl = ossService.upload(file, directory);
            return Result.success(fileUrl);

        } catch (IllegalArgumentException e) {
            log.warn("[OssController] 参数校验失败: {}", e.getMessage());
            return Result.validateFailed(e.getMessage());
        } catch (Exception e) {
            log.error("[OssController] 文件上传失败", e);
            return Result.failed("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 批量文件上传
     *
     * @param files     文件数组
     * @param directory 存储目录
     * @return 文件 URL 列表
     */
    @PostMapping("/upload/batch")
    public Result<List<String>> uploadBatch(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "directory", defaultValue = "default") String directory) {

        try {
            log.info("[OssController] 批量文件上传请求: 文件数量={}, 目录={}", files.length, directory);

            // 参数校验
            if (files == null || files.length == 0) {
                return Result.validateFailed("文件列表不能为空");
            }

            if (files.length > 10) {
                return Result.validateFailed("单次最多上传 10 个文件");
            }

            List<String> urls = ossService.uploadBatch(files, directory);
            return Result.success(urls);

        } catch (IllegalArgumentException e) {
            log.warn("[OssController] 参数校验失败: {}", e.getMessage());
            return Result.validateFailed(e.getMessage());
        } catch (Exception e) {
            log.error("[OssController] 批量文件上传失败", e);
            return Result.failed("批量文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件 URL
     * @return 操作结果
     */
    @DeleteMapping
    public Result<Void> delete(@RequestParam String fileUrl) {

        try {
            log.info("[OssController] 删除文件请求: url={}", fileUrl);

            // 参数校验
            if (fileUrl == null || fileUrl.trim().isEmpty()) {
                return Result.validateFailed("文件 URL 不能为空");
            }

            ossService.delete(fileUrl);
            return Result.success();

        } catch (IllegalArgumentException e) {
            log.warn("[OssController] 参数校验失败: {}", e.getMessage());
            return Result.validateFailed(e.getMessage());
        } catch (Exception e) {
            log.error("[OssController] 文件删除失败", e);
            return Result.failed("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取上传策略（预签名 URL）
     * 用于客户端直接上传到 MinIO，不经过服务端
     *
     * @param fileName      文件名
     * @param directory     存储目录
     * @param expiryMinutes 过期时间（分钟），默认 15 分钟
     * @return 包含预签名 URL 和相关参数的 Map
     */
    @GetMapping("/policy")
    public Result<Map<String, String>> getUploadPolicy(
            @RequestParam("fileName") String fileName,
            @RequestParam(value = "directory", defaultValue = "default") String directory,
            @RequestParam(value = "expiryMinutes", defaultValue = "15") int expiryMinutes) {

        try {
            log.info("[OssController] 获取上传策略: 文件名={}, 目录={}, 过期时间={} 分钟",
                    fileName, directory, expiryMinutes);

            // 参数校验
            if (fileName == null || fileName.trim().isEmpty()) {
                return Result.validateFailed("文件名不能为空");
            }

            if (expiryMinutes <= 0 || expiryMinutes > 60) {
                return Result.validateFailed("过期时间必须在 1-60 分钟之间");
            }

            // 生成预签名上传 URL
            String presignedUrl = ossService.getPresignedUploadUrl(fileName, directory, expiryMinutes);

            // 构建返回参数
            Map<String, String> policy = new HashMap<>();
            policy.put("uploadUrl", presignedUrl);
            policy.put("fileName", fileName);
            policy.put("directory", directory);
            policy.put("expiryMinutes", String.valueOf(expiryMinutes));

            log.info("[OssController] 上传策略生成成功");
            return Result.success(policy);

        } catch (IllegalArgumentException e) {
            log.warn("[OssController] 参数校验失败: {}", e.getMessage());
            return Result.validateFailed(e.getMessage());
        } catch (Exception e) {
            log.error("[OssController] 获取上传策略失败", e);
            return Result.failed("获取上传策略失败: " + e.getMessage());
        }
    }

    /**
     * 获取预签名下载 URL
     * 用于临时授权下载私有文件
     *
     * @param fileName      文件名
     * @param directory     存储目录
     * @param expiryMinutes 过期时间（分钟）
     * @return 预签名下载 URL
     */
    @GetMapping("/download-url")
    public Result<String> getPresignedDownloadUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam(value = "directory", defaultValue = "default") String directory,
            @RequestParam(value = "expiryMinutes", defaultValue = "30") int expiryMinutes) {

        try {
            log.info("[OssController] 获取预签名下载 URL: 文件名={}, 目录={}, 过期时间={} 分钟",
                    fileName, directory, expiryMinutes);

            // 参数校验
            if (fileName == null || fileName.trim().isEmpty()) {
                return Result.validateFailed("文件名不能为空");
            }

            if (expiryMinutes <= 0 || expiryMinutes > 1440) {  // 最多 24 小时
                return Result.validateFailed("过期时间必须在 1-1440 分钟之间");
            }

            String downloadUrl = ossService.getPresignedDownloadUrl(fileName, directory, expiryMinutes);
            return Result.success(downloadUrl);

        } catch (IllegalArgumentException e) {
            log.warn("[OssController] 参数校验失败: {}", e.getMessage());
            return Result.validateFailed(e.getMessage());
        } catch (Exception e) {
            log.error("[OssController] 获取预签名下载 URL 失败", e);
            return Result.failed("获取预签名下载 URL 失败: " + e.getMessage());
        }
    }
}
