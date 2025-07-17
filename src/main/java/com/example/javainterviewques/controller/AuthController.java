package com.example.javainterviewques.controller;

import com.example.javainterviewques.model.User;
import com.example.javainterviewques.service.RefreshTokenService;
import com.example.javainterviewques.service.UserService;
import com.example.javainterviewques.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        return userService.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(username);
                    // Refresh token oluştur
                    var refreshToken = refreshTokenService.createRefreshToken(user.getId());
                    return ResponseEntity.ok(Map.of(
                            "token", token,
                            "refreshToken", refreshToken.getToken()
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Kullanıcı adı veya şifre hatalı")));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestParam String username, @RequestParam String password) {
        if (userService.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Bu kullanıcı adı zaten mevcut."));
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("ROLE_ADMIN"); // default role
        userService.save(user);
        return ResponseEntity.ok(Map.of("message", "Kullanıcı başarıyla kaydedildi."));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String requestRefreshToken = request.get("refreshToken");

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    User user = refreshToken.getUser();
                    String token = jwtUtil.generateToken(user.getUsername());
                    return ResponseEntity.ok(Map.of(
                            "token", token,
                            "refreshToken", requestRefreshToken
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(403).body(Map.of("error", "Geçersiz refresh token")));
    }
}
