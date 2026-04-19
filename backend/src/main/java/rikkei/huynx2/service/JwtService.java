package rikkei.huynx2.service;

import org.springframework.stereotype.Service;

import rikkei.huynx2.dto.request.RegisterRequestDTO;
import rikkei.huynx2.dto.response.RegisterResponseDTO;

@Service
public interface JwtService {
    RegisterResponseDTO register(RegisterRequestDTO requestDTO);
}
