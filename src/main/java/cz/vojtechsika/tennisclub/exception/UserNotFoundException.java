package cz.vojtechsika.tennisclub.exception;

/**
 * UserNotFoundException is thrown when a requested user cannot be found in the system.
 * This exception typically indicates that there is no User entity corresponding to the provided identifier
 * or that the user has been marked as deleted.
 * This exception extends {@link RuntimeException} and is intended to be handled by a global
 * exception handler that returns an appropriate HTTP response (e.g., 404 Not Found) when thrown in a REST API.
 */
public class UserNotFoundException extends RuntimeException {

  /**
   * Constructs a new UserNotFoundException with the specified detail message.
   *
   * @param message The detail message explaining why the user was not found.
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
