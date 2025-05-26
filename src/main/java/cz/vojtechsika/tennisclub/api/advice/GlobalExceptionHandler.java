package cz.vojtechsika.tennisclub.api.advice;

import cz.vojtechsika.tennisclub.dto.error.ApiErrorDTO;
import cz.vojtechsika.tennisclub.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // je tu dost duplicitní kod pokud zbyde čas musím refaktorovat  upravit

    @ExceptionHandler
    public ResponseEntity<ApiErrorDTO> handleSurfaceTypeException(SurfaceTypeNotFoundException e){

        ApiErrorDTO error = new ApiErrorDTO();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

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



    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleUserException(RuntimeException e){

        ApiErrorDTO error = new ApiErrorDTO();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }
}
