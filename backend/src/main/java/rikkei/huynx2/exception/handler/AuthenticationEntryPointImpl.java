package rikkei.huynx2.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import rikkei.huynx2.exception.ApiError;
import rikkei.huynx2.exception.ErrorCode;
import rikkei.huynx2.exception.factory.ErrorResponseFactory;

import java.io.IOException;

/**
 * Description: Dùng để bắt lỗi 401 của Filter
 */

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        ApiError error = ErrorResponseFactory.build(ErrorCode.UNAUTHORIZED,  request);

        response.setStatus(error.getStatus());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
