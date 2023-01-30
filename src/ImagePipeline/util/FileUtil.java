package ImagePipeline.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class FileUtil {
    public static String join(String dirPath, String filePath) {
        String joinedPath = null;

        if (dirPath != null && filePath != null) {
            Path tmp_dirPath = Paths.get(dirPath);
            Path tmp_filePath = Paths.get(filePath);
            joinedPath = tmp_dirPath.resolve(tmp_filePath).toString();
        }

        return joinedPath;
    }

    public static String[] join(String dirPath, String[] relativePaths) {
        String[] absPaths = null;

        if (dirPath != null && relativePaths != null) {
            absPaths = new String[relativePaths.length];
            Path tmp_dirPath = Paths.get(dirPath);
            for (int i = 0; i < relativePaths.length; i++) {
                Path relativePath = Paths.get(relativePaths[i]);
                absPaths[i] = tmp_dirPath.resolve(relativePath).toString();
            }

        }

        return absPaths;
    }

    public static void mkdirs(String dirPath) {
        if (dirPath != null) {
            Logger logger = Logger.getLogger(FileUtil.class.getName());
            logger.setLevel(Common.GLOBAL_LOG_LEVEL);
            Path dir = Paths.get(dirPath);

            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                logger.warning("Failed to make directories.");
            }
        }
    }

    public static String dirPath(String filePath) {
        String dirPath = null;
        if (filePath != null) {
            Path file = Paths.get(filePath);
            dirPath = file.getParent().toString();
        }
        return dirPath;
    }
}
