package ru.course.recipemanager.mediator;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.course.recipemanager.entity.AppUser;
import ru.course.recipemanager.foundation.UserRepository;
import ru.course.recipemanager.security.JwtTokenService;

@Service
public class AuthService {
    public static final String DEMO_ADMIN_EMAIL = "admin@ladushki.app";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtTokenService jwtTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        });

        AppUser user = new AppUser();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(isAdminEmail(request.email()) ? "ADMIN" : "USER");
        userRepository.save(user);

        return toResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        AppUser user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return toResponse(user);
    }

    private AuthResponse toResponse(AppUser user) {
        String token = jwtTokenService.createToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, "Bearer", user.getRole(), user.getName());
    }

    private boolean isAdminEmail(String email) {
        return DEMO_ADMIN_EMAIL.equalsIgnoreCase(email);
    }

    public record LoginRequest(String email, String password) {}
    public record RegisterRequest(String name, String email, String password) {}
    public record AuthResponse(String accessToken, String tokenType, String role, String name) {}
}
