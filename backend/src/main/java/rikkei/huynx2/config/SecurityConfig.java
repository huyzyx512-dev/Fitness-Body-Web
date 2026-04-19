package rikkei.huynx2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cấu hình Spring Security: phân quyền, login/logout và mã hóa mật khẩu.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Cấu hình Filter Chain (Phân quyền URL, CSRF, Login/Logout)
     * 
     * @param HttpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
        // Tắt CSRF (thường dùng cho API)
                csrf(crsf -> crsf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Public
                        .requestMatchers("/api/auth/**").permitAll()

                        // USER: xem thông tin
                        .requestMatchers(HttpMethod.GET, "/api/users/**")
                        .hasAnyRole("USER", "ADMIN")

                        // ADMIN: tạo/sửa/xóa
                        .requestMatchers(HttpMethod.POST, "/api/users/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                        .hasRole("ADMIN")

                        // Role management (chỉ ADMIN)
                        .requestMatchers("/api/roles/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated() // Các request khác bắt buộc đăng nhập
                )

                .formLogin(form -> form.disable()) // API → nên tắt form login

                // Cấu hình HTTP Basic Auth (Thường dùng cho test API qua Postman)
                .httpBasic(basic -> {
                });

        return http.build();
    }

    /**
     * Bean mã hóa mật khẩu (BCrypt)
     * 
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
