package hud.app.event_management.exceptions;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String message){
        super(message);
    }
}
