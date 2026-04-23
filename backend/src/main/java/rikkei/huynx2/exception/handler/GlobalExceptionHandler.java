package rikkei.huynx2.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rikkei.huynx2.exception.ApiError;
import rikkei.huynx2.exception.AppException;
import rikkei.huynx2.exception.ErrorCode;
import rikkei.huynx2.exception.factory.ErrorResponseFactory;

/**
 * Description: Dùng để bắt lỗi cho Controller/Service Layer
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiError> handleAppException(AppException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(ErrorResponseFactory.build(ex.getErrorCode(), request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnknown(Exception ex, HttpServletRequest request) {

        return ResponseEntity.status(500).body(ErrorResponseFactory.build(ErrorCode.INTERNAL_ERROR, request));
    }
}
