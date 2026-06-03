package ru.course.recipemanager.mediator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.course.recipemanager.entity.Recipe;
import ru.course.recipemanager.entity.RecipeCategory;
import ru.course.recipemanager.entity.RecipeStatus;
import ru.course.recipemanager.foundation.RecipeRepository;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @Mock
    private RecipeRepository repository;

    @Test
    void approveChangesRecipeStatus() {
        Recipe recipe = new Recipe();
        recipe.setId(7L);
        recipe.setStatus(RecipeStatus.PENDING);
        when(repository.findById(7L)).thenReturn(Optional.of(recipe));
        when(repository.save(recipe)).thenReturn(recipe);

        Recipe approved = new RecipeService(repository).approve(7L);

        assertSame(recipe, approved);
        assertEquals(RecipeStatus.APPROVED, approved.getStatus());
        verify(repository).save(recipe);
    }

    @Test
    void rejectChangesRecipeStatus() {
        Recipe recipe = new Recipe();
        recipe.setId(8L);
        recipe.setStatus(RecipeStatus.PENDING);
        when(repository.findById(8L)).thenReturn(Optional.of(recipe));
        when(repository.save(recipe)).thenReturn(recipe);

        Recipe rejected = new RecipeService(repository).reject(8L);

        assertEquals(RecipeStatus.REJECTED, rejected.getStatus());
        verify(repository).save(recipe);
    }

    @Test
    void searchByCategoryHasPriorityOverQuery() {
        Recipe breakfast = new Recipe();
        breakfast.setTitle("Ягодная овсянка");
        when(repository.findByCategory(RecipeCategory.Breakfast)).thenReturn(List.of(breakfast));

        List<Recipe> result = new RecipeService(repository).search("суп", RecipeCategory.Breakfast);

        assertEquals(List.of(breakfast), result);
        verify(repository).findByCategory(RecipeCategory.Breakfast);
    }

    @Test
    void searchByQueryUsesTitleContainsIgnoringCase() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Сырники");
        when(repository.findByTitleContainingIgnoreCase("сыр")).thenReturn(List.of(recipe));

        List<Recipe> result = new RecipeService(repository).search("сыр", null);

        assertEquals(List.of(recipe), result);
        verify(repository).findByTitleContainingIgnoreCase("сыр");
    }

    @Test
    void emptySearchReturnsAllRecipes() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Паста");
        when(repository.findAll()).thenReturn(List.of(recipe));

        List<Recipe> result = new RecipeService(repository).search("   ", null);

        assertEquals(List.of(recipe), result);
        verify(repository).findAll();
    }
}
