package ru.course.recipemanager.control;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
    private UserSettingsDto settings = new UserSettingsDto(true, true, true, false);

    @GetMapping
    public UserSettingsDto get() {
        return settings;
    }

    @PutMapping
    public UserSettingsDto update(@RequestBody UserSettingsDto request) {
        settings = request;
        return settings;
    }

    public record UserSettingsDto(
        boolean syncEnabled,
        boolean offlineCacheEnabled,
        boolean notificationsEnabled,
        boolean compactModeEnabled
    ) {}
}

