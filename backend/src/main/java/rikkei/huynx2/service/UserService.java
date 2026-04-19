package rikkei.huynx2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import rikkei.huynx2.dto.request.UserRequestDTO;
import rikkei.huynx2.dto.response.UserResponseDTO;

@Service
public interface UserService {
    UserResponseDTO create(UserRequestDTO request);

    List<UserResponseDTO> findAll();

    UserResponseDTO findById(Long id);

    UserResponseDTO update(Long id, UserRequestDTO request);

    void delete(Long id);
}
