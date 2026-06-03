package ru.course.recipemanager.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.course.recipemanager.entity.Ingredient;
import ru.course.recipemanager.entity.Recipe;
import ru.course.recipemanager.entity.RecipeCategory;
import ru.course.recipemanager.entity.RecipeStatus;
import ru.course.recipemanager.foundation.RecipeRepository;
import ru.course.recipemanager.foundation.UserRepository;
import ru.course.recipemanager.mediator.RecipeService;
import ru.course.recipemanager.mediator.ShoppingListService;

@ExtendWith(MockitoExtension.class)
class ControllerSmokeTest {
    @Mock
    private RecipeService recipeService;

    @Mock
    private ShoppingListService shoppingListService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void recipeControllerDelegatesCrudAndSearch() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Сырники");
        when(recipeService.findAll()).thenReturn(List.of(recipe));
        when(recipeService.findById(1L)).thenReturn(recipe);
        when(recipeService.save(recipe)).thenReturn(recipe);
        when(recipeService.search("сыр", RecipeCategory.Dessert)).thenReturn(List.of(recipe));

        RecipeController controller = new RecipeController(recipeService);

        assertEquals(List.of(recipe), controller.all());
        assertEquals(recipe, controller.byId(1L));
        assertEquals(recipe, controller.create(recipe));
        assertEquals(RecipeStatus.PENDING, recipe.getStatus());
        assertEquals(recipe, controller.update(1L, recipe));
        assertEquals(List.of(recipe), controller.search("сыр", RecipeCategory.Dessert));
        controller.delete(1L);

        verify(recipeService).delete(1L);
    }

    @Test
    void shoppingListControllerAddsIngredientsFromRecipe() {
        Ingredient milk = new Ingredient();
        milk.setName("Молоко");
        Ingredient eggs = new Ingredient();
        eggs.setName("Яйца");
        Recipe recipe = new Recipe();
        recipe.setIngredients(List.of(milk, eggs));
        when(recipeService.findById(4L)).thenReturn(recipe);

        new ShoppingListController(shoppingListService, recipeService).addFromRecipe(4L);

        verify(shoppingListService).add("Молоко");
        verify(shoppingListService).add("Яйца");
    }

    @Test
    void adminControllerReturnsStatsAndModeratesRecipes() {
        Recipe pending = new Recipe();
        when(userRepository.count()).thenReturn(2L);
        when(recipeRepository.count()).thenReturn(8L);
        when(recipeRepository.countByStatus(RecipeStatus.PENDING)).thenReturn(1L);
        when(recipeService.findPending()).thenReturn(List.of(pending));
        when(recipeService.approve(1L)).thenReturn(pending);
        when(recipeService.reject(2L)).thenReturn(pending);

        AdminController controller = new AdminController(userRepository, recipeRepository, recipeService);
        AdminController.AdminStatsDto stats = controller.stats();

        assertEquals(2L, stats.usersCount());
        assertEquals(8L, stats.recipesCount());
        assertEquals(1L, stats.pendingRecipesCount());
        assertEquals(List.of(pending), controller.pendingRecipes());
        assertEquals(pending, controller.approveRecipe(1L));
        assertEquals(pending, controller.rejectRecipe(2L));
    }

    @Test
    void simpleStatefulControllersKeepExpectedState() {
        FavoriteController favorites = new FavoriteController();
        favorites.add(10L);
        assertTrue(favorites.all().contains(10L));
        favorites.delete(10L);
        assertTrue(favorites.all().isEmpty());

        SettingsController settings = new SettingsController();
        SettingsController.UserSettingsDto updated = settings.update(
            new SettingsController.UserSettingsDto(false, true, false, true)
        );
        assertEquals(updated, settings.get());

        ComplaintController complaints = new ComplaintController();
        assertEquals("В работе", complaints.review(1L).status());
        assertEquals("Закрыта", complaints.resolve(1L).status());
        assertEquals("Илья", new UserController().me().get("name"));
    }
}
