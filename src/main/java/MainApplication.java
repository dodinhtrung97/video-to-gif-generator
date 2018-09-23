import service.FileService;
import service.impl.FileServiceImpl;

public class MainApplication {

    public static void main(String[] args) {
        FileService fileService = new FileServiceImpl();

        try {
            fileService.convertMovieToJpg("D:\\Mahidol\\proj1-test.mp4", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
