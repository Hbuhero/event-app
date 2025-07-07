package hud.app.event_management.utils;

import hud.app.event_management.configurations.ApplicationConfigurations;
import hud.app.event_management.exceptions.FileCreationException;
import hud.app.event_management.exceptions.InvalidFileExtensionException;
import hud.app.event_management.model.EntityType;
import hud.app.event_management.model.FileUpload;
import hud.app.event_management.repository.FileRepository;
import jakarta.mail.Multipart;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class FileUtil {

    private final ApplicationConfigurations applicationConfigurations;
    private final FileRepository fileRepository;
    private static final Set<String> allowedFileExtensions = Set.of("png", "jpg", "jpeg", "gif", "svg");

    public FileUtil(ApplicationConfigurations applicationConfigurations, FileRepository fileRepository) {
        this.applicationConfigurations = applicationConfigurations;
        this.fileRepository = fileRepository;
    }

    public FileUpload saveFile(EntityType entityType, MultipartFile file) throws IOException {
        String filename = sanitizeFilename(file.getOriginalFilename());

        validateFileExtension(filename);

        String updatedFilename = entityType.toString() + "_" + UUID.randomUUID();
        Path fileServerPath = resolvePath(entityType, updatedFilename);

        copyFile(file, fileServerPath);

        String path = URI.create("/file/" + entityType.toString().toLowerCase() + "/").resolve(updatedFilename).toString();

        return fileRepository.save(
                new FileUpload(
                path,
                fileServerPath.toString(),
                updatedFilename,
                entityType
        ));
    }

    public Optional<FileUpload> findByPath(String path) {
        return fileRepository.findByPath(path);
    }

    public String sanitizeFilename(String filename){
        if (filename == null) {
            throw new InvalidFileExtensionException("Filename cannot be null");
        }
        return filename.replaceAll("[^a-zA-Z\\d.\\-]", "_").replaceAll("\\.+", ".");
    }

    public void validateFileExtension(String filename){
        final String fileExtension = FilenameUtils.getExtension(filename);

        if (allowedFileExtensions.stream().noneMatch(fileExtension::equalsIgnoreCase)){
            throw new InvalidFileExtensionException("Invalid file extension: " + fileExtension);
        }

    }

    private void copyFile(MultipartFile file, Path filePath) {
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), filePath.toFile());
        }
        catch (IOException e) {
            throw new FileCreationException("Could not create file");
        }
    }

    public Path resolvePath(EntityType entityType, String filename) throws IOException {
        Path basePath = this.applicationConfigurations.getBasePath().resolve(entityType.toString());

        if (!Files.exists(basePath)){
            Files.createDirectory(basePath);
        }

        return basePath.resolve(filename);
    }

    public byte[] getFileForPath(Path path) throws IOException {
        if (Files.exists(path)) {
            return Files.readAllBytes(path);
        }
        return null;
    }

    public void setFileStatusIsDeleted(FileUpload fileUpload) {
        fileRepository.delete(fileUpload);
    }
}
