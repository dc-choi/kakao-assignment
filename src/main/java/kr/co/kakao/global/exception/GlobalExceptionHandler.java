package kr.co.kakao.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import kr.co.kakao.global.common.message.FailHttpMessage;
import kr.co.kakao.global.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Custom Exception Handler
     */
    @ExceptionHandler({
            BusinessException.class,
            ValidationException.class // Custom Validation Exception
    })
    public ResponseEntity<ErrorResponse> handlerBusinessException(BusinessException e, HttpServletRequest request) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(
                        e.getMessage(),
                        request
                ));
    }

    /**
     * Validation 관련 Exception Handler
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            NumberFormatException.class,
            MethodArgumentTypeMismatchException.class,
            InvalidFormatException.class,
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ErrorResponse> handleBindException(MethodArgumentNotValidException e, HttpServletRequest request) {
        StringBuilder message = new StringBuilder();

        e.getFieldErrors().forEach(fieldError -> {
            message.append(fieldError.getDefaultMessage()).append(',');
        });
        message.deleteCharAt(message.length() - 1); // 마지막 ',' 제거

        return ResponseEntity
                .status(FailHttpMessage.INVALID_INPUT_VALUE.getStatus())
                .body(ErrorResponse.of(
                        message.toString(),
                        request
                ));
    }

    /**
     * HTTP 관련 Exception Handler
     */
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
    })
    public ResponseEntity<ErrorResponse> handleHttpException(Exception e, HttpServletRequest request) {
        FailHttpMessage response;

        String exceptionName = e.getClass().getSimpleName();
        switch (exceptionName) {
            case "HttpRequestMethodNotSupportedException" -> response = FailHttpMessage.METHOD_NOT_ALLOWED;
            case "AccessDeniedException" -> response = FailHttpMessage.DEACTIVATE_USER;
            case "MissingServletRequestParameterException" -> response = FailHttpMessage.MISSING_PARAMETER;
            default -> response = FailHttpMessage.BAD_REQUEST;
        }

        return ResponseEntity
                .status(response.getStatus())
                .body(ErrorResponse.of(
                        response.getMessage(),
                        request
                ));
    }

    /**
     * 설정하지 않은 Exception 처리 Handler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerAllException(Exception e, HttpServletRequest request) {
        return ResponseEntity
                .status(FailHttpMessage.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(
                        FailHttpMessage.INTERNAL_SERVER_ERROR.getMessage(),
                        request,
                        e
                ));
    }
}
