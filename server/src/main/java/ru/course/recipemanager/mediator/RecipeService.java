package ru.course.recipemanager.mediator;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.course.recipemanager.entity.Recipe;
import ru.course.recipemanager.entity.RecipeCategory;
import ru.course.recipemanager.entity.RecipeStatus;
import ru.course.recipemanager.foundation.RecipeRepository;

@Service
public class RecipeService {
    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public List<Recipe> findAll() {
        return repository.findAll();
    }

    public Recipe findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional
    public Recipe save(Recipe recipe) {
        if (recipe.getStatus() == null) {
            recipe.setStatus(RecipeStatus.PENDING);
        }
        return repository.save(recipe);
    }

    public List<Recipe> findPending() {
        return repository.findByStatus(RecipeStatus.PENDING);
    }

    @Transactional
    public Recipe approve(Long id) {
        Recipe recipe = findById(id);
        recipe.setStatus(RecipeStatus.APPROVED);
        return repository.save(recipe);
    }

    @Transactional
    public Recipe reject(Long id) {
        Recipe recipe = findById(id);
        recipe.setStatus(RecipeStatus.REJECTED);
        return repository.save(recipe);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Recipe> search(String query, RecipeCategory category) {
        if (category != null) {
            return repository.findByCategory(category);
        }
        if (query != null && !query.isBlank()) {
            return repository.findByTitleContainingIgnoreCase(query);
        }
        return repository.findAll();
    }
}
