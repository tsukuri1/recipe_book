package ru.course.recipemanager.control;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ComplaintController {
    private final List<ComplaintDto> complaints = new ArrayList<>(List.of(
        new ComplaintDto(1L, "Некорректный ингредиент", "Ягодная овсянка", "Не хватает количества молока.", "Новая"),
        new ComplaintDto(2L, "Дубликат рецепта", "Сырники к чаю", "Похожий рецепт уже есть в каталоге.", "На проверке")
    ));

    @PostMapping("/complaints")
    public ComplaintDto create(@RequestBody ComplaintRequest request) {
        ComplaintDto complaint = new ComplaintDto(
            nextId(),
            request.title(),
            request.recipeTitle(),
            request.description(),
            "Новая"
        );
        complaints.add(0, complaint);
        return complaint;
    }

    @GetMapping("/admin/complaints")
    public List<ComplaintDto> all() {
        return complaints;
    }

    @PatchMapping("/admin/complaints/{id}/resolve")
    public ComplaintDto resolve(@PathVariable Long id) {
        return changeStatus(id, "Закрыта");
    }

    @PatchMapping("/admin/complaints/{id}/review")
    public ComplaintDto review(@PathVariable Long id) {
        return changeStatus(id, "В работе");
    }

    private Long nextId() {
        return complaints.stream().mapToLong(ComplaintDto::id).max().orElse(0L) + 1L;
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

    public record ComplaintRequest(
        Long recipeId,
        String recipeTitle,
        String title,
        String description
    ) {}
}
