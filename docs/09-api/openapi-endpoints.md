# REST API для приложения «Ладушки»

![REST Controllers Diagram](../images/20-rest-controllers-diagram.png)

Базовый URL: `/api`

## Аутентификация

| Метод | Эндпоинт | Доступ | Назначение |
|---|---|---|---|
| POST | `/auth/register` | Public | Регистрация пользователя |
| POST | `/auth/login` | Public | Получение JWT |
| GET | `/users/me` | USER | Профиль текущего пользователя |

## Рецепты

| Метод | Эндпоинт | Доступ | Назначение |
|---|---|---|---|
| GET | `/recipes` | USER | Получить список рецептов |
| GET | `/recipes/{id}` | USER | Получить рецепт по ID |
| POST | `/recipes` | USER | Создать рецепт |
| PUT | `/recipes/{id}` | OWNER, ADMIN | Обновить рецепт |
| DELETE | `/recipes/{id}` | OWNER, ADMIN | Удалить рецепт |
| GET | `/recipes/search` | USER | Поиск по названию, тегам, ингредиентам |

## Избранное и покупки

| Метод | Эндпоинт | Доступ | Назначение |
|---|---|---|---|
| GET | `/favorites` | USER | Список избранных рецептов |
| POST | `/favorites/{recipeId}` | USER | Добавить рецепт в избранное |
| DELETE | `/favorites/{recipeId}` | USER | Убрать из избранного |
| GET | `/shopping-list` | USER | Получить список покупок |
| POST | `/shopping-list/from-recipe/{recipeId}` | USER | Добавить ингредиенты рецепта |
| PATCH | `/shopping-list/items/{itemId}` | USER | Отметить продукт купленным |
| DELETE | `/shopping-list/completed` | USER | Очистить купленные продукты |
| DELETE | `/shopping-list` | USER | Полностью очистить список покупок |

## Настройки и администрирование

| Метод | Эндпоинт | Доступ | Назначение |
|---|---|---|---|
| GET | `/settings` | USER | Получить настройки приложения |
| PUT | `/settings` | USER | Сохранить настройки приложения |
| GET | `/admin/stats` | ADMIN | Получить статистику админ-панели |
| GET | `/admin/recipes/pending` | ADMIN | Получить рецепты на проверке |
| PATCH | `/admin/recipes/{id}/approve` | ADMIN | Одобрить рецепт |
| PATCH | `/admin/recipes/{id}/reject` | ADMIN | Отклонить рецепт |
| GET | `/admin/complaints` | ADMIN | Получить жалобы пользователей |
| PATCH | `/admin/complaints/{id}/review` | ADMIN | Взять жалобу в работу |
| PATCH | `/admin/complaints/{id}/resolve` | ADMIN | Закрыть жалобу |

## Основные сущности

- `User`: пользователь, роль, email, пароль в виде BCrypt-хеша.
- `Recipe`: рецепт, автор, название, описание, время, сложность, порции.
- `Ingredient`: ингредиент, количество, единица измерения.
- `CookingStep`: шаг приготовления.
- `RecipeCategory`: категория рецепта.
- `Favorite`: связь пользователя и рецепта.
- `ShoppingListItem`: позиция списка покупок.

## Связь с Android-клиентом

| Android файл | Используемые эндпоинты |
|---|---|
| `foundation/remote/RecipeApi.kt` | Все методы REST API |
| `control/RecipeViewModel.kt` | Загрузка рецептов, выбор рецепта, покупки |
| `foundation/CachedRecipeRepository.kt` | Получение данных с сервера и fallback на Room |
| `foundation/local/RecipeDao.kt` | Локальный оффлайн-кэш |
| `presentation/screens/AuthScreen.kt` | Вход и регистрация |
| `presentation/screens/SettingsScreen.kt` | Настройки |
| `presentation/screens/AdminScreen.kt` | Админ-панель |

Для эмулятора Android адрес локального Spring Boot сервера должен быть `http://10.0.2.2:8080/api/`, а не `localhost`, потому что `localhost` внутри эмулятора указывает на сам эмулятор.
