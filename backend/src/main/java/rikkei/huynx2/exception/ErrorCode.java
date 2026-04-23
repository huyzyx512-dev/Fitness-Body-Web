package rikkei.huynx2.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Description: Chứa toàn bộ khai báo lỗi của hệ thống
 */

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // AUTH
    UNAUTHORIZED(401, "UNAUTHORIZED", "Unauthorized"),
    FORBIDDEN(403, "FORBIDDEN", "Access denied"),

    // USER
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "User not found"),
    USER_EXISTED(409, "FORBIDDEN", "User is existed"),

    // VALIDATION
    INVALID_INPUT(400, "INVALID_INPUT", "Invalid input"),

    // SYSTEM
    INTERNAL_ERROR(500, "INTERNAL_SERVER_ERROR", "Something went wrong");

    private final int status;
    private final String code;
    private final String message;

}
