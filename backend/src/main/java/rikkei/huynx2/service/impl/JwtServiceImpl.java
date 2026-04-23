package rikkei.huynx2.service.impl;

import rikkei.huynx2.dto.request.RegisterRequestDTO;
import rikkei.huynx2.dto.response.RegisterResponseDTO;
import rikkei.huynx2.exception.AppException;
import rikkei.huynx2.exception.ErrorCode;
import rikkei.huynx2.model.Role;
import rikkei.huynx2.model.User;
import rikkei.huynx2.repository.RoleRepository;
import rikkei.huynx2.repository.UserRepository;
import rikkei.huynx2.service.JwtService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 *
 */

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO requestDTO) {
        // Kiểm tra username tồn tại hay chưa
        Optional<User> user = userRepository.findByUsername(requestDTO.getUsername());

        if (user.isPresent())
            throw new AppException(ErrorCode.USER_EXISTED);
        // Lấy all role trong hệ thống
        List<Role> roles = roleRepository.findAll();

        Set<Role> userRoles = roles.stream()
                .filter(r -> r.getName().equals("ROLE_USER"))
                .collect(Collectors.toSet());

        User newUser = User.builder()
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .enabled(true)
                .roles(userRoles)
                .build();

        userRepository.save(newUser);

        Set<String> rolesName = newUser.getRoles()
                .stream()
                .map(Role::getName)
                .filter("ROLE_USER"::equals)
                .collect(Collectors.toSet());

        return new RegisterResponseDTO(newUser.getUsername(), rolesName);
    }

}
