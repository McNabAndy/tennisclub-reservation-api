package cz.vojtechsika.tennisclub.api.advice;

import cz.vojtechsika.tennisclub.dto.error.ApiErrorDTO;
import cz.vojtechsika.tennisclub.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler is a class responsible for handling exceptions globally in the application.
 * It uses the {@link ControllerAdvice} annotation to provide a centralized way of handling exceptions
 * across all controllers in the application.
 * Each method in this class is responsible for handling a specific type of exception and returning a response
 * with an appropriate HTTP status and error details.
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles the {@link SurfaceTypeNotFoundException}.
     * When a surface type is not found, it returns a response with HTTP status 404 (Not Found).
     *
     * @param e The exception that was caught.
     * @return ResponseEntity containing error details and HTTP status code 404.
     */
    @ExceptionHandler
    public ResponseEntity<ApiErrorDTO> handleSurfaceTypeException(SurfaceTypeNotFoundException e){

        ApiErrorDTO error = new ApiErrorDTO();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    /**
     * Handles exceptions of type {@link CourtNotFoundException} and {@link CourtNumberAlreadyExistsException}.
     * If a court number already exists, it returns a response with HTTP status 409 (Conflict).
     * If the court is not found, it returns a response with HTTP status 404 (Not Found).
     *
     * @param e The exception that was caught.
     * @return ResponseEntity containing error details and the appropriate HTTP status.
     */
    @ExceptionHandler({CourtNotFoundException.class, CourtNumberAlreadyExistsException.class})
    public ResponseEntity<ApiErrorDTO> handleCourException(RuntimeException e){

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        if (e.getCause() instanceof CourtNumberAlreadyExistsException){
            httpStatus = HttpStatus.CONFLICT;
        }

        ApiErrorDTO error = new ApiErrorDTO();
        error.setStatusCode(httpStatus.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, httpStatus);
    }


    /**
     * Handles exceptions of type {@link ReservationNotFoundException} and {@link ReservationValidationException}.
     * If a reservation is not found, it returns a response with HTTP status 404 (Not Found).
     * If the reservation validation fails, it returns a response with HTTP status 400 (Bad Request).
     *
     * @param e The exception that was caught.
     * @return ResponseEntity containing error details and the appropriate HTTP status.
     */
    @ExceptionHandler({ReservationNotFoundException.class, ReservationValidationException.class})
    public ResponseEntity<ApiErrorDTO> handleReservationException(RuntimeException e){

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        if (e.getCause() instanceof ReservationValidationException){
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        ApiErrorDTO error = new ApiErrorDTO();
        error.setStatusCode(httpStatus.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, httpStatus);
    }


    /**
     * Handles the {@link UserNotFoundException}.
     * When a user is not found, it returns a response with HTTP status 404 (Not Found).
     *
     * @param e The exception that was caught.
     * @return ResponseEntity containing error details and HTTP status code 404.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleUserException(RuntimeException e){

        ApiErrorDTO error = new ApiErrorDTO();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }
}
