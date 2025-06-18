package hud.app.event_management.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class FileUtil {

    private static final Set<String> allowedFileExtensions = Set.of("png", "jpg", "jpeg", "gif", "svg");

    public static String sanitizeFilename(String filename){
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }
        return filename.replaceAll("[^a-zA-Z\\d.\\-]", "_").replaceAll("\\.+", ".");
    }

    public static void validateFileExtension(String filename){
        final String fileExtension = FilenameUtils.getExtension(filename);

        if (allowedFileExtensions.stream().noneMatch(fileExtension::equalsIgnoreCase)){
            throw new IllegalStateException("Bad request, illegal file extension");
        }
    }

    private static void copyFile(MultipartFile file, Path filePath) {
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), filePath.toFile());
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not create file");
        }
    }

    public static Path saveFile(MultipartFile file, Path filePath) {
        copyFile(file, filePath);
        return filePath;
    }
}
