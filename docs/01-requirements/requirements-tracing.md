# Трассировка требований

![Android Layers Diagram](../images/22-android-layers-diagram.png)

| Требование | Реализация | Экран |
|---|---|---|
| FR-01 | `AuthScreen`, `RecipeViewModel.login/register` | Авторизация |
| FR-02 | `CatalogScreen`, `RecipeListCard` | Каталог |
| FR-03 | `AddRecipeScreen` | Добавление рецепта |
| FR-04 | `ShoppingListScreen` | Покупки |
| FR-05 | `AdminScreen.approveRecipe` | Админ-панель |
| FR-06 | `ProfileStore`, `ProfileScreen` | Профиль |

## Расширенная трассировка

| ID | Требование | Backend | Android | Документация |
|---|---|---|---|---|
| FR-01 | Регистрация и вход | `AuthController`, `AuthService`, `JwtTokenService` | `AuthScreen`, `RecipeViewModel.login` | [09-api](../09-api/openapi-endpoints.md) |
| FR-02 | Каталог рецептов | `RecipeController.all` | `CatalogScreen`, `RecipeCards` | [01-requirements](requirements.md) |
| FR-03 | Создание рецепта | `RecipeController.create` | `AddRecipeScreen` | [04-detailed-design](../04-detailed-design/sequence-diagrams.md) |
| FR-04 | Список покупок | `ShoppingListController` | `ShoppingListScreen` | [09-api](../09-api/openapi-endpoints.md) |
| FR-05 | Модерация | `AdminController` | `AdminScreen` | [11-guides](../11-guides/admin-guide.md) |
| FR-06 | Профиль | `UserController`, `ProfileStore` | `ProfileScreen` | [08-ui](../08-ui/ui-concept.md) |
| NFR-01 | JWT-безопасность | `SecurityConfig`, `JwtAuthenticationFilter` | `TokenStore`, `NetworkModule` | [02-architecture](../02-architecture/adr.md) |
| NFR-02 | Offline cache | Room DAO/cache entities | `CachedRecipeRepository` | [02-architecture](../02-architecture/pcmef-mobile.md) |

## Вывод

Трассировка показывает, что требования не остаются отдельным списком: каждое ключевое требование связано с конкретными классами, экранами и разделами документации. Это упрощает защиту проекта и демонстрирует связь жизненного цикла разработки с реализацией.
