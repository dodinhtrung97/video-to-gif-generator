import org.apache.commons.io.FileUtils;
import service.FileService;
import service.impl.FileServiceImpl;
import util.Static;

import java.io.File;

public class MainApplication {

    public static void main(String[] args) {
        FileService fileService = new FileServiceImpl();

        File imageFolder = new File(Static.IMAGE_PATH);

        try {
            // For testing purposes
            FileUtils.cleanDirectory(imageFolder);
            fileService.convertMovieToJpg("D:\\Mahidol\\proj1-test.mp4", 0);
            fileService.convertFramesToGif(Static.IMAGE_PATH, "test.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
