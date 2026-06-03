package ru.course.recipemanager.control;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/me")
    public Map<String, Object> me() {
        return Map.of(
            "name", "Илья",
            "recipesCount", 48,
            "favoriteCount", 16,
            "offlineCount", 12
        );
    }
}

