package ru.course.recipemanager.mediator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import ru.course.recipemanager.entity.AppUser;
import ru.course.recipemanager.foundation.UserRepository;
import ru.course.recipemanager.mediator.AuthService.AuthResponse;
import ru.course.recipemanager.mediator.AuthService.LoginRequest;
import ru.course.recipemanager.mediator.AuthService.RegisterRequest;
import ru.course.recipemanager.security.JwtTokenService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private final JwtTokenService jwtTokenService = new JwtTokenService("test-secret");

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void registerCreatesAdminForDemoAdminEmail() {
        AuthService service = new AuthService(userRepository, passwordEncoder, jwtTokenService);
        when(userRepository.findByEmail("admin@ladushki.app")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encoded-password");

        AuthResponse response = service.register(new RegisterRequest("Администратор", "admin@ladushki.app", "1234"));

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).save(userCaptor.capture());
        AppUser saved = userCaptor.getValue();
        assertEquals("ADMIN", saved.getRole());
        assertEquals("encoded-password", saved.getPasswordHash());
        assertEquals("ADMIN", response.role());
        assertEquals("Bearer", response.tokenType());
        assertFalse(response.accessToken().isBlank());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        AuthService service = new AuthService(userRepository, passwordEncoder, jwtTokenService);
        when(userRepository.findByEmail("user@ladushki.app")).thenReturn(Optional.of(new AppUser()));

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.register(new RegisterRequest("Пользователь", "user@ladushki.app", "1234"))
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void loginReturnsTokenForValidPassword() {
        AuthService service = new AuthService(userRepository, passwordEncoder, jwtTokenService);
        AppUser user = new AppUser();
        user.setName("Соня");
        user.setEmail("user@ladushki.app");
        user.setPasswordHash("encoded-password");
        user.setRole("USER");
        when(userRepository.findByEmail("user@ladushki.app")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "encoded-password")).thenReturn(true);

        AuthResponse response = service.login(new LoginRequest("user@ladushki.app", "1234"));

        assertEquals("USER", response.role());
        assertEquals("Соня", response.name());
        assertTrue(response.accessToken().length() > 10);
    }

    @Test
    void loginRejectsInvalidPassword() {
        AuthService service = new AuthService(userRepository, passwordEncoder, jwtTokenService);
        AppUser user = new AppUser();
        user.setEmail("user@ladushki.app");
        user.setPasswordHash("encoded-password");
        when(userRepository.findByEmail("user@ladushki.app")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("bad", "encoded-password")).thenReturn(false);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.login(new LoginRequest("user@ladushki.app", "bad"))
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }
}
