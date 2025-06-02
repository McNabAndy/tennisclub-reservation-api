package cz.vojtechsika.tennisclub.exception;

/**
 * SurfaceTypeNotFoundException is thrown when a requested surface type cannot be found in the system.
 * This exception typically indicates that there is no SurfaceType entity corresponding to the provided identifier,
 * or that the surface type has been marked as deleted.
 * This exception extends {@link RuntimeException} and is intended to be handled by a global exception
 * handler that returns an appropriate HTTP response (e.g., 404 Not Found) when thrown in a REST API.
 */
public class SurfaceTypeNotFoundException extends RuntimeException {

    /**
     * Constructs a new SurfaceTypeNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining why the surface type was not found.
     */
    public SurfaceTypeNotFoundException(String message) {
        super(message);
    }
}
