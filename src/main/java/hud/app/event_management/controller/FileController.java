package hud.app.event_management.controller;

import hud.app.event_management.annotations.loggedUser.LoggedUser;
import hud.app.event_management.model.EntityType;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.utils.FileUtil;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.responseUtils.ResponseCode;
import jakarta.activation.MimetypesFileTypeMap;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileUtil fileUtil;
    private final UserAccountRepository userAccountRepository;

    public FileController(FileUtil fileUtil, UserAccountRepository userAccountRepository) {
        this.fileUtil = fileUtil;
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping("/try")
    private UserAccount test() throws FileNotFoundException {
//        return userAccountRepository.findById(1221L).orElseThrow(FileNotFoundException::new);
        throw new FileNotFoundException("ok");
    }

    @GetMapping("{entityType}/{filename}")
    private ResponseEntity<byte[]> getUserPhoto(@PathVariable("filename") String filename, @PathVariable("entityType") String entityType, @LoggedUser UserAccount userAccount){
        Optional<EntityType> optionalType = Arrays.stream(EntityType.values()).filter(e -> entityType.equalsIgnoreCase(e.toString())).findFirst();

        if (optionalType.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        EntityType type  = optionalType.get();

        if (type.equals(EntityType.USERACCOUNT)){
            if (userAccount == null){
                return ResponseEntity.status(403).build();
            }
        }
        String path = "/file/" + entityType + "/" + filename;
        var fileUpload = fileUtil.findByPath(path);

        if (fileUpload.isPresent()) {
            return buildFileResponse(Path.of(fileUpload.get().getServerPath()), filename);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("upload/{entityType}")
    @PreAuthorize("isAuthenticated()")
    private Response<String> upload(@PathVariable("entityType") String entityType, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<EntityType> optionalType = Arrays.stream(EntityType.values()).filter(e -> entityType.equalsIgnoreCase(e.toString())).findFirst();

        if (optionalType.isEmpty()){
            return new Response<>(true, "Entity type is not found", ResponseCode.NO_RECORD_FOUND);
        }

        EntityType type  = optionalType.get();
        String path =  fileUtil.saveFile(type, file).getPath();

        return new Response<>(false, path, ResponseCode.SUCCESS);
    }

    private ResponseEntity<byte[]> buildFileResponse(Path path, String filename) {
        try {

            byte[] file = fileUtil.getFileForPath(path);
            if (file == null) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();

            // attachment will force the user to download the file
            String lowerCaseFilename = filename.toLowerCase();
            String contentType = lowerCaseFilename.endsWith("htm") || lowerCaseFilename.endsWith("html") || lowerCaseFilename.endsWith("svg") || lowerCaseFilename.endsWith("svgz")
                    ? "attachment"
                    : "inline";
            headers.setContentDisposition(ContentDisposition.builder(contentType).build());


            var response = ResponseEntity.ok().headers(headers).contentType(getMediaTypeFromFilename(filename)).header("filename", filename);

            return response.body(file);
        }
        catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private MediaType getMediaTypeFromFilename(String filename) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(filename);
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        }
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();

        return MediaType.parseMediaType(fileTypeMap.getContentType(filename));
    }
}
