package cz.vojtechsika.tennisclub.exception;

/**
 * CourtNotFoundException is thrown when a requested tennis court cannot be found in the system.
 * This exception typically indicates that there is no court entity corresponding to the provided identifier
 * or court number, or that the court has been marked as deleted.
 * This exception extends {@link RuntimeException} and is intended to be handled by a global exception
 * handler that returns an appropriate HTTP response (e.g., 404 Not Found) when thrown in a REST API.
 */
public class CourtNotFoundException extends RuntimeException {

    /**
     * Constructs a new CourtNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining why the court was not found.
     */
    public CourtNotFoundException(String message) {
        super(message);
    }
}
