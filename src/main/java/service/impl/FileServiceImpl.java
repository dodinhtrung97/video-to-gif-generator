package service.impl;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.imgscalr.Scalr;
import service.FileService;
import util.GifSequenceWriter;
import util.Static;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileServiceImpl implements FileService {

    /**
     * Convert movie to multiple jpeg frames
     * @param moviePath
     * @param frameJump number of frames to skip per recorded frame
     */
    public void convertMovieToJpg(String moviePath, int frameJump) throws Exception {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(moviePath);

        frameGrabber.start();

        Frame frame;
        int imgNum = 0;
        int startRecordingAt;

        if (frameGrabber.getLengthInFrames() > 900)
            startRecordingAt = (frameGrabber.getLengthInFrames()*2) / 3;
        else
            startRecordingAt = 0;

        System.out.println("Video has " + frameGrabber.getLengthInFrames() +
                            " frames and " + frameGrabber.getFrameRate() + " fps");

        try {
            for (int i = startRecordingAt; i <= frameGrabber.getLengthInFrames(); i++) {
                imgNum++;
                frameGrabber.setFrameNumber(i);
                frame = frameGrabber.grab();

                BufferedImage bufferedImage = this.scaleFrame(420, 360, converter.convert(frame));

                String framePath = Static.IMAGE_PATH + File.separator + imgNum + Static.IMAGE_SUFFIX;
                ImageIO.write(bufferedImage, Static.IMAGE_TYPE, new File(framePath));
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
     * @param framePath
     * @param targetFileName gif name input
     */
    public void convertFramesToGif(String framePath, String targetFileName) {
        File frameFolder = new File(framePath);
        File targetGif = new File(Static.IMAGE_PATH + targetFileName);
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

            writer.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
