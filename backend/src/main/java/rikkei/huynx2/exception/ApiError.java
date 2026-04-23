package rikkei.huynx2.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Description: Chuẩn hóa kiểu trả về lỗi cho toàn bộ hệ thống
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private int status;
    private String code;
    private String message;
    private String path;
    private ZonedDateTime timestamp;

}
