package rikkei.huynx2.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rikkei.huynx2.model.User;
import rikkei.huynx2.repository.UserRepository;

/**
 *  Load thông tin User từ DB cho Spring Security
 *  Dùng để phục vụ quá trình authentication (Login).
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Tìm User theo username trong DB.
     * Nếu tồn tại -> convert sang UserDetails cho Spring Security
     * @return CustomUserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm User trong db
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. Nạp User vào UserDetailsService của Spring Security
        return new CustomUserDetails(user);
    }

}
