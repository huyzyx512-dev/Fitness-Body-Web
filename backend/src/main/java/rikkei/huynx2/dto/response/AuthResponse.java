package rikkei.huynx2.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
