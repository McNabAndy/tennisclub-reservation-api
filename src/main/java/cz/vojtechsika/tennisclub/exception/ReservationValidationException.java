package cz.vojtechsika.tennisclub.exception;

public class ReservationValidationException extends RuntimeException {
    public ReservationValidationException(String message) {
        super(message);
    }
}
