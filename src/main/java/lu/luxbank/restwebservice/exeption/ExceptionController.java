package lu.luxbank.restwebservice.exeption;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Log4j2
@ResponseBody
public class ExceptionController {


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        StringBuilder err = new StringBuilder();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(f -> err.append("\nfield [").append(f.getField()).append("] ")
                        .append(f.getDefaultMessage()).append("; "));

        log.fatal(err.toString());
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        log.fatal(ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder err = new StringBuilder();
        ex.getConstraintViolations()
                .forEach(f ->
                        err.append("\nfield [").append(f.getPropertyPath()).append("] ")
                                .append(f.getMessage()).append("; "));
        log.fatal(err.toString());
    }

    @ExceptionHandler({LuxBankGeneralException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionInfo> handleLuxBankInternalExceptions(LuxBankGeneralException ex) {
        log.fatal("\n\n================================= Lux Bank Exception =================================\n\n" +
                ex + "\n\n======================================================================================\n\n");
        return new ResponseEntity<>(new ExceptionInfo(ex.getErrorMsg()), ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionInfo handleAnyAnexpectedExceptions(Exception ex) {
        log.info("Unexpected exception", ex);
        return new ExceptionInfo("Unknown internal error");
    }

}
