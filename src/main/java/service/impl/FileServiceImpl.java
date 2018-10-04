package service.impl;

import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.imgscalr.Scalr;
import service.FileService;
import util.Constant;
import util.GifSequenceWriter;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileServiceImpl implements FileService {

    /**
     * Download video file
     * @param host
     * @param bucketName
     * @param objectName
     */
    public String downloadFile(String host, String bucketName, String objectName) {
        System.out.println("Downloading video");
        URL url = null;

        try {
            url = new URL("http://" + host + ":" + Constant.PORT +
                                File.separator + bucketName +
                                File.separator + objectName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String downloadDirUnparsed = Constant.BASE_PATH + bucketName;
        File targetFile = new File(downloadDirUnparsed + File.separator + objectName);

        File downloadDir = new File(downloadDirUnparsed);

        if (!downloadDir.exists())
            downloadDir.mkdirs();

        try {
            FileUtils.copyURLToFile(url, targetFile);
            return targetFile.getAbsolutePath();
        } catch (IOException e) {
            System.out.println("Failed to download video");
        }
        return "";
    }

    /**
     * Convert movie to multiple jpeg frames
     * @param destBucketName
     * @param videoPath
     * @param frameJump number of frames to skip per recorded frame
     */
    public void convertVideoToJpg(String destBucketName, String videoPath, int frameJump) throws Exception {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoPath);

        frameGrabber.start();
        Frame frame;
        int imgNum = 0;
        int startRecordingAt;

        String tempWorkDirUnparsed = Constant.BASE_PATH + destBucketName + File.separator + Constant.IMAGE_PATH;
        File tempWorkDir = new File(tempWorkDirUnparsed);
        if (!tempWorkDir.exists())
            tempWorkDir.mkdir();

        // Convert only a few frames from video (900)
        if (frameGrabber.getLengthInFrames() > 900)
            startRecordingAt = (frameGrabber.getLengthInFrames()*2) / 3;
        else
            startRecordingAt = 1;

        System.out.println("Video has " + frameGrabber.getLengthInFrames() +
                            " frames and " + frameGrabber.getFrameRate() + " fps");

        try {
            for (int i = startRecordingAt; i <= frameGrabber.getLengthInFrames(); i++) {
                imgNum++;
                frameGrabber.setFrameNumber(i);
                frame = frameGrabber.grab();

                BufferedImage bufferedImage = this.scaleFrame(420, 360, converter.convert(frame));

                String framePath = tempWorkDirUnparsed + imgNum + Constant.IMAGE_SUFFIX;
                ImageIO.write(bufferedImage, Constant.IMAGE_TYPE, new File(framePath));
                i += frameJump;

                if (imgNum >= 200) break;
            }
            frameGrabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert existing frames to gif
     * @param destBucketName
     * @param targetFileName gif name input
     */
    public String convertFramesToGif(String destBucketName, String targetFileName) {
        String framePath = Constant.BASE_PATH + destBucketName + File.separator + Constant.IMAGE_PATH;
        String targetDir = Constant.BASE_PATH + destBucketName;

        File frameFolder = new File(framePath);
        File targetGif = new File(targetDir + File.separator + targetFileName);
        List<BufferedImage> bufferedImageList = new ArrayList<BufferedImage>();

        File[] frames = frameFolder.listFiles();

        try {
            for (File frame : frames) {
                if (frame.isFile()) {
                    BufferedImage currentFrame = ImageIO.read(frame);
                    bufferedImageList.add(currentFrame);
                }
            }

            int intImageType = bufferedImageList.get(0).getType();
            ImageOutputStream output = new FileImageOutputStream(targetGif);
            GifSequenceWriter writer = new GifSequenceWriter(output, intImageType, 0, false);

            for (BufferedImage frame : bufferedImageList)
                writer.writeToSequence(frame);

            // Remove temp frames folder
            FileUtils.deleteDirectory(frameFolder);

            writer.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return targetDir + File.separator + targetFileName;
    }

    /**
     * Scale collected frame
     * @param scaleX
     * @param scaleY
     * @param frame
     * @return
     */
    private BufferedImage scaleFrame(int scaleX, int scaleY, BufferedImage frame) {
        BufferedImage bufferedImage = Scalr.resize(frame, Scalr.Method.BALANCED, scaleX, scaleY);

        return bufferedImage;
    }
}
