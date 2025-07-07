package hud.app.event_management.exceptions.handlers;

import hud.app.event_management.exceptions.ApiError;
import hud.app.event_management.exceptions.UnauthenticatedAccessException;
import hud.app.event_management.exceptions.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(UnauthenticatedAccessException.class)
    public ResponseEntity<ApiError> handleException(UnauthenticatedAccessException e, HttpServletRequest request){
        ApiError apiError =  new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiError> handleException(UnauthorizedAccessException e, HttpServletRequest request){
        ApiError apiError =  new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}
