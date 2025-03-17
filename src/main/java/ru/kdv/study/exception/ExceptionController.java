package ru.kdv.study.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kdv.study.model.ServiceExceptionMessage;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ServiceExceptionMessage> handleException(Exception e) {
        log.error("ExceptionController#Exception", e);
        return ResponseEntity.internalServerError()
                .body(new ServiceExceptionMessage(e.getMessage()));
    }

    @ExceptionHandler(NoDataFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ServiceExceptionMessage> handleNoDataFoundException(NoDataFoundException e) {
        log.error("ExceptionController#NoDataFoundException", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ServiceExceptionMessage(e.getMessage()));
    }

    @ExceptionHandler(DataBaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServiceExceptionMessage> handleDataBaseException(DataBaseException e) {
        log.error("ExceptionController#DataBaseException", e);
        return ResponseEntity.badRequest()
                .body(new ServiceExceptionMessage(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServiceExceptionMessage> handleBadRequestException(BadRequestException e){
        log.error("ExceptionController#BadRequestException", e);
        return ResponseEntity.badRequest()
                .body(new ServiceExceptionMessage(e.getMessage()));
    }
}