package cz.vojtechsika.tennisclub.api.advice;

import cz.vojtechsika.tennisclub.dto.error.SurfaceTypeErrorResponse;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SurfaceTypeExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SurfaceTypeErrorResponse> handleSurfaceTypeException(SurfaceTypeNotFoundException e){

        SurfaceTypeErrorResponse error = new SurfaceTypeErrorResponse();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

}
