package ru.course.recipemanager.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String imageUri;
    private int cookingTimeMinutes;
    private int portions;
    private int calories;
    private int protein;
    private int fat;
    private int carbs;

    @Enumerated(EnumType.STRING)
    private RecipeCategory category;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private RecipeStatus status = RecipeStatus.APPROVED;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Ingredient> ingredients = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recipe_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<CookingStep> steps = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public int getCookingTimeMinutes() { return cookingTimeMinutes; }
    public void setCookingTimeMinutes(int cookingTimeMinutes) { this.cookingTimeMinutes = cookingTimeMinutes; }
    public int getPortions() { return portions; }
    public void setPortions(int portions) { this.portions = portions; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    public int getProtein() { return protein; }
    public void setProtein(int protein) { this.protein = protein; }
    public int getFat() { return fat; }
    public void setFat(int fat) { this.fat = fat; }
    public int getCarbs() { return carbs; }
    public void setCarbs(int carbs) { this.carbs = carbs; }
    public RecipeCategory getCategory() { return category; }
    public void setCategory(RecipeCategory category) { this.category = category; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public RecipeStatus getStatus() { return status; }
    public void setStatus(RecipeStatus status) { this.status = status; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
    public List<CookingStep> getSteps() { return steps; }
    public void setSteps(List<CookingStep> steps) { this.steps = steps; }
}
