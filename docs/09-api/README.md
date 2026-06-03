# 09. REST API

| Артефакт | Файл |
|---|---|
| Endpoints | [openapi-endpoints.md](openapi-endpoints.md) |
| Проверка API | [api-test.md](api-test.md) |

Swagger UI доступен после запуска backend: `http://localhost:8080/swagger-ui.html`.

## Назначение API

REST API связывает Android-клиент и backend. Клиент не обращается напрямую к PostgreSQL: все операции проходят через HTTP endpoint, где сервер проверяет JWT, роль пользователя и выполняет бизнес-логику.

![REST Controllers Diagram](../images/20-rest-controllers-diagram.png)

## Группы endpoint

- `auth` — регистрация и вход;
- `recipes` — каталог, поиск, создание и редактирование рецептов;
- `favorites` — избранные рецепты;
- `shopping-list` — список покупок;
- `settings` — пользовательские настройки;
- `admin` — статистика, модерация, жалобы.

## Безопасность API

Публичными остаются только регистрация, вход и Swagger. Остальные endpoint требуют заголовок `Authorization: Bearer <token>`. Административные endpoint доступны только роли `ADMIN`.
