package ru.course.recipemanager.mediator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.course.recipemanager.entity.ShoppingListItem;
import ru.course.recipemanager.foundation.ShoppingListRepository;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {
    @Mock
    private ShoppingListRepository repository;

    @Test
    void addCreatesUncheckedItem() {
        ShoppingListService service = new ShoppingListService(repository);

        service.add("Молоко");

        ArgumentCaptor<ShoppingListItem> itemCaptor = ArgumentCaptor.forClass(ShoppingListItem.class);
        verify(repository).save(itemCaptor.capture());
        ShoppingListItem saved = itemCaptor.getValue();
        assertFalse(saved.isChecked());
    }

    @Test
    void toggleInvertsCheckedFlag() {
        ShoppingListItem item = new ShoppingListItem();
        item.setId(3L);
        item.setChecked(false);
        when(repository.findById(3L)).thenReturn(Optional.of(item));

        new ShoppingListService(repository).toggle(3L);

        assertTrue(item.isChecked());
        verify(repository).save(item);
    }

    @Test
    void clearCompletedDeletesOnlyCheckedItems() {
        ShoppingListItem checked = new ShoppingListItem();
        checked.setId(1L);
        checked.setChecked(true);
        ShoppingListItem unchecked = new ShoppingListItem();
        unchecked.setId(2L);
        unchecked.setChecked(false);
        when(repository.findAll()).thenReturn(List.of(checked, unchecked));

        new ShoppingListService(repository).clearCompleted();

        verify(repository).delete(checked);
        verify(repository, never()).delete(unchecked);
    }

    @Test
    void clearAllDelegatesToRepository() {
        new ShoppingListService(repository).clearAll();

        verify(repository).deleteAll();
    }
}
