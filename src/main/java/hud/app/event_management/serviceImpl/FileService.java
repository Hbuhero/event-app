package hud.app.event_management.serviceImpl;

import hud.app.event_management.model.EventStatus;
import hud.app.event_management.model.FileUpload;
import hud.app.event_management.repository.FileRepository;
import hud.app.event_management.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Arrays;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileUpload createFileUpload(MultipartFile file, Long entityID, String entityType){
        try {
            String filename = FileUtil.sanitizeFilename(file.getOriginalFilename());

            FileUtil.validateFileExtension(file.getOriginalFilename());



        } catch (Exception e){

        }
    }
}
