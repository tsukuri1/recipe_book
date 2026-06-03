# Спецификация методов

![REST Controllers Diagram](../images/20-rest-controllers-diagram.png)

| Метод | Назначение |
|---|---|
| `saveRecipe` | Создает новый рецепт и отправляет на проверку |
| `approveRecipe` | Убирает рецепт из списка проверки |
| `clearShoppingList` | Полностью очищает покупки |
| `updateProfile` | Сохраняет имя и аватар пользователя |

## Методы Android-клиента

| Метод | Класс | Входные данные | Результат |
|---|---|---|---|
| `login(email, password)` | `RecipeViewModel` | email, пароль | пользователь авторизован, профиль записан в состояние |
| `register(name, email, password)` | `RecipeViewModel` | имя, email, пароль | создан аккаунт, пользователь входит в приложение |
| `refreshPublicData()` | `RecipeViewModel` | нет | загружены рецепты, покупки, настройки, статистика |
| `saveRecipe(...)` | `RecipeViewModel` | поля формы рецепта | создан рецепт, добавлен в pending |
| `addSelectedRecipeToShoppingList()` | `RecipeViewModel` | выбранный рецепт | ингредиенты добавлены в покупки |
| `saveSettings(settings)` | `RecipeViewModel` | настройки | настройки сохранены и отражены в UI |

## Методы backend

| Метод | Класс | Назначение |
|---|---|---|
| `register` | `AuthService` | создаёт пользователя, хеширует пароль, выдаёт JWT |
| `login` | `AuthService` | проверяет пароль и возвращает JWT |
| `approve` | `RecipeService` | переводит рецепт в статус `APPROVED` |
| `reject` | `RecipeService` | переводит рецепт в статус `REJECTED` |
| `search` | `RecipeService` | ищет рецепты по названию или категории |
| `toggle` | `ShoppingListService` | меняет состояние позиции списка покупок |

## Ошибки и исключения

При повторной регистрации email backend возвращает конфликт. При неверном пароле возвращается ошибка авторизации. Если рецепт или позиция покупок не найдены, сервис выбрасывает исключение, которое может быть преобразовано в HTTP-ошибку на уровне контроллера.
