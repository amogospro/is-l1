package com.lab1.lab1.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;

@ApplicationScoped
public class MinIOService {
    private final MinioClient minioClient;

    public MinIOService() {
        minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("admin", "admin123")
                .build();
    }

    public void uploadFile(String bucketName, String fileName, InputStream stream) throws Exception {
        try {
            // Создаем bucket, если его нет
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Загружаем файл
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(stream, stream.available(), -1)
                    .contentType("application/json")
                    .build());
        } catch (Exception e) {
            throw new Exception("Ошибка при загрузке файла в MinIO", e);
        }
    }

    public boolean bucketExists(String bucketName) throws Exception {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new Exception("Ошибка при проверке существования бакета в MinIO", e);
        }
    }

    public boolean fileExists(String bucketName, String fileName) throws Exception {
        try {
            Iterable<Result<Item>> items = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> item : items) {
                if (item.get().objectName().equals(fileName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Ошибка при проверке существования файла в MinIO", e);
        }
    }

    public void deleteFile(String bucketName, String fileName) throws Exception {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            throw new Exception("Ошибка при удалении файла из MinIO", e);
        }
    }

}
