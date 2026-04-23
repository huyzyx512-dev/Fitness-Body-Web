package rikkei.huynx2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import rikkei.huynx2.exception.handler.AccessDeniedHandlerImpl;
import rikkei.huynx2.exception.handler.AuthenticationEntryPointImpl;
import rikkei.huynx2.security.CustomOAuth2UserService;

/**
 * Cấu hình Spring Security: phân quyền, login/logout và mã hóa mật khẩu.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationEntryPointImpl customAuthenticationEntryPoint;
    private final AccessDeniedHandlerImpl customAccessDeniedHandler;

    /**
     * Cấu hình Filter Chain (Phân quyền URL, CSRF, Login/Logout)
     *
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF (thường dùng cho API)
                .csrf(crsf -> crsf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/error").permitAll()
                        // Public
                        .requestMatchers("/api/auth/**").permitAll()

                        // USER: xem thông tin
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USER", "ADMIN")

                        // ADMIN: tạo/sửa/xóa
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        // Role management (chỉ ADMIN)
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")

                        .anyRequest().authenticated() // Các request khác bắt buộc đăng nhập
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Phase 5
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                // Cấu hình HTTP Basic Auth (Thường dùng cho test API qua Postman)
                .httpBasic(basic -> {});

        return http.build();
    }

}
