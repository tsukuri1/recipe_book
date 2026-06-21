# PCMEF диаграмма

![PCMEF Architecture](../images/06-pcmef-architecture.png)

| Слой | Android | Backend |
|---|---|---|
| Presentation | Compose Screens | - |
| Control | RecipeViewModel | REST Controllers |
| Mediator | RecipeInteractor | Services |
| Entity | Kotlin models | JPA entities |
| Foundation | Repository, Retrofit, Room | Spring Data repositories |

## Расшифровка слоёв

Presentation отвечает только за визуальное представление. В проекте это Compose-экраны: `HomeScreen`, `CatalogScreen`, `RecipeDetailScreen`, `AddRecipeScreen`, `ShoppingListScreen`, `ProfileScreen`, `SettingsScreen`, `AuthScreen` и `AdminScreen`.

Control принимает действия пользователя и переводит их в изменение состояния. В Android это `RecipeViewModel`, а на сервере — REST-контроллеры. Контроллеры не должны содержать бизнес-правила, их задача — принять запрос, вызвать сервис и вернуть ответ.

Mediator содержит бизнес-сценарии. В Android это `RecipeInteractor`, на backend — `AuthService`, `RecipeService`, `ShoppingListService`. Именно здесь находится логика статуса рецепта, авторизации, покупок и модерации.

Entity описывает предметную область: рецепт, ингредиент, профиль, пользователь, список покупок, настройки.

Foundation отвечает за инфраструктуру: Retrofit, Room, SharedPreferences, Spring Data JPA, PostgreSQL.

## Вывод

PCMEF-диаграмма показывает, что проект не является набором несвязанных экранов. Каждый экран проходит через цепочку слоёв, а данные хранятся и передаются через устойчивые контракты.
