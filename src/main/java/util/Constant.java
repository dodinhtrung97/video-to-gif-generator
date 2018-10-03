package util;

import java.io.File;

public class Constant {

    public static String BASE_PATH = new File("").getAbsolutePath() + File.separator + "temp" + File.separator;

    public static String IMAGE_PATH = "frames" + File.separator;

    public static String IMAGE_TYPE = "jpg";

    public static String IMAGE_SUFFIX = ".jpg";

    public static String PORT = "8080";

    public static String QUEUE_NAME = "task_queue";
}
