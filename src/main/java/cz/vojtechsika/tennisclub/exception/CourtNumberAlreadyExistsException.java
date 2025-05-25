package cz.vojtechsika.tennisclub.exception;

public class CourtNumberAlreadyExistsException extends RuntimeException {
    public CourtNumberAlreadyExistsException(String message) {
        super(message);
    }
}
