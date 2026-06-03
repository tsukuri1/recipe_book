# Spring Boot backend «Ладушки»

Серверная часть для мобильной траектории курсового проекта.

## Быстрые ссылки

| Раздел | Ссылка |
|---|---|
| Корневое описание проекта | [../README.md](../README.md) |
| Документация по методичке | [../docs/README.md](../docs/README.md) |
| REST API | [../docs/09-api/openapi-endpoints.md](../docs/09-api/openapi-endpoints.md) |
| ER-модель | [../docs/images/03-er-diagram.png](../docs/images/03-er-diagram.png) |
| REST-контроллеры | [../docs/images/20-rest-controllers-diagram.png](../docs/images/20-rest-controllers-diagram.png) |
| Deployment Diagram | [../docs/images/08-deployment-diagram.png](../docs/images/08-deployment-diagram.png) |

## Соответствие PCMEF

| Слой | Пакет | Назначение |
|---|---|---|
| Control | `control` | REST-контроллеры и HTTP API |
| Mediator | `mediator` | Бизнес-логика, транзакции |
| Entity | `entity` | JPA-сущности предметной области |
| Foundation | `foundation` | Spring Data JPA репозитории |
| Security | `security` | BCrypt, JWT Bearer Token, Spring Security filter |

Общая схема PCMEF:

![PCMEF Architecture](../docs/images/06-pcmef-architecture.png)

Схема REST-контроллеров:

![REST Controllers Diagram](../docs/images/20-rest-controllers-diagram.png)

## Эндпоинты

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`
- `GET /api/recipes`
- `GET /api/recipes/{id}`
- `POST /api/recipes`
- `PUT /api/recipes/{id}`
- `DELETE /api/recipes/{id}`
- `GET /api/recipes/search`
- `POST /api/favorites/{recipeId}`
- `DELETE /api/favorites/{recipeId}`
- `GET /api/shopping-list`
- `POST /api/shopping-list/from-recipe/{recipeId}`
- `PATCH /api/shopping-list/items/{itemId}`
- `DELETE /api/shopping-list/completed`
- `DELETE /api/shopping-list`
- `GET /api/settings`
- `PUT /api/settings`
- `GET /api/admin/stats`
- `GET /api/admin/recipes/pending`
- `PATCH /api/admin/recipes/{id}/approve`
- `PATCH /api/admin/recipes/{id}/reject`
- `GET /api/admin/complaints`
- `PATCH /api/admin/complaints/{id}/review`
- `PATCH /api/admin/complaints/{id}/resolve`

## Запуск

```bash
mvn spring-boot:run
```

PostgreSQL настраивается в `src/main/resources/application.yml`.

## Проверка

```bash
mvn test jacoco:report
```

Фактический результат: 19 тестов, 0 ошибок, line coverage 64.95%.

## Важно для финальной сдачи

Структура и API соответствуют мобильной траектории методички. Пароли хешируются через BCrypt, `AuthService` возвращает подписанный JWT, а `SecurityFilterChain` закрывает приватные эндпоинты. Рецепт хранит фото (`imageUri`), КБЖУ и статус модерации.
