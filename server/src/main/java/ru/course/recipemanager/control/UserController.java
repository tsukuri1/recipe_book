package ru.course.recipemanager.control;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.course.recipemanager.entity.AppUser;
import ru.course.recipemanager.foundation.RecipeRepository;
import ru.course.recipemanager.foundation.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public UserController(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/me")
    public Map<String, Object> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication == null ? "user@ladushki.app" : authentication.getName();
        AppUser user = userRepository.findByEmail(email).orElseGet(() -> {
            AppUser fallback = new AppUser();
            fallback.setName("Пользователь");
            fallback.setEmail(email);
            fallback.setRole("USER");
            return fallback;
        });

        return Map.of(
            "name", user.getName(),
            "email", user.getEmail(),
            "role", user.getRole(),
            "recipesCount", recipeRepository.count(),
            "favoriteCount", 0,
            "offlineCount", 0
        );
    }
}
