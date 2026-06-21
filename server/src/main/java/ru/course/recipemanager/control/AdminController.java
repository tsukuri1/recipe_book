package ru.course.recipemanager.control;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.course.recipemanager.entity.Recipe;
import ru.course.recipemanager.entity.RecipeStatus;
import ru.course.recipemanager.foundation.RecipeRepository;
import ru.course.recipemanager.foundation.UserRepository;
import ru.course.recipemanager.mediator.RecipeService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;

    public AdminController(
        UserRepository userRepository,
        RecipeRepository recipeRepository,
        RecipeService recipeService
    ) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.recipeService = recipeService;
    }

    @GetMapping("/stats")
    public AdminStatsDto stats() {
        return new AdminStatsDto(
            userRepository.count(),
            recipeRepository.count(),
            recipeRepository.countByStatus(RecipeStatus.PENDING),
            2
        );
    }

    @GetMapping("/recipes/pending")
    public List<Recipe> pendingRecipes() {
        return recipeService.findPending();
    }

    @PatchMapping("/recipes/{id}/approve")
    public Recipe approveRecipe(@PathVariable Long id) {
        return recipeService.approve(id);
    }

    @PatchMapping("/recipes/{id}/reject")
    public Recipe rejectRecipe(@PathVariable Long id) {
        return recipeService.reject(id);
    }

    public record AdminStatsDto(
        long usersCount,
        long recipesCount,
        long pendingRecipesCount,
        long reportsCount
    ) {}
}
