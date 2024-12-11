package com.lab1.lab1.MinIOTransaction;

import com.lab1.lab1.service.MinIOService;

import java.io.InputStream;

public class MinIOTransactionalResource implements TransactionalResource {
    private final MinIOService minioService;
    private final String bucketName;
    private final String fileName;
    private final InputStream fileContent;
    private boolean prepared = false;

    public MinIOTransactionalResource(MinIOService minioService, String bucketName, String fileName, InputStream fileContent) {
        this.minioService = minioService;
        this.bucketName = bucketName;
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    @Override
    public void prepare() throws Exception {
        // Проверяем доступность MinIO (например, существование бакета)
        if (!minioService.bucketExists(bucketName)) {
            throw new Exception("Bucket " + bucketName + " does not exist.");
        }
        prepared = true; // Отмечаем, что MinIO готово
    }

    @Override
    public void commit() throws Exception {
        if (prepared) {
            minioService.uploadFile(bucketName, fileName, fileContent);
        } else {
            throw new IllegalStateException("MinIO resource not prepared!");
        }
    }

    @Override
    public void rollback() {
        try {
            if (minioService.fileExists(bucketName, fileName)) {
                minioService.deleteFile(bucketName, fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
