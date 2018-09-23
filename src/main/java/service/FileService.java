package service;

public interface FileService {

    /**
     * Convert movie to jpg (predefined dest folder)
     * @param moviePath
     * @param frameJump
     */
    void convertMovieToJpg(String moviePath, int frameJump) throws Exception;
}
