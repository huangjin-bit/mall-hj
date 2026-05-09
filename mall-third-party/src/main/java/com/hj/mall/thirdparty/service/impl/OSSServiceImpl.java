package com.hj.mall.thirdparty.service.impl;

import com.hj.mall.thirdparty.config.MinioConfig;
import com.hj.mall.thirdparty.service.OSSService;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 对象存储服务实现
 * 基于 MinIO 实现文件上传、下载、删除等功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OSSServiceImpl implements OSSService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    /**
     * 服务启动时检查并创建存储桶
     */
    @PostConstruct
    public void init() {
        try {
            String bucketName = minioConfig.getBucketName();
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (!exists) {
                // 创建存储桶
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                log.info("[OSS] 存储桶创建成功: {}", bucketName);
            } else {
                log.info("[OSS] 存储桶已存在: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("[OSS] 存储桶初始化失败", e);
            throw new RuntimeException("存储桶初始化失败", e);
        }
    }

    @Override
    public String upload(MultipartFile file, String directory) {
        try {
            // 获取原始文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("文件名不能为空");
            }

            String extension = getFileExtension(originalFilename);
            // 生成唯一文件名：UUID + 扩展名
            String fileName = UUID.randomUUID() + extension;

            // 构建对象名称（带目录前缀）
            String objectName = buildObjectName(directory, fileName);

            // 上传文件
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("[OSS] 文件上传成功: {}", objectName);
            return buildFileUrl(objectName);

        } catch (Exception e) {
            log.error("[OSS] 文件上传失败: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadBytes(byte[] data, String fileName, String contentType, String directory) {
        try {
            String objectName = buildObjectName(directory, fileName);

            // 上传字节数组
            InputStream inputStream = new java.io.ByteArrayInputStream(data);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(inputStream, data.length, -1)
                            .contentType(contentType)
                            .build()
            );

            log.info("[OSS] 字节数组上传成功: {}", objectName);
            return buildFileUrl(objectName);

        } catch (Exception e) {
            log.error("[OSS] 字节数组上传失败: {}", fileName, e);
            throw new RuntimeException("字节数组上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> uploadBatch(MultipartFile[] files, String directory) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String url = upload(file, directory);
                urls.add(url);
            } catch (Exception e) {
                log.error("[OSS] 批量上传中单个文件失败: {}", file.getOriginalFilename(), e);
                // 继续上传其他文件，不中断整个批量操作
            }
        }

        log.info("[OSS] 批量上传完成，成功: {}/{}", urls.size(), files.length);
        return urls;
    }

    @Override
    public void delete(String fileUrl) {
        try {
            // 从 URL 中提取对象名称
            String objectName = extractObjectName(fileUrl);
            if (objectName == null) {
                throw new IllegalArgumentException("无效的文件 URL");
            }

            // 删除对象
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );

            log.info("[OSS] 文件删除成功: {}", objectName);

        } catch (Exception e) {
            log.error("[OSS] 文件删除失败: {}", fileUrl, e);
            throw new RuntimeException("文件删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getPresignedUploadUrl(String fileName, String directory, int expiryMinutes) {
        try {
            String objectName = buildObjectName(directory, fileName);

            // 生成预签名上传 URL
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .expiry(expiryMinutes, TimeUnit.MINUTES)
                            .build()
            );

            log.info("[OSS] 生成预签名上传 URL: {}, 过期时间: {} 分钟", objectName, expiryMinutes);
            return url;

        } catch (Exception e) {
            log.error("[OSS] 生成预签名上传 URL 失败: {}", fileName, e);
            throw new RuntimeException("生成预签名 URL 失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getPresignedDownloadUrl(String fileName, String directory, int expiryMinutes) {
        try {
            String objectName = buildObjectName(directory, fileName);

            // 生成预签名下载 URL
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .expiry(expiryMinutes, TimeUnit.MINUTES)
                            .build()
            );

            log.info("[OSS] 生成预签名下载 URL: {}, 过期时间: {} 分钟", objectName, expiryMinutes);
            return url;

        } catch (Exception e) {
            log.error("[OSS] 生成预签名下载 URL 失败: {}", fileName, e);
            throw new RuntimeException("生成预签名下载 URL 失败: " + e.getMessage(), e);
        }
    }

    /**
     * 构建对象名称（包含目录前缀）
     * 例如：product/2024/01/15/uuid.jpg
     */
    private String buildObjectName(String directory, String fileName) {
        // 添加日期分层：目录/年/月/日/文件名
        String datePath = getDatePath();
        return String.format("%s/%s/%s", directory, datePath, fileName);
    }

    /**
     * 生成日期路径：年/月/日
     */
    private String getDatePath() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%d/%02d/%02d", year, month, day);
    }

    /**
     * 构建文件访问 URL
     */
    private String buildFileUrl(String objectName) {
        return minioConfig.getEndpoint() + "/" +
               minioConfig.getBucketName() + "/" +
               objectName;
    }

    /**
     * 从 URL 中提取对象名称
     * 例如：http://localhost:9000/mall/product/2024/01/15/uuid.jpg -> product/2024/01/15/uuid.jpg
     */
    private String extractObjectName(String fileUrl) {
        try {
            String baseUrl = minioConfig.getEndpoint() + "/" +
                           minioConfig.getBucketName() + "/";

            if (fileUrl.startsWith(baseUrl)) {
                return fileUrl.substring(baseUrl.length());
            }

            return null;
        } catch (Exception e) {
            log.error("[OSS] 提取对象名称失败: {}", fileUrl, e);
            return null;
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex);
        }
        return "";
    }
}
