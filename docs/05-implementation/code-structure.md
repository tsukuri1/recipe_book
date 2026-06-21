# Структура проекта

![Component Diagram](../images/07-component-diagram.png)

| Каталог | Назначение |
|---|---|
| `app` | Android Studio проект |
| `server` | Spring Boot backend |
| `docs` | Документация курсовой |
| `app/app/src/main/java/.../presentation` | UI экраны |
| `app/app/src/main/java/.../control` | ViewModel и состояние |
| `server/src/main/java/.../control` | REST controllers |

## Android-клиент

```text
presentation/
  screens/        экраны приложения
  components/     карточки, нижняя навигация, UI-компоненты
control/
  RecipeViewModel.kt
  RecipeUiState.kt
mediator/
  RecipeInteractor.kt
entity/
  Recipe.kt, Ingredient.kt, ShoppingItem.kt, UserProfile.kt
foundation/
  RecipeRepository.kt
  FakeRecipeRepository.kt
  CachedRecipeRepository.kt
  remote/
  local/
```

## Backend

```text
control/       REST-контроллеры
mediator/      сервисы и бизнес-логика
entity/        JPA-сущности
foundation/    Spring Data repositories
security/      JWT, BCrypt, фильтр авторизации
```

## Почему структура удобна

Пакеты названы по слоям PCMEF, поэтому проверяющему легко сопоставить методичку и код. Изменение UI не требует изменения JPA-репозиториев, а изменение backend API не должно затрагивать Compose-компоненты напрямую.
