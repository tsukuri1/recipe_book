package ru.course.recipemanager.control;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.course.recipemanager.entity.Recipe;
import ru.course.recipemanager.entity.RecipeCategory;
import ru.course.recipemanager.entity.RecipeStatus;
import ru.course.recipemanager.mediator.RecipeService;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Recipe> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Recipe byId(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Recipe create(@RequestBody Recipe recipe) {
        recipe.setStatus(RecipeStatus.PENDING);
        return service.save(recipe);
    }

    @PutMapping("/{id}")
    public Recipe update(@PathVariable Long id, @RequestBody Recipe recipe) {
        recipe.setId(id);
        return service.save(recipe);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<Recipe> search(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) RecipeCategory category
    ) {
        return service.search(query, category);
    }
}
