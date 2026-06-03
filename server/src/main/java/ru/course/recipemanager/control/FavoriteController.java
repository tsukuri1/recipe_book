package ru.course.recipemanager.control;

import java.util.HashSet;
import java.util.Set;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final Set<Long> favoriteRecipeIds = new HashSet<>();

    @GetMapping
    public Set<Long> all() {
        return favoriteRecipeIds;
    }

    @PostMapping("/{recipeId}")
    public void add(@PathVariable Long recipeId) {
        favoriteRecipeIds.add(recipeId);
    }

    @DeleteMapping("/{recipeId}")
    public void delete(@PathVariable Long recipeId) {
        favoriteRecipeIds.remove(recipeId);
    }
}
