package com.hj.mall.thirdparty.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 对象存储服务接口
 * 提供文件上传、删除、预签名 URL 等功能
 */
public interface OSSService {

    /**
     * 上传文件到 MinIO
     *
     * @param file      文件对象
     * @param directory 存储目录（如：product、avatar 等）
     * @return 文件访问 URL
     */
    String upload(MultipartFile file, String directory);

    /**
     * 上传字节数组到 MinIO
     *
     * @param data        文件字节数组
     * @param fileName    文件名（包含扩展名）
     * @param contentType MIME 类型
     * @param directory   存储目录
     * @return 文件访问 URL
     */
    String uploadBytes(byte[] data, String fileName, String contentType, String directory);

    /**
     * 批量上传文件
     *
     * @param files     文件数组
     * @param directory 存储目录
     * @return 文件 URL 列表
     */
    List<String> uploadBatch(MultipartFile[] files, String directory);

    /**
     * 删除文件
     *
     * @param fileUrl 文件 URL（包含完整的对象名称）
     */
    void delete(String fileUrl);

    /**
     * 生成预签名上传 URL
     * 用于客户端直接上传到 MinIO，不经过服务端
     *
     * @param fileName      文件名
     * @param directory     存储目录
     * @param expiryMinutes 过期时间（分钟）
     * @return 预签名 URL 和相关参数
     */
    String getPresignedUploadUrl(String fileName, String directory, int expiryMinutes);

    /**
     * 生成预签名下载 URL
     * 用于临时授权下载私有文件
     *
     * @param fileName      文件名
     * @param directory     存储目录
     * @param expiryMinutes 过期时间（分钟）
     * @return 预签名下载 URL
     */
    String getPresignedDownloadUrl(String fileName, String directory, int expiryMinutes);
}
