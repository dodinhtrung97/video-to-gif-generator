package service;

public interface FileService {

    /**
     * Convert movie to jpg (predefined dest folder)
     * @param moviePath
     * @param frameJump
     */
    void convertMovieToJpg(String moviePath, int frameJump) throws Exception;

    /**
     * Convert existing frames to gif
     * @param framePath
     * @param targetFileName gif name input
     */
    void convertFramesToGif(String framePath, String targetFileName);
}
