package rikkei.huynx2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .requestMatchers("/", "/home", "/public/**").permitAll() // Ai cũng vào được
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Chỉ ADMIN mới vào được
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // USER hoặc ADMIN đều được
                .anyRequest().authenticated()                                   // Các request khác bắt buộc đăng nhập
            )

            // Cấu hình Form Login mặc định (hoặc bạn có thể custom trang login)
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )

            // Cấu hình HTTP Basic Auth (Thường dùng cho test API qua Postman)
            .httpBasic(basic -> {})

            // Cấu hình Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
        
        return http.build();
    }

    /**
     * Bean mã hóa mật khẩu (BCrypt)
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
