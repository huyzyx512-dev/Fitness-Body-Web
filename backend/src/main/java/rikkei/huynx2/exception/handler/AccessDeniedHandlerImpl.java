package rikkei.huynx2.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import rikkei.huynx2.exception.ApiError;
import rikkei.huynx2.exception.ErrorCode;
import rikkei.huynx2.exception.factory.ErrorResponseFactory;

import java.io.IOException;

/**
 * Description: Dùng để bắt lỗi 403 của Security
 */

@RequiredArgsConstructor
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    public final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        ApiError error = ErrorResponseFactory.build(ErrorCode.FORBIDDEN, request);

        response.setStatus(error.getStatus());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
