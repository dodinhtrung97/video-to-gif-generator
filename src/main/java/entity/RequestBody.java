package entity;

public class RequestBody {

    private String srcHost;

    private String srcBucketName;

    private String srcObjectName;

    private String destHost;

    private String destBucketName;

    private String destObjectName;

    public RequestBody (String srcHost, String srcBucketName, String srcObjectName,
                        String destHost, String destBucketName, String destObjectName) {
        this.srcHost = srcHost;
        this.srcBucketName = srcBucketName;
        this.srcObjectName = srcObjectName;
        this.destHost = destHost;
        this.destBucketName = destBucketName;
        this.destObjectName = destObjectName;
    }

    public String getSrcHost() {
        return srcHost;
    }

    public void setSrcHost(String srcHost) {
        this.srcHost = srcHost;
    }

    public String getSrcBucketName() {
        return srcBucketName;
    }

    public void setSrcBucketName(String srcBucketName) {
        this.srcBucketName = srcBucketName;
    }

    public String getSrcObjectName() {
        return srcObjectName;
    }

    public void setSrcObjectName(String srcObjectName) {
        this.srcObjectName = srcObjectName;
    }

    public String getDestHost() {
        return destHost;
    }

    public void setDestHost(String destHost) {
        this.destHost = destHost;
    }

    public String getDestBucketName() {
        return destBucketName;
    }

    public void setDestBucketName(String destBucketName) {
        this.destBucketName = destBucketName;
    }

    public String getDestObjectName() {
        return destObjectName;
    }

    public void setDestObjectName(String destObjectName) {
        this.destObjectName = destObjectName;
    }
}
