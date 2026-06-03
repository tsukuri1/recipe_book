package ru.course.recipemanager.control;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/complaints")
public class ComplaintController {
    private final List<ComplaintDto> complaints = new ArrayList<>(List.of(
        new ComplaintDto(1L, "Некорректный ингредиент", "Ягодная овсянка", "Не хватает количества молока.", "Новая"),
        new ComplaintDto(2L, "Дубликат рецепта", "Сырники к чаю", "Похожий рецепт уже есть в каталоге.", "На проверке")
    ));

    @GetMapping
    public List<ComplaintDto> all() {
        return complaints;
    }

    @PatchMapping("/{id}/resolve")
    public ComplaintDto resolve(@PathVariable Long id) {
        return changeStatus(id, "Закрыта");
    }

    @PatchMapping("/{id}/review")
    public ComplaintDto review(@PathVariable Long id) {
        return changeStatus(id, "В работе");
    }

    private ComplaintDto changeStatus(Long id, String status) {
        for (int i = 0; i < complaints.size(); i++) {
            ComplaintDto item = complaints.get(i);
            if (item.id().equals(id)) {
                ComplaintDto updated = new ComplaintDto(item.id(), item.title(), item.recipeTitle(), item.description(), status);
                complaints.set(i, updated);
                return updated;
            }
        }
        throw new IllegalArgumentException("Complaint not found");
    }

    public record ComplaintDto(
        Long id,
        String title,
        String recipeTitle,
        String description,
        String status
    ) {}
}
