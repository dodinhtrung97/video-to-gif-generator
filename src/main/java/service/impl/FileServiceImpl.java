package service.impl;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import service.FileService;
import service.util.Static;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class FileServiceImpl implements FileService {

    /**
     * Convert movie to multiple jpeg frames
     * @param moviePath
     * @param frameJump
     */
    public void convertMovieToJpg(String moviePath, int frameJump) throws Exception {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(moviePath);

        frameGrabber.start();

        Frame frame;
        int imgNum = 0;
        System.out.println("Video has " + frameGrabber.getLengthInFrames() +
                            " frames and " + frameGrabber.getFrameRate() + " framerate");

        try {
            for (int i = 1; i <= frameGrabber.getLengthInFrames(); i++) {
                imgNum++;
                frameGrabber.setFrameNumber(i);
                frame = frameGrabber.grab();
                BufferedImage bufferedImage = converter.convert(frame);

                String path = Static.IMAGE_PATH + File.separator + imgNum + Static.IMAGE_SUFFIX;
                ImageIO.write(bufferedImage, Static.IMAGE_TYPE, new File(path));
                i += frameJump;
            }
            frameGrabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
