import org.apache.commons.io.FileUtils;
import service.FileService;
import service.impl.FileServiceImpl;
import util.Static;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainApplication {

    public static void main(String[] args) throws IOException {
        FileService fileService = new FileServiceImpl();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();

        String[] inputs = line.trim().split("\\s+");
        String sourceFile = inputs[0];
        String outputGif = inputs[1];

        File imageFolder = new File(Static.IMAGE_PATH);

        try {
            // For testing purposes
            FileUtils.cleanDirectory(imageFolder);
            fileService.convertMovieToJpg(sourceFile, 1);
            fileService.convertFramesToGif(Static.IMAGE_PATH, outputGif);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
