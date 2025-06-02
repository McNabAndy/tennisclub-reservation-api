package cz.vojtechsika.tennisclub.exception;

/**
 * ReservationValidationException is thrown when a reservation fails validation rules.
 * This exception indicates that the provided reservation data does not meet the business constraints,
 * such as overlapping times, invalid time ranges, or exceeding allowed duration.
 * This exception extends {@link RuntimeException} and is intended to be handled by a global
 * exception handler that returns an HTTP 400 Bad Request response when thrown in a REST API.
 */
public class ReservationValidationException extends RuntimeException {

    /**
     * Constructs a new ReservationValidationException with the specified detail message.
     *
     * @param message The detail message explaining why the reservation validation failed.
     */
    public ReservationValidationException(String message) {
        super(message);
    }
}
