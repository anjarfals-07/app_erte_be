package com.apps.erte.service.minio;

import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;

    public void uploadObject(String bucketName, String objectName, String filePath) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new FileInputStream(filePath), -1, ObjectWriteArgs.MIN_MULTIPART_SIZE)
                            .build()
            );
        } catch (Exception e) {
            // Handle exceptions
        }
    }

    public void downloadObject(String bucketName, String objectName, String destinationFilePath) {
        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(destinationFilePath)
                            .build()
            );
        } catch (Exception e) {
            // Handle exceptions
        }
    }

    public void deleteObject(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            // Handle exceptions
        }
    }

    public void createBucket(String bucketName) {
        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        } catch (Exception e) {
            // Handle exceptions
        }
    }
    public Iterable<Result<Item>> listObjects(String bucketName) {
        try {
            return minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        } catch (Exception e) {
            // Handle exceptions
            return null;
        }
    }
}
