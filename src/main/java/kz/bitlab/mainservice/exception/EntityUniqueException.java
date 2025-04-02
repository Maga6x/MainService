package kz.bitlab.mainservice.exception;

public class EntityUniqueException extends RuntimeException {

    public EntityUniqueException() {
        super();
    }

    public EntityUniqueException(String message) {
        super(message);
    }
}
