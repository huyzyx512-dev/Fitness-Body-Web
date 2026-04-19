package rikkei.huynx2.controller;

import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rikkei.huynx2.model.Role;
import rikkei.huynx2.security.CustomUserDetails;

@RestController
public class UserController {
    
    // API public ai cũng gọi được
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Chào mừng bạn đến với hệ thống!";
    }

    // API cần đăng nhập, sử dụng @AuthenticationPrincipal để lấy thông tin user hiện tại
    @GetMapping("/api/profile")
    public String getUserProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        String username = currentUser.getUsername();
        Set<Role> role = currentUser.getUserEntity().getRoles();
        
        return "Xin chào " + username + "! Quyền hạn của bạn là: " + role;
    }
}
