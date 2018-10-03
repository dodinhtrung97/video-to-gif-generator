package service;

public interface FileService {

    /**
     * Download video file
     * @param host
     * @param bucketName
     * @param objectName
     * @return downloaded file directory
     */
    String downloadFile(String host, String bucketName, String objectName);

    /**
     * Convert movie to multiple jpeg frames
     * @param destBucketName
     * @param videoPath
     * @param frameJump number of frames to skip per recorded frame
     */
    public void convertVideoToJpg(String destBucketName, String videoPath, int frameJump) throws Exception;

    /**
     * Convert existing frames to gif
     * @param destBucketName
     * @param targetFileName gif name input
     */
    public void convertFramesToGif(String destBucketName, String targetFileName);
}
