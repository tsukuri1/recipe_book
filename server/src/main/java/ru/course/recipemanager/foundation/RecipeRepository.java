package ru.course.recipemanager.foundation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.recipemanager.entity.Recipe;
import ru.course.recipemanager.entity.RecipeCategory;
import ru.course.recipemanager.entity.RecipeStatus;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByTitleContainingIgnoreCase(String query);
    List<Recipe> findByCategory(RecipeCategory category);
    List<Recipe> findByStatus(RecipeStatus status);
    long countByStatus(RecipeStatus status);
}
