package rikkei.huynx2.mapper;

import java.util.stream.Collectors;

import rikkei.huynx2.dto.response.UserResponseDTO;
import rikkei.huynx2.model.User;

public class UserMapper {
    
    public static UserResponseDTO toResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role -> role.getName())
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
