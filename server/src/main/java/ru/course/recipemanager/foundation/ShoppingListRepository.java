package ru.course.recipemanager.foundation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.recipemanager.entity.ShoppingListItem;

public interface ShoppingListRepository extends JpaRepository<ShoppingListItem, Long> {
}

