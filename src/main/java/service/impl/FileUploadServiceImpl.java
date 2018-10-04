package service.impl;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import service.FileUploadService;
import util.Constant;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUploadServiceImpl implements FileUploadService {

    /**
     * Create bucket request
     * @param destHost
     * @param destBucketName
     * @param destObjectName
     */
    @Override
    public void createBucket(String destHost, String destBucketName, String destObjectName) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://" + destHost + ":" + Constant.PORT + "/" + destBucketName + "?create");

        try {
            client.execute(post);
        } catch (ClientProtocolException e) {
            System.out.println("Failed to create bucket");
        }
    }

    /**
     * Create upload ticket request
     * @param destHost
     * @param destBucketName
     * @param destObjectName
     */
    @Override
    public void createUploadTicket(String destHost, String destBucketName, String destObjectName) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://" + destHost + ":" + Constant.PORT + "/" + destBucketName + "/" + destObjectName + "?create");

        try {
            client.execute(post);
        } catch (ClientProtocolException e) {
            System.out.println("Failed create upload ticket");
        }
    }

    /**
     * Complete upload request
     * @param destHost
     * @param destBucketName
     * @param destObjectName
     */
    @Override
    public void completeUpload(String destHost, String destBucketName, String destObjectName) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://" + destHost + ":" + Constant.PORT + "/" + destBucketName + "/" + destObjectName + "?complete");

        try {
            client.execute(post);
        } catch (ClientProtocolException e) {
            System.out.println("Failed create upload ticket");
        }
    }

    /**
     * Upload file request
     * @param filePath
     * @param destHost
     * @param destBucketName
     * @param destObjectName
     */
    @Override
    public void uploadFile(String filePath, String destHost, String destBucketName, String destObjectName) throws IOException {
        System.out.println("Uploading File");

        int partNumber = 1;
        long fileSize = 0;

        try {
            fileSize = Files.size(new File(filePath).toPath());
        } catch (IOException e) {
            System.out.println("File does not exist");
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut("http://" + destHost + ":" + Constant.PORT + "/" + destBucketName + "/" + destObjectName + "?partNumber=" + partNumber);

        File file = new File(filePath);
        MultipartEntity multipartEntity = new MultipartEntity();
        FileBody fileBody = new FileBody(file);
        multipartEntity.addPart("attachment", fileBody);

        request.setHeader("Content-Type", "application/octet-stream");
        request.setHeader("Content-Length", Long.toString(fileSize));
        request.setHeader("Content-MD5", this.getFileCheckSum(filePath));
        request.setEntity(multipartEntity);

        try {
            client.execute(request);
        } catch (ClientProtocolException e) {
            System.out.println("Failed to upload file");
        }

    }

    /**
     * Get file's md5
     * @param filePath
     * @return
     */
    private String getFileCheckSum(String filePath) throws IOException {
        String digestInHex = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(filePath)));

            byte[] digest = md.digest();
            digestInHex = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Failed to calculate file's checksum");
        }

        return digestInHex;
    }
}
