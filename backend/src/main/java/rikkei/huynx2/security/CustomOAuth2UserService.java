package rikkei.huynx2.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rikkei.huynx2.model.Role;
import rikkei.huynx2.model.User;
import rikkei.huynx2.repository.RoleRepository;
import rikkei.huynx2.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("🚀 Bắt đầu loadUser từ OAuth2. RegistrationId: {}",
                userRequest.getClientRegistration().getRegistrationId());

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "google"
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");

        log.debug("📦 Dữ liệu từ Google: email={}, sub={}, name={}", email, providerId, fullName);

        // 1. Tìm user đã tồn tại
        Optional<User> existingUser = userRepository.findByProviderAndProviderId(registrationId, providerId);

        User user;
        if (existingUser.isPresent()) {
            log.info("✅ User đã tồn tại trong DB: {}", existingUser.get().getEmail());
            user = existingUser.get();
        } else {
            log.info("🆕 Tạo user mới trong DB...");
            List<Role> roles = roleRepository.findAll();

            Set<Role> userRoles = roles.stream()
                    .filter(r -> r.getName().equals("ROLE_USER"))
                    .collect(Collectors.toSet());
            user = new User();
            user.setEmail(email);
            user.setProvider(registrationId);
            user.setProviderId(providerId);
            user.setRoles(userRoles);
            user.setCreatedAt(LocalDateTime.now());

            user = userRepository.save(user); // ⬅️ Breakpoint sẽ dừng ở đây
            log.info("💾 Đã lưu user vào DB với ID: {}", user.getId());
        }

        // 2. Trả về OAuth2User để Spring Security tạo session
        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
