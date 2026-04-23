package rikkei.huynx2.exception.factory;

import jakarta.servlet.http.HttpServletRequest;
import rikkei.huynx2.exception.ApiError;
import rikkei.huynx2.exception.ErrorCode;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Description: Chuẩn hóa mọi lỗi thành một format trả về
 */

public class ErrorResponseFactory {

    public static ApiError build(ErrorCode code, HttpServletRequest request) {
        return ApiError.builder()
                .status(code.getStatus())
                .code(code.name())
                .message(code.getMessage())
                .path(request.getRequestURI())
                .timestamp(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();
    }
}
