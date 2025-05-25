package cz.vojtechsika.tennisclub.api.advice;

import cz.vojtechsika.tennisclub.dto.error.ApiErrorDTO;
import cz.vojtechsika.tennisclub.exception.CourtNotFoundException;
import cz.vojtechsika.tennisclub.exception.CourtNumberAlreadyExistsException;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
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

    @ExceptionHandler
    public ResponseEntity<ApiErrorDTO> handleCourException(CourtNumberAlreadyExistsException e){
        ApiErrorDTO error = new ApiErrorDTO();
        error.setStatusCode(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorDTO> handleCourException(CourtNotFoundException e){

        ApiErrorDTO error = new ApiErrorDTO();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }
}
