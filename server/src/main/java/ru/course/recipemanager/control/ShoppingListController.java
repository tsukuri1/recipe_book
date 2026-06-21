package ru.course.recipemanager.control;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.course.recipemanager.entity.ShoppingListItem;
import ru.course.recipemanager.mediator.RecipeService;
import ru.course.recipemanager.mediator.ShoppingListService;

@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {
    private final ShoppingListService service;
    private final RecipeService recipeService;

    public ShoppingListController(ShoppingListService service, RecipeService recipeService) {
        this.service = service;
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<ShoppingListItem> all() {
        return service.findAll();
    }

    @PostMapping("/from-recipe/{recipeId}")
    public void addFromRecipe(@PathVariable Long recipeId) {
        recipeService.findById(recipeId).getIngredients()
            .forEach(ingredient -> service.add(ingredient.getName()));
    }

    @PatchMapping("/items/{itemId}")
    public void toggle(@PathVariable Long itemId) {
        service.toggle(itemId);
    }

    @DeleteMapping("/completed")
    public void clearCompleted() {
        service.clearCompleted();
    }

    @DeleteMapping
    public void clearAll() {
        service.clearAll();
    }
}
