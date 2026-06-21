package ru.course.recipemanager;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.course.recipemanager.entity.AppUser;
import ru.course.recipemanager.entity.CookingStep;
import ru.course.recipemanager.entity.Difficulty;
import ru.course.recipemanager.entity.Ingredient;
import ru.course.recipemanager.entity.Recipe;
import ru.course.recipemanager.entity.RecipeCategory;
import ru.course.recipemanager.entity.RecipeStatus;
import ru.course.recipemanager.foundation.RecipeRepository;
import ru.course.recipemanager.foundation.UserRepository;

@SpringBootApplication
public class RecipeBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipeBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner demoData(
        UserRepository userRepository,
        RecipeRepository recipeRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            upsertDemoUser(userRepository, passwordEncoder, "Пользователь", "user@ladushki.app", "USER");
            upsertDemoUser(userRepository, passwordEncoder, "Администратор", "admin@ladushki.app", "ADMIN");
            seedDefaultRecipes(recipeRepository);
        };
    }

    private void upsertDemoUser(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        String name,
        String email,
        String role
    ) {
        AppUser user = userRepository.findByEmail(email).orElseGet(AppUser::new);
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode("1234"));
        user.setRole(role);
        userRepository.save(user);
    }

    private void seedDefaultRecipes(RecipeRepository recipeRepository) {
        saveRecipeIfMissing(
            recipeRepository,
            recipe(
                "Ягодная овсянка",
                RecipeCategory.Breakfast,
                "Нежный завтрак с ягодами, мятой и медом для спокойного утра.",
                15,
                2,
                Difficulty.Easy,
                315,
                11,
                8,
                52,
                List.of(
                    ingredient("Овсяные хлопья", "80", "г"),
                    ingredient("Молоко", "220", "мл"),
                    ingredient("Клубника и голубика", "120", "г"),
                    ingredient("Мед", "1", "ч. л.")
                ),
                List.of(
                    step(1, "Сварить овсянку на молоке до мягкой текстуры."),
                    step(2, "Добавить мед, ягоды и листики мяты."),
                    step(3, "Сохранить рецепт оффлайн и добавить ягоды в покупки.")
                )
            )
        );
        saveRecipeIfMissing(
            recipeRepository,
            recipe(
                "Паста с зеленью",
                RecipeCategory.Dinner,
                "Быстрый ужин с нежным соусом и свежей зеленью.",
                25,
                2,
                Difficulty.Easy,
                520,
                18,
                17,
                74,
                List.of(
                    ingredient("Паста", "180", "г"),
                    ingredient("Зелень", "1", "пучок")
                ),
                List.of(
                    step(1, "Отварить пасту."),
                    step(2, "Смешать с соусом и зеленью.")
                )
            )
        );
        saveRecipeIfMissing(
            recipeRepository,
            recipe(
                "Сырники к чаю",
                RecipeCategory.Dessert,
                "Воздушные сырники с ягодной подачей.",
                30,
                3,
                Difficulty.Medium,
                410,
                29,
                16,
                35,
                List.of(
                    ingredient("Творог", "400", "г"),
                    ingredient("Яйцо", "1", "шт")
                ),
                List.of(
                    step(1, "Смешать ингредиенты."),
                    step(2, "Обжарить до румяности.")
                )
            )
        );
    }

    private void saveRecipeIfMissing(RecipeRepository recipeRepository, Recipe recipe) {
        boolean exists = recipeRepository.findAll().stream()
            .anyMatch(existing -> recipe.getTitle().equalsIgnoreCase(existing.getTitle()));
        if (!exists) {
            recipeRepository.save(recipe);
        }
    }

    private Recipe recipe(
        String title,
        RecipeCategory category,
        String description,
        int cookingTimeMinutes,
        int portions,
        Difficulty difficulty,
        int calories,
        int protein,
        int fat,
        int carbs,
        List<Ingredient> ingredients,
        List<CookingStep> steps
    ) {
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setCategory(category);
        recipe.setDescription(description);
        recipe.setCookingTimeMinutes(cookingTimeMinutes);
        recipe.setPortions(portions);
        recipe.setDifficulty(difficulty);
        recipe.setCalories(calories);
        recipe.setProtein(protein);
        recipe.setFat(fat);
        recipe.setCarbs(carbs);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        recipe.setStatus(RecipeStatus.APPROVED);
        return recipe;
    }

    private Ingredient ingredient(String name, String amount, String unit) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setAmount(amount);
        ingredient.setUnit(unit);
        return ingredient;
    }

    private CookingStep step(int number, String text) {
        CookingStep step = new CookingStep();
        step.setNumber(number);
        step.setText(text);
        return step;
    }
}
