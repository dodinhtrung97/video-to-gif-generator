package service;

import java.io.IOException;

public interface FileUploadService {

    /**
     * Send create bucket request
     * @param destHost
     * @param destBucketName
     * @param destObjectName
     */
    void createBucket(String destHost, String destBucketName, String destObjectName) throws IOException;

    /**
     * Send create upload ticket request
     * @param destHost
     * @param destBucketName
     * @param destObjectName
     */
    void createUploadTicket(String destHost, String destBucketName, String destObjectName) throws IOException;

    /**
     * Send complete upload request
     * @param destHost
     * @param destBucketName
     * @param destObjectName
     */
    void completeUpload(String destHost, String destBucketName, String destObjectName) throws IOException;

    /**
     * Send file upload request
     * @param filePath
     * @param destHost
     * @param destBucketName
     * @param destObjectName
     */
    void uploadFile(String filePath, String destHost, String destBucketName, String destObjectName) throws IOException;
}
