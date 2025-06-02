package cz.vojtechsika.tennisclub.exception;
/**
 * CourtNumberAlreadyExistsException is thrown when attempting to create or update a court
 * with a court number that already exists in the system. This exception indicates a conflict
 * due to the uniqueness constraint on court numbers.
 *
 * This exception extends {@link RuntimeException} and is intended to be handled by a global
 * exception handler that returns an HTTP 409 Conflict response when thrown in a REST API.
 */
public class CourtNumberAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new CourtNumberAlreadyExistsException with the specified detail message.
     *
     * @param message The detail message explaining why the court number conflict occurred.
     */
    public CourtNumberAlreadyExistsException(String message) {
        super(message);
    }
}
