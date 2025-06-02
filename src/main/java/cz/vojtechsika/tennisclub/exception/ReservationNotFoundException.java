package cz.vojtechsika.tennisclub.exception;

/**
 * ReservationNotFoundException is thrown when a requested reservation cannot be found in the system.
 * This exception typically indicates that there is no reservation entity corresponding to the provided identifier,
 * or that the reservation has been marked as deleted.
 * This exception extends {@link RuntimeException} and is intended to be handled by a global exception
 * handler that returns an appropriate HTTP response (e.g., 404 Not Found) when thrown in a REST API.
 */
public class ReservationNotFoundException extends RuntimeException {

    /**
     * Constructs a new ReservationNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining why the reservation was not found.
     */
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
