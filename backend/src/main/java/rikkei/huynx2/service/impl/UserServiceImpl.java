package rikkei.huynx2.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rikkei.huynx2.dto.request.UserRequestDTO;
import rikkei.huynx2.dto.response.UserResponseDTO;
import rikkei.huynx2.exception.AppException;
import rikkei.huynx2.exception.ErrorCode;
import rikkei.huynx2.mapper.UserMapper;
import rikkei.huynx2.model.Role;
import rikkei.huynx2.model.User;
import rikkei.huynx2.repository.RoleRepository;
import rikkei.huynx2.repository.UserRepository;
import rikkei.huynx2.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDTO create(UserRequestDTO request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if (user.isPresent()) throw new AppException(ErrorCode.USER_EXISTED);
        Set<Role> roles = getRolesFromRequest(request.getRoles());

        return UserMapper.toResponse(
                userRepository.save(User.builder()
                        .username(request.getUsername())
                        .password(request.getPassword())
                        .enabled(request.getEnabled())
                        .roles(roles)
                        .build()));
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setUsername(request.getUsername());
        user.setEnabled(request.getEnabled());

        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }

        // update roles
        if (request.getRoles() != null) {
            Set<Role> roles = getRolesFromRequest(request.getRoles());
            user.setRoles(roles);
        }

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // helper
    private Set<Role> getRolesFromRequest(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();

        for (String name : roleNames) {
            Role role = roleRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + name));
            roles.add(role);
        }

        return roles;
    }

}
