package by.Brahyam.Garcia.Blas.the.message.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {
    private static final Logger log = LoggerFactory.getLogger(GlobalHandlerException.class);
    @ExceptionHandler(PasswordEncoderNotMatch.class)
    public ResponseEntity<Map<String,Object>> handlePasswordEncoderNotMatch(PasswordEncoderNotMatch ex){
        log.warn("PasswordEncoderNotMatch: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleRoomNotFoundException(RoomNotFoundException ex){
        log.warn("RoomNotFoundException: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.NOT_FOUND,ex.getMessage());
    }
    @ExceptionHandler(FieldsNotMatchesException.class)
    public ResponseEntity<Map<String,Object>> handleFieldsNotMatchesException(FieldsNotMatchesException ex){
        log.warn("FieldsNotMatchesException: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String,Object>> handleTokenExpiredException(TokenExpiredException ex){
        log.warn("TokenExpiredException: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.FORBIDDEN,ex.getMessage());
    }
    @ExceptionHandler(TokenVericationPasswordNotFoundExeption.class)
    public ResponseEntity<Map<String,Object>> handleTokenVericationPasswordNotFoundExeption(TokenVericationPasswordNotFoundExeption ex){
        log.warn("TokenVericationPasswordNotFoundExeption: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.NOT_FOUND,ex.getMessage());
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handlerGEmailNotFoundException(Exception ex){
        log.warn("EmailNotFoundException: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(BadCredentialException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialException ex) {
        log.warn("BadCredentialException: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AccountDeactivatedException.class)
    public ResponseEntity<Map<String, Object>> handleAccountDeactivatedException(AccountDeactivatedException ex) {
        log.warn("AccountDeactivatedException: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<Map<String, Object>> handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
        log.warn("EmailNotVerifiedException: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredencialException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredencialException(InvalidCredencialException ex) {
        log.warn("InvalidCredencialException: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleResourceNotFoundException(ResourceNotFoundException ex){
        log.warn("ResourceNotFoundException: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.NOT_FOUND,ex.getMessage());
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidTokenException(InvalidTokenException ex){
        log.warn("InvalidTokenException: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidRoleException(InvalidRoleException ex){
        log.warn("InvalidRoleException: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    @ExceptionHandler(EmailAlreadyException.class)
    public ResponseEntity<Map<String,Object>> handleEmailAlreadyException(EmailAlreadyException ex){
        log.warn("EmailAlreadyException: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String message=
                ex.getBindingResult().getFieldErrors().stream()
                        .findFirst()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .orElse("invalid request");
        HttpStatus status=HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(Map.of("timestamp", Instant.now(),"error",message));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handlerGeneric(Exception ex){
        log.warn("Exception: {}",ex.getMessage(),ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
    }


    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body=Map.of("timestamp",Instant.now(),"error",message);
        return ResponseEntity.status(status).body(body);
    }


}
