# Диаграммы последовательности

![Login Sequence](../images/09-login-sequence.png)

![Add And Moderate Recipe Sequence](../images/10-add-and-moderate-recipe-sequence.png)

![Shopping List Sequence](../images/11-shopping-list-sequence.png)

![Search Sequence](../images/12-search-sequence.png)

![Change Avatar Sequence](../images/13-change-avatar-sequence.png)

![Save Settings Sequence](../images/14-save-settings-sequence.png)

![Recipe State Diagram](../images/15-recipe-state-diagram.png)

![Admin Moderation Sequence](../images/18-admin-moderation-sequence.png)

```mermaid
sequenceDiagram
    actor User
    participant UI as AddRecipeScreen
    participant VM as RecipeViewModel
    participant Repo as RecipeRepository
    User->>UI: Заполняет рецепт
    UI->>VM: saveRecipe(...)
    VM->>Repo: addRecipe(recipe)
    Repo-->>VM: savedRecipe
    VM-->>UI: Сообщение о сохранении
```

## Сценарий входа

```mermaid
sequenceDiagram
    actor User
    participant UI as AuthScreen
    participant VM as RecipeViewModel
    participant Repo as RecipeRepository
    participant API as AuthController
    User->>UI: Вводит email и пароль
    UI->>VM: login(email, password)
    VM->>Repo: login(email, password)
    Repo->>API: POST /api/auth/login
    API-->>Repo: JWT + role
    Repo-->>VM: UserProfile
    VM-->>UI: isAuthenticated = true
```

## Сценарий добавления рецепта в покупки

```mermaid
sequenceDiagram
    actor User
    participant UI as RecipeDetailScreen
    participant VM as RecipeViewModel
    participant Interactor as RecipeInteractor
    participant Repo as RecipeRepository
    User->>UI: Нажимает "В покупки"
    UI->>VM: addSelectedRecipeToShoppingList()
    VM->>Interactor: addIngredientsToShoppingList(recipe.id)
    Interactor->>Repo: addIngredientsToShoppingList(ingredients)
    Repo-->>Interactor: updated list
    VM-->>UI: Сообщение об успехе
```

## Сценарий модерации

```mermaid
sequenceDiagram
    actor Admin
    participant UI as AdminScreen
    participant VM as RecipeViewModel
    participant API as AdminController
    participant Service as RecipeService
    Admin->>UI: Нажимает "Одобрить"
    UI->>VM: approveRecipe(recipeId)
    VM->>API: PATCH /api/admin/recipes/{id}/approve
    API->>Service: approve(id)
    Service-->>API: Recipe(APPROVED)
    API-->>VM: updated recipe
    VM-->>UI: Pending count уменьшен
```

## Вывод

Диаграммы последовательности показывают, что пользовательские действия проходят через несколько уровней: экран, ViewModel, Interactor/Repository, REST API и сервисы backend. Это подтверждает соблюдение PCMEF и отсутствие бизнес-логики в UI.
