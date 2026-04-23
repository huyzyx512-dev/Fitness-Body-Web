package rikkei.huynx2.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class ApiResponse<T> {
    private Boolean status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
