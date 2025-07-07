package hud.app.event_management.exceptions.handlers;

import hud.app.event_management.exceptions.ApiError;
import hud.app.event_management.exceptions.FileCreationException;
import hud.app.event_management.exceptions.InvalidFileExtensionException;
import hud.app.event_management.exceptions.InvalidFilenameException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

@ControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiError> handleException(FileNotFoundException e, HttpServletRequest request){
        ApiError apiError = new ApiError(
                null,
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        System.out.println(apiError);

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);

    }



    @ExceptionHandler(FileCreationException.class)
    public ResponseEntity<ApiError> handleException(FileCreationException e, HttpServletRequest request){
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidFilenameException.class)
    public ResponseEntity<ApiError> handleException(InvalidFilenameException e, HttpServletRequest request){
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidFileExtensionException.class)
    public ResponseEntity<ApiError> handleException(InvalidFileExtensionException e, HttpServletRequest request){
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
