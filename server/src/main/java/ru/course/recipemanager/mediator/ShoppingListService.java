package ru.course.recipemanager.mediator;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.course.recipemanager.entity.ShoppingListItem;
import ru.course.recipemanager.foundation.ShoppingListRepository;

@Service
public class ShoppingListService {
    private final ShoppingListRepository repository;

    public ShoppingListService(ShoppingListRepository repository) {
        this.repository = repository;
    }

    public List<ShoppingListItem> findAll() {
        return repository.findAll();
    }

    @Transactional
    public ShoppingListItem add(String name) {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(name);
        item.setChecked(false);
        return repository.save(item);
    }

    @Transactional
    public void toggle(Long itemId) {
        ShoppingListItem item = repository.findById(itemId).orElseThrow();
        item.setChecked(!item.isChecked());
        repository.save(item);
    }

    @Transactional
    public void clearCompleted() {
        repository.findAll().stream()
            .filter(ShoppingListItem::isChecked)
            .forEach(repository::delete);
    }

    @Transactional
    public void clearAll() {
        repository.deleteAll();
    }
}
