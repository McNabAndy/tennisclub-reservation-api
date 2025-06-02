package cz.vojtechsika.tennisclub.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * ApiErrorDTO is a Data Transfer Object used to represent error details in API responses.
 * It contains the HTTP status code, an error message, and a timestamp indicating when the error occurred.
 * This DTO is used by global exception handlers to send structured error information to clients.
 *
 * <p>Example JSON representation:</p>
 * <pre>
 * {
 *   "statusCode": 404,
 *   "message": "Resource not found",
 *   "timestamp": 1685721600000
 * }
 * </pre>
 * </p>
 *
 * Fields:
 * <ul>
 *   <li>{@code statusCode}: The HTTP status code corresponding to the error (e.g., 404 for Not Found, 400 for Bad Request).</li>
 *   <li>{@code message}: A human-readable message describing the error condition.</li>
 *   <li>{@code timestamp}: A timestamp (in milliseconds since the epoch) indicating when the error occurred.</li>
 * </ul>
 *
 *
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDTO {


    /**
     * The HTTP status code corresponding to the error (e.g., 404 for Not Found, 400 for Bad Request).
     */
    private int statusCode;

    /**
     * A human-readable message describing the error condition.
     */
    private String message;

    /**
     * A timestamp (in milliseconds since the epoch) indicating when the error occurred.
     */
    private long timestamp;

}
