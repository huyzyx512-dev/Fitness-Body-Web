package rikkei.huynx2.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rikkei.huynx2.dto.request.UserRequestDTO;
import rikkei.huynx2.dto.response.UserResponseDTO;
import rikkei.huynx2.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public UserResponseDTO create(@RequestBody UserRequestDTO request) {
        return userService.create(request);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO update(@PathVariable Long id,
                               @RequestBody UserRequestDTO request) {
        return userService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
